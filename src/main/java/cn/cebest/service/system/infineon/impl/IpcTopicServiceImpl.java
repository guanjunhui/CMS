package cn.cebest.service.system.infineon.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.MsgTemplate;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.IpcSiteMsgService;
import cn.cebest.service.system.infineon.IpcTopicService;
import cn.cebest.util.Const;
import cn.cebest.util.EmailInfo;
import cn.cebest.util.EmailUtil;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;
import cn.cebest.util.SendEmialThread;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.UuidUtil;

/**
 *
 * @author wangweijie
 * @Date 2018年9月27日
 * @company 中企高呈
 */
@Service
public class IpcTopicServiceImpl implements IpcTopicService{
	private static Logger logger = Logger.getLogger(IpcTopicServiceImpl.class);
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Autowired
	private IpcSiteMsgService ipcSiteMsgService;

	@Override
	public Map<String, Object> getMyTopicListByUserId(Page page) throws Exception{
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> collections = new ArrayList<>();
		collections = (List<Map<String, Object>>) dao.findForList("IpcTopicMapper.getMyTopicByUserIdlistPage", page);
		data.put("data", collections);
		data.put("page", page);
		return data;
	}

	@Override
	public void saveTopic(PageData pd) throws Exception {
		//保存话题
		this.dao.save("IpcTopicMapper.saveTopic", pd);
	}
	
	@Override
	public void saveTopicReply(PageData pd)throws Exception {
		//保存话题评论
		this.dao.save("IpcTopicMapper.saveTopicReply", pd);
		
		//发送站内信
		String template = null;
		String baseUrl = SystemConfig.getPropertiesString("web.base_url");
		if(StringUtils.isEmpty(pd.getString("parentId")) || "0".equals(pd.getString("parentId"))){
			//一级评论
			template = new MsgTemplate().getTemplate(Const.MSG_TEMPLATE_REPLY_TOPIC);
			pd.put("type", Const.MSG_TEMPLATE_REPLY_TOPIC);
		}else{
			//子评论
			template = new MsgTemplate().getTemplate(Const.MSG_TEMPLATE_REPLY_REPLY);
			pd.put("type", Const.MSG_TEMPLATE_REPLY_REPLY);
		}
		if(!StringUtils.isEmpty(template)){
			//发送站内信
			template = template.replace("{title}", pd.getString("title"));
			template = template.replace("{url}", baseUrl + "/jump/ask_details?topicId=" + pd.getString("topicId"));
			pd.put("content", template);
			this.ipcSiteMsgService.saveMsg(pd);
		}
	}

	@Override
	public Map<String, Object> getHotList(Page page)throws Exception  {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> collections = new ArrayList<>();
		collections = (List<Map<String, Object>>) dao.findForList("IpcTopicMapper.getHotlistPage", page);
		data.put("data", collections);
		data.put("page", page);
		return data;
	}
	
