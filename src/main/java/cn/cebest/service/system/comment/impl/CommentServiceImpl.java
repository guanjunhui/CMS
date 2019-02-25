package cn.cebest.service.system.comment.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.User;
import cn.cebest.service.system.comment.CommentService;
import cn.cebest.util.DateUtil;
import cn.cebest.util.EmailInfo;
import cn.cebest.util.EmailUtil;
import cn.cebest.util.Logger;
import cn.cebest.util.Message;
import cn.cebest.util.PageData;
import cn.cebest.util.SystemConfig;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	private static Logger logger = Logger.getLogger(CommentServiceImpl.class);
	
	/**
	 * <p>Title: reSendMail</p>   
	 * <p>Description: 通过管理员ID与解决方案ID和重发提起 </p>   
	 * @return
	 * @throws Exception   
	 * @see cn.cebest.service.system.comment.CommentService#reSendMail()
	 */
	@Override
	public String reSendMail(PageData pageData) throws Exception {
		Page page = new Page<>();
		PageData pd = new PageData();
		page.setPd(pd);
		String date = pageData.getString("DATE");
		List<User> users = (List<User>) dao.findForList("UserMapper.userlistPage", page);
		//不存在非admin以外状态为启用的管理员
		if (users.size()<1) {
			return null;
		}
		for (int i = 0; i < users.size(); i++) {
			String mail = users.get(i).getEMAIL();
			if (!StringUtils.isBlank(mail) && users.get(i).getUSER_ID().equals(pageData.get("USERID"))) {
				String USER_ID = users.get(i).getUSER_ID();
				//根据用户ID查找当前用户关联的解决方案ID列表
				List<String> resourceId = (List<String>) dao.findForList("CommentMapper.findCommentIdListByUserId",USER_ID);
				//每个方案都要发一件邮件
				String resendId = pageData.getString("RESOURCEID");
				for (int j = 0; j < resourceId.size(); j++) {
					if (!resendId.contains((resourceId.get(j).replaceAll(" ", "")))) {
						continue;
					}
					//查找当前方案详情的跳转路径
					Map<String, Object> messageResource =  (Map<String, Object>) dao.findForObject("CommentMapper.findSendMessageResource", resourceId.get(j));
					Map<String, Object> messageMap = new HashMap<String,Object>();
					messageMap.put("resourceId", resourceId.get(j));
					messageMap.put("date", date);
					//第一种：判断已有评论中是否有新增评论
					List<Message> newCommentList = (List<Message>) dao.findForList("CommentMapper.findNewCommentList", messageMap);
					for (Message message : newCommentList) {
						//根据ID查找对应子评论
						messageMap.put("PARENT_ID", message.getCommentId());
						List<Message> sonMessage = (List<Message>) dao.findForList("CommentMapper.findNewSonCommentList", messageMap);
						message.setSonList(sonMessage);
						message.setSonCount(sonMessage.size());
					}
					//第二种:当前评论不是当前日期,但回答是当前日期的
					List<Message> oldCommentList = (List<Message>) dao.findForList("CommentMapper.findNewSonMessageList",messageMap);
					//发送邮件
					if (newCommentList.size()>0 || oldCommentList.size()>0) {
						//项目域名
						String baseUrl = SystemConfig.getPropertiesString("web.base_url");
						String jumpUrl = baseUrl+"/skippath/"+messageResource.get("SKIPPATH")+"/"+messageResource.get("SKIPPATH")+"_"+messageResource.get("SKIPID");//解决方案跳转路径
						StringBuffer emailMessage = new StringBuffer();
						emailMessage.append("尊敬的管理员,<br/><br/>");
						emailMessage.append("<a href='"+jumpUrl+"'>");
						emailMessage.append(messageResource.get("CONTENT_TITLE")+"</a>最新提问和回答:<br/><br/>");
						for (int k = 0; k < newCommentList.size(); k++) {
							Message message = newCommentList.get(k);
							emailMessage.append("&nbsp&nbsp&nbsp&nbsp<a href='"+jumpUrl+"'>");
							emailMessage.append(message.getCommentTitle()+"</a>("+message.getSonCount()+"个新回答)<br/><br/>");
							if (message.getSonCount()>0) {
								for (Message sonMessage : message.getSonList()) {
									emailMessage.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+(k+1)+"."+"<a href='"+jumpUrl+"'>");
									emailMessage.append(sonMessage.getCommentTitle()+"</a><br/><br/>");
								}
							}
						}
						for (int k = 0; k < oldCommentList.size(); k++) {
							Message message = oldCommentList.get(k);
							emailMessage.append("&nbsp&nbsp&nbsp&nbsp<a href='"+jumpUrl+"'>");
							emailMessage.append(message.getCommentTitle()+"</a>("+message.getSonCount()+"个新回答)<br/><br/>");
							if (message.getSonCount()>0) {
								for (Message sonMessage : message.getSonList()) {
									emailMessage.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+(k+1)+"."+"<a href='"+jumpUrl+"'>");
									emailMessage.append(sonMessage.getCommentTitle()+"</a><br/><br/>");
								}
							}
						}
						emailMessage.append("<br/><br/><br/><br/>英飞凌电源与传感社区");
						EmailInfo info = new EmailInfo();
				        info.setHost(SystemConfig.getPropertiesString("web.mail_host"));
				        info.setPort(SystemConfig.getPropertiesString("web.mail_port"));
				        info.setAccount(SystemConfig.getPropertiesString("web.mail_account"));
				        info.setPassword(SystemConfig.getPropertiesString("web.mail_password"));
				        info.setAutograph("英飞凌电源与传感社区");//发件人
				        info.setSubject("英飞凌电源与传感社区 -解决方案【"+messageResource.get("CONTENT_TITLE")+"】有新的提问和回答");//主题
				        info.setContent(emailMessage.toString());
				        info.setTomail(mail);
				        try {
							//发送邮件
				        	logger.info("send email==================="+info.toString());
							new EmailUtil().sendMail(info);
							logger.info("==============send email success");
						} catch (Exception e) {
							logger.info("发送邮件主服务器异常："+ e);
							logger.info("切换备用服务器：");
							info.setHost(SystemConfig.getPropertiesString("web.mail_spare_host"));
							try {
								new EmailUtil().sendMail(info);
							} catch (Exception e1) {
								logger.info("发送邮件备用服务器异常：" + e);
							}
						}
					}
				}
				return "success";
			}
		}
		return null;
	}
	
	/**
	 * <p>Title: sendRelationMail</p>   
	 * <p>Description: 根据用户ID查询解决方案ID并查找新增的评论并发送邮件</p>   
	 * @throws Exception   
	 * @see cn.cebest.service.system.comment.CommentService#sendRelationMail()
	 */
	@Override
	public void sendRelationMail() throws Exception {
		Page page = new Page<>();
		PageData pd = new PageData();
		page.setPd(pd);
		List<User> users = (List<User>) dao.findForList("UserMapper.userlistPage", page);
		//不存在非admin以外状态为启用的管理员
		if (users.size()<1) {
			return;
		}
		for (int i = 0; i < users.size(); i++) {
			String mail = users.get(i).getEMAIL();
			if (!StringUtils.isBlank(mail)) {
				String USER_ID = users.get(i).getUSER_ID();
				//根据用户ID查找当前用户关联的解决方案ID列表
				List<String> resourceId = (List<String>) dao.findForList("CommentMapper.findCommentIdListByUserId",USER_ID);
				//每个方案都要发一件邮件
				for (int j = 0; j < resourceId.size(); j++) {
					//通过资源ID查找前一天内新增的评论和回复
					//查找当前方案详情的跳转路径
					Map<String, Object> messageResource =  (Map<String, Object>) dao.findForObject("CommentMapper.findSendMessageResource", resourceId.get(j));
					Map<String, Object> messageMap = new HashMap<String,Object>();
					messageMap.put("resourceId", resourceId.get(j));
					messageMap.put("date", DateUtil.beforeDayFormat());
					//第一种：判断已有评论中是否有新增评论
					List<Message> newCommentList = (List<Message>) dao.findForList("CommentMapper.findNewCommentList", messageMap);
					for (Message message : newCommentList) {
						//根据ID查找对应子评论
						messageMap.put("PARENT_ID", message.getCommentId());
						List<Message> sonMessage = (List<Message>) dao.findForList("CommentMapper.findNewSonCommentList", messageMap);
						message.setSonList(sonMessage);
						message.setSonCount(sonMessage.size());
					}
					//第二种:当前评论不是当前日期,但回答是当前日期的
					List<Message> oldCommentList = (List<Message>) dao.findForList("CommentMapper.findNewSonMessageList",messageMap);
					//发送邮件
					if (newCommentList.size()>0 || oldCommentList.size()>0) {
						//项目域名
						String baseUrl = SystemConfig.getPropertiesString("web.base_url");
						String jumpUrl = baseUrl+"/skippath/"+messageResource.get("SKIPPATH")+"/"+messageResource.get("SKIPPATH")+"_"+messageResource.get("SKIPID");//解决方案跳转路径
						StringBuffer emailMessage = new StringBuffer();
						emailMessage.append("尊敬的管理员,<br/><br/>");
						emailMessage.append("<a href='"+jumpUrl+"'>");
						emailMessage.append(messageResource.get("CONTENT_TITLE")+"</a>最新提问和回答:<br/><br/>");
						for (int k = 0; k < newCommentList.size(); k++) {
							Message message = newCommentList.get(k);
							emailMessage.append("&nbsp&nbsp&nbsp&nbsp<a href='"+jumpUrl+"'>");
							emailMessage.append(message.getCommentTitle()+"</a>("+message.getSonCount()+"个新回答)<br/><br/>");
							if (message.getSonCount()>0) {
								for (Message sonMessage : message.getSonList()) {
									emailMessage.append("<a href='"+jumpUrl+"'>");
									emailMessage.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+(k+1)+"."+sonMessage.getCommentTitle()+"</a><br/><br/>");
								}
							}
						}
						for (int k = 0; k < oldCommentList.size(); k++) {
							Message message = oldCommentList.get(k);
							emailMessage.append("&nbsp&nbsp&nbsp&nbsp<a href='"+jumpUrl+"'>");
							emailMessage.append(message.getCommentTitle()+"</a>("+message.getSonCount()+"个新回答)<br/><br/>");
							if (message.getSonCount()>0) {
								for (Message sonMessage : message.getSonList()) {
									emailMessage.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+(k+1)+"."+"<a href='"+jumpUrl+"'>");
									emailMessage.append(sonMessage.getCommentTitle()+"</a><br/><br/>");
								}
							}
						}
						emailMessage.append("英飞凌电源与传感社区");
						EmailInfo info = new EmailInfo();
				        info.setHost(SystemConfig.getPropertiesString("web.mail_host"));
				        info.setPort(SystemConfig.getPropertiesString("web.mail_port"));
				        info.setAccount(SystemConfig.getPropertiesString("web.mail_account"));
				        info.setPassword(SystemConfig.getPropertiesString("web.mail_password"));
				        info.setAutograph("英飞凌电源与传感社区");//发件人
				        info.setSubject("英飞凌电源与传感社区 -解决方案"+messageResource.get("CONTENT_TITLE")+"有新的提问和回答");//主题
				        info.setContent(emailMessage.toString());
				        info.setTomail(mail);
				        try {
							//发送邮件
				        	logger.info("auto send email begin:"+emailMessage.toString());
							new EmailUtil().sendMail(info);
							logger.info("auto send email success");
						} catch (Exception e) {
							logger.info("发送邮件主服务器异常："+ e);
							logger.info("切换备用服务器：");
							info.setHost(SystemConfig.getPropertiesString("web.mail_spare_host"));
							try {
								new EmailUtil().sendMail(info);
							} catch (Exception e1) {
								logger.info("发送邮件备用服务器异常：" + e);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * <p>Title: deleteRelationByIds</p>   
	 * <p>Description: 根据管理员ID和解决方案ID删除关联表中对应的关系</p>   
	 * @param pd
	 * @throws Exception   
	 * @see cn.cebest.service.system.comment.CommentService#deleteRelationByIds(cn.cebest.util.PageData)
	 */
	@Override
	public void deleteRelationByIds(PageData pd) throws Exception {
		dao.delete("CommentMapper.deleteCommentByUserIdAndComId", pd);
	}
	
	/**
	 * <p>Title: insertCommentByUserId</p>   
	 * <p>Description: 解决方案和管理员添加关联关系</p>   
	 * @param pd
	 * @throws Exception   
	 * @see cn.cebest.service.system.comment.CommentService#insertCommentByUserId(cn.cebest.util.PageData)
	 */
	@Override
	public void insertCommentByUserId(List<Map<String, Object>> pd) throws Exception {
		dao.save("CommentMapper.saveCommentByUserId", pd);
	}
	
	/**
	 * <p>Title: findNoReCommentListByUserId</p>   
	 * <p>Description: 通过用户ID查找未关联的解决方案列表</p>   
	 * @param pd
	 * @return
	 * @throws Exception   
	 * @see cn.cebest.service.system.comment.CommentService#findNoReCommentListByUserId(cn.cebest.util.PageData)
	 */
	@Override
	public List<PageData> findNoReCommentListByUserId(PageData pd) throws Exception {
		List<PageData> comments = (List<PageData>) dao.findForList("CommentMapper.findNoReCommentListByUserId", pd);
		return comments;
	}
	
	/**
	 * <p>Title: findCommentListByUserId</p>   
	 * <p>Description: 通过用户ID查找关联的解决方案列表</p>   
	 * @param pd
	 * @return
	 * @throws Exception   
	 * @see cn.cebest.service.system.comment.CommentService#findCommentListByUserId(cn.cebest.util.PageData)
	 */
	@Override
	public List<PageData> findCommentListByUserId(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("CommentMapper.findCommentListByUserId",pd);
	}
	
	/**
	 * 根据关联ID查询评论列表
	 * @throws Exception 
	 */
	@Override
	public List<PageData> listcommentlistPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("CommentMapper.commentlistPage", page);
	}
	
	/**
	 * 前台接口,根据提问ID查询跟评
	 */
	@Override
	public List<PageData> commentReplylistpage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("CommentMapper.commentReplylistpage", page);
	}
	
	/**
	 * 新增评论
	 * @throws Exception 
	 */
	@Override
	public void saveComment(PageData pd) throws Exception {
		dao.save("CommentMapper.saveCommentProperty", pd);
		dao.save("CommentMapper.saveComment", pd);
	}
	
	/**
	 * 回复提问
	 */
	@Override
	public void saveCommentReply(PageData pd) throws Exception {
		dao.save("CommentMapper.saveCommentReply", pd);
	}

	/**
	 * 更新回复提问
	 */
	@Override
	public void updateCommentReply(PageData pd) throws Exception {
		dao.update("CommentMapper.updateCommentReply", pd);
	}
	
	/**
	 * 删除评论
	 * @throws Exception 
	 */
	@Override
	public void delComment(PageData pd) throws Exception {
		dao.update("CommentMapper.delComment", pd);
		
	}

	/**
	 * 根据id查询
	 */
	@Override
	public PageData findCommentById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("CommentMapper.findCommentById", pd);
	}

	/**
	 * 根据id审核
	 * @throws Exception 
	 */
	@Override
	public void auditComment(PageData pd) throws Exception {
		dao.update("CommentMapper.auditComment", pd);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void deleteAllComment(String[] arrayCOMMENT_IDS) throws Exception {
		dao.update("CommentMapper.deleteAllComment", arrayCOMMENT_IDS);	//删除评论/提问
	}

	@Override
	public List<PageData> commentManagerlistPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("CommentMapper.commentManagerlistPage", page);
	}

}