	@Override
	public Map<String, Object> getTopicById(Map<String,String> pMap)throws Exception {
		Map<String, Object> data = (Map<String, Object>) dao.findForObject("IpcTopicMapper.getTopicById", pMap);
		if(data != null){
			//拼装用户信息
			String getAllUserListUrl = SystemConfig.getPropertiesString("web.center_url") + "user/getAllUser?ids=" + data.get("userId").toString();
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet get = new HttpGet(getAllUserListUrl);
			
	        try {
	        	//post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
				HttpResponse response = httpClient.execute(get);
				// 判断网络连接状态码是否正常(0--200都数正常)
				String result = null;
	            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                result= EntityUtils.toString(response.getEntity(),"utf-8");
	            } 
	            if(StringUtils.isNotEmpty(result)){
	            	ObjectMapper mapper = new ObjectMapper();
	            	//组装用户信息
	            	ArrayList<Map<String, Object>> uList = mapper.readValue(result, ArrayList.class);
	            	if(uList!=null && uList.size()>0 && data!=null){
						String id = data.get("userId").toString();
						for (Map<String, Object> map1 : uList) {
							if(map1.get("id").toString().equals(id)){
								data.put("name", map1.get("name"));
								data.put("surname", map1.get("surname"));
								data.put("nickname", map1.get("nickname"));
								data.put("photoUrl", map1.get("photoUrl"));
							}
						}
					}
	            }
			} catch (Exception e) {
				logger.error("获取话题详情---->拼装用户信息失败:",e);
				e.printStackTrace();
			}
		}
		return data;
	}

	@Override
	public Map<String, Object> getAllList(Page page) throws Exception {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> collections = (List<Map<String, Object>>) dao.findForList("IpcTopicMapper.getAlllistPage", page);
		List<Map<String, Object>> rList = null;
		if(collections != null && collections.size()>0){
			 //拼装用户信息
			
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			//组装所有ids
			//List<String> idsList = new ArrayList<>();
			StringBuffer buffer = new StringBuffer();
			
			for (Map<String, Object> map : collections) {
				String _id = map.get("userId").toString();
				//idsList.add(_id);
				buffer.append(_id+",");
			}
			String getAllUserListUrl = SystemConfig.getPropertiesString("web.center_url") + "user/getAllUser?ids=" + buffer.toString();
			HttpGet get = new HttpGet(getAllUserListUrl);
	        try {
	        	//post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
				HttpResponse response = httpClient.execute(get);
				// 判断网络连接状态码是否正常(0--200都数正常)
				String result = null;
	            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                result= EntityUtils.toString(response.getEntity(),"utf-8");
	            } 
	            if(StringUtils.isNotEmpty(result)){
	            	ObjectMapper mapper = new ObjectMapper();
	            	//组装用户信息
	            	ArrayList<Map<String, Object>> uList = mapper.readValue(result, ArrayList.class);
	            	if(uList!=null && uList.size()>0 && collections!=null && collections.size()>0){
	            		rList = new ArrayList<Map<String, Object>>();
	            		for (Map<String, Object> map : collections) {
							String id = map.get("userId").toString();
							for (Map<String, Object> map1 : uList) {
								if(map1.get("id").toString().equals(id)){
									map.put("name", map1.get("name"));
									map.put("surname", map1.get("surname"));
									map.put("nickname", map1.get("nickname"));
									map.put("photoUrl", map1.get("photoUrl"));
									rList.add(map);
								}
							}
						}
	            	}
	            	
	            }
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		data.put("data", collections);
		data.put("page", page);
		return data;
	}

	@Override
	public Map<String, Object> getReplyList(Page page) throws Exception {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> collections = (List<Map<String, Object>>) dao.findForList("IpcTopicMapper.getReplylistPage", page);
		List<Map<String, Object>> rList = null;
		if(collections != null && collections.size() > 0){
			//拼装用户信息
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			//组装所有ids
			//List<String> idsList = new ArrayList<>();
			StringBuffer buffer = new StringBuffer(); 
			for (Map<String, Object> map : collections) {
				String _id = map.get("userId").toString();
				//idsList.add(_id);
				buffer.append(_id+",");
			}
			String getAllUserListUrl = SystemConfig.getPropertiesString("web.center_url") + "user/getAllUser?ids=" + buffer.toString();
			HttpGet get = new HttpGet(getAllUserListUrl);
			
	        try {
	        	//post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
				HttpResponse response = httpClient.execute(get);
				// 判断网络连接状态码是否正常(0--200都数正常)
				String result = null;
	            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                result= EntityUtils.toString(response.getEntity(),"utf-8");
	            } 
	            if(StringUtils.isNotEmpty(result)){
	            	ObjectMapper mapper = new ObjectMapper();
	            	//组装用户信息
	            	ArrayList<Map<String, Object>> uList = mapper.readValue(result, ArrayList.class);
	            	if(uList!=null && uList.size()>0 && collections!=null && collections.size()>0){
	            		rList = new ArrayList<Map<String, Object>>();
	            		for (Map<String, Object> map : collections) {
							String userId = map.get("userId").toString();
							String touserId = map.get("touserId").toString();
							for (Map<String, Object> map1 : uList) {
								if(map1.get("id").toString().equals(userId)){
									map.put("nickname", map1.get("nickname"));
									map.put("photoUrl", map1.get("photoUrl"));
								}
								if(map1.get("id").toString().equals(touserId)){
									map.put("tonickname", map1.get("nickname"));
								}
							}
							rList.add(map);
						}
	            	}
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		data.put("data", collections);
		data.put("page", page);
		return data;
	}
	@Override
	public List<Map<String, Object>> getReply2List(PageData pd) throws Exception {
		List<Map<String, Object>> collections = (List<Map<String, Object>>) dao.findForList("IpcTopicMapper.getReply2List", pd);
		List<Map<String, Object>> rList = null;
		if(collections != null && collections.size() > 0){
			//拼装用户信息
			//组装所有ids
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			//List<String> idsList = new ArrayList<>();
			StringBuffer buffer = new StringBuffer();
			for (Map<String, Object> map : collections) {
				String _id = map.get("userId").toString();
				//idsList.add(_id);
				buffer.append(_id+",");
			}
			String getAllUserListUrl = SystemConfig.getPropertiesString("web.center_url") + "user/getAllUser?ids=" + buffer.toString();
			HttpGet get = new HttpGet(getAllUserListUrl);
	        try {
	        	//post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
				HttpResponse response = httpClient.execute(get);
				// 判断网络连接状态码是否正常(0--200都数正常)
				String result = null;
	            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                result= EntityUtils.toString(response.getEntity(),"utf-8");
	            } 
	            if(StringUtils.isNotEmpty(result)){
	            	ObjectMapper mapper = new ObjectMapper();
	            	//组装用户信息
	            	ArrayList<Map<String, Object>> uList = mapper.readValue(result, ArrayList.class);
	            	if(uList!=null && uList.size()>0 && collections!=null && collections.size()>0){
	            		rList = new ArrayList<Map<String, Object>>();
	            		for (Map<String, Object> map : collections) {
							String userId = map.get("userId").toString();
							String touserId = map.get("touserId").toString();
							for (Map<String, Object> map1 : uList) {
								if(map1.get("id").toString().equals(userId)){
									map.put("nickname", map1.get("nickname"));
									map.put("photoUrl", map1.get("photoUrl"));
								}
								if(map1.get("id").toString().equals(touserId)){
									map.put("tonickname", map1.get("nickname"));
								}
							}
							rList.add(map);
						}
	            	}
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return collections;
	}

	
	
	@Override
	public void updatePageViews(String topicId) throws Exception {
		//修改话题浏览量
		this.dao.save("IpcTopicMapper.updatePageViews", topicId);
	}

	@Override
	public void closeTopic(PageData pd) throws Exception {
		
		if(pd.get("state").toString().equals("1")){
			//已解决
			//设置最佳答案
			this.dao.update("IpcTopicMapper.setAdoptReply", pd);
			//发送站内信
			String template = new MsgTemplate().getTemplate(Const.MSG_TEMPLATE_REPLY_ADOPT);
			PageData pd1 = new PageData();
			String baseUrl = SystemConfig.getPropertiesString("web.base_url");
			pd1.put("type", Const.MSG_TEMPLATE_REPLY_ADOPT);
			template = template.replace("{title}", pd.getString("reply"));
			template = template.replace("{url}", baseUrl + "/jump/ask_details?topicId=" + pd.getString("topicId"));
			pd1.put("content", template);
			pd1.put("id", UuidUtil.get32UUID());
			pd1.put("userId", "000");
			pd1.put("touserId", pd.get("touserId"));
			this.ipcSiteMsgService.saveMsg(pd1);
		}
		//关闭话题
		this.dao.update("IpcTopicMapper.closeTopic", pd);
	}

	@Override
	public Map<String, Object> getMyAnswerTopic(Page page) throws Exception {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> collections = new ArrayList<>();
		collections = (List<Map<String, Object>>) dao.findForList("IpcTopicMapper.getMyAnswerTopiclistPage", page);
		data.put("data", collections);
		data.put("page", page);
		return data;
	}

	@Override
	public List<Map<String, Object>> getMyReplyByUserIdAndTopicId(Map<String, String> pMap) throws Exception {
		return  (List<Map<String, Object>>) dao.findForList("IpcTopicMapper.getMyReplyByUserIdAndTopicId", pMap);
	}

	@Override
	public void deleteById(PageData pd) throws Exception {
		//删除话题
		dao.delete("IpcTopicMapper.deleteById", pd);
		//删除话题回复
		dao.delete("IpcTopicMapper.deleteReplyByTipicId", pd);
		//删除话题点赞
		dao.delete("IpcTopicMapper.deleteThumbByTipicId", pd);
		//删除话题收藏
		dao.delete("IpcTopicMapper.deleteCollByTipicId", pd);
	}

	@Override
	public void deleteReplyById(PageData pd) throws Exception {
		//删除话题回复
		dao.delete("IpcTopicMapper.deleteReplyById", pd);
	}

	@Override
	public Map<String, Object> getAllReplyList(Page page) throws Exception {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> collections = (List<Map<String, Object>>) dao.findForList("IpcTopicMapper.getAllReplylistPage", page);
		List<Map<String, Object>> rList = null;
		if(collections != null && collections.size() > 0){
			//拼装用户信息
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			//组装所有ids
			//List<String> idsList = new ArrayList<>();
			StringBuffer buffer = new StringBuffer(); 
			for (Map<String, Object> map : collections) {
				String _id = map.get("userId").toString();
				//idsList.add(_id);
				buffer.append(_id+",");
			}
			String getAllUserListUrl = SystemConfig.getPropertiesString("web.center_url") + "user/getAllUser?ids=" + buffer.toString();
			HttpGet get = new HttpGet(getAllUserListUrl);
			
	        try {
	        	//post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
				HttpResponse response = httpClient.execute(get);
				// 判断网络连接状态码是否正常(0--200都数正常)
				String result = null;
	            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                result= EntityUtils.toString(response.getEntity(),"utf-8");
	            } 
	            if(StringUtils.isNotEmpty(result)){
	            	ObjectMapper mapper = new ObjectMapper();
	            	//组装用户信息
	            	ArrayList<Map<String, Object>> uList = mapper.readValue(result, ArrayList.class);
	            	if(uList!=null && uList.size()>0 && collections!=null && collections.size()>0){
	            		rList = new ArrayList<Map<String, Object>>();
	            		for (Map<String, Object> map : collections) {
							String userId = map.get("userId").toString();
							String touserId = map.get("touserId").toString();
							for (Map<String, Object> map1 : uList) {
								if(map1.get("id").toString().equals(userId)){
									map.put("nickname", map1.get("nickname"));
									map.put("photoUrl", map1.get("photoUrl"));
								}
								if(map1.get("id").toString().equals(touserId)){
									map.put("tonickname", map1.get("nickname"));
								}
							}
							rList.add(map);
						}
	            	}
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		data.put("data", collections);
		data.put("page", page);
		return data;
	}

	@Override
	public Object detailById(String id) throws Exception {
		return this.dao.findForObject("IpcTopicMapper.detailById", id);
	}

	@Override
	public void scanTopic() throws Exception {
		@SuppressWarnings("unchecked")
		List<Map<String, String>> rList = (List<Map<String, String>>) this.dao.findForList("IpcTopicMapper.scanTopic", null);
		if(rList != null && rList.size()>0){
			String format = "{num}、<a href='{href}'>【{title}】</a>,发表于{time}<br/>";
			StringBuffer sb = new StringBuffer();
			String baseUrl = SystemConfig.getPropertiesString("web.base_url");
			int i = 1;
			sb.append("尊敬的管理员，请尽快处理英飞凌家电生态圈技术问答的以下提问：<br/>");
			for (Map<String, String> map : rList) {
				sb.append(format.replace("{num}", String.valueOf(i++)).replace("{href}", baseUrl + "/jump/ask_details?topicId=" + map.get("id")).replace("{title}", map.get("title")).replace("{time}", map.get("createTime")));
			}
			sb.append("谢谢!<br/>英飞凌家电生态圈");
			
	        EmailInfo info = new EmailInfo();
	        info.setHost(SystemConfig.getPropertiesString("web.mail_host"));
	        info.setPort(SystemConfig.getPropertiesString("web.mail_port"));
	        info.setAccount(SystemConfig.getPropertiesString("web.mail_account"));
	        info.setPassword(SystemConfig.getPropertiesString("web.mail_password"));
	        info.setAutograph("英飞凌家电生态圈");
	        info.setSubject("英飞凌家电生态圈技术问答提问待处理");
	        info.setContent(sb.toString());
	        info.setTomail(SystemConfig.getPropertiesString("web.admin_email"));
	        try {
				//发送邮件
				new EmailUtil().sendMail(info);
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
