package cn.cebest.service.web.infineon.impl;

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
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.infineon.SolutionComment;
import cn.cebest.entity.system.infineon.SolutionCommentVO;
import cn.cebest.entity.system.infineon.StationMessage;
import cn.cebest.service.web.infineon.SolutionCommentService;
import cn.cebest.util.HttpClientUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;
import cn.cebest.util.SystemConfig;

@Service("SolutionCommentService")
public class SolutionCommentServiceImpl implements SolutionCommentService {

	private static Logger logger = Logger.getLogger(SolutionCommentServiceImpl.class);
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 添加评论
	 */
	@Override
	public JsonResult synchComment(SolutionCommentVO solution) {
		JsonResult jr = new JsonResult();
		try {
			String content = "";
			if(solution != null && solution.getATusers() != null){
				List<String> ATusers = removeDuplicate(solution.getATusers());
				
				//@ 站内消息通知
				List<StationMessage> messageList = new ArrayList<>();
				content = "用户“"+solution.getUsername()+"”在“"+solution.getResourceName()+"”提问和回答里提到了你。点击<a href='javascript:;' class='newPath' data-name='"+solution.getPath()+"'>【此处】</a>去查看";
				for (String str : ATusers) {
					StationMessage message = new StationMessage();
					message.setContent(content);
					message.setToUserId(str);
					messageList.add(message);
				}
				dao.save("StationMessageMapper.insertMore", messageList);
			}
			dao.save("SolutionCommentMapper.insertComment", solution);
			
			
			StationMessage message = new StationMessage();
			PageData pd = new PageData();
			pd.put("contentId", solution.getContentId());
			String cont = (String) dao.findForObject("SolutionCommentMapper.getCommentById", pd);
			//回复  站内消息通知  
			if("1".equals(solution.getType())){
				// 提问被回答
				content = "您的针对解决方案“"+solution.getResourceName()+"”的提问"+cont+"有新的回答，请点击<a href='javascript:;' class='newPath' data-name='"+solution.getPath()+"'>【此处】</a>去查看";
			}else if("2".equals(solution.getType())){
				
				// 回答被回复
				content = "您的回答“"+cont+"”被回复了，请点击<a href='javascript:;' class='newPath' data-name='"+solution.getPath()+"'>【此处】</a>去查看";
			}
			
			message.setContent(content);
			message.setToUserId(solution.getToUserId());
			dao.save("StationMessageMapper.insert", message);
			jr.setCode(200);
			
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("添加评论异常", e);
		}
		return jr;
		
	}

	/**
	 * 评论列表
	 */
	@Override
	public JsonResult getCommentList(Page page) {
		JsonResult jr = new JsonResult();
		Map<String, Object> data = new HashMap<>();
		try {
			List<SolutionCommentVO> commentList = (List<SolutionCommentVO>) dao.findForList("SolutionCommentMapper.getCommentlistPage", page);
			
			StringBuffer str = new StringBuffer();
			for (SolutionCommentVO solutionCommentVO : commentList) {
				if(!"".equals(solutionCommentVO.getUserId()) && null != solutionCommentVO.getUserId()){
					
					str.append(solutionCommentVO.getUserId()+",");
				}
				if(!"".equals(solutionCommentVO.getToUserId()) && null != solutionCommentVO.getToUserId()){
					str.append(solutionCommentVO.getToUserId()+",");
				}
			}
			if(commentList != null && !commentList.isEmpty()){
				createTree(commentList,str);
				data.put("data", commentList);
				data.put("page", page);
				jr.setCode(200);
				jr.setData(data);
			}
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("获取评论列表异常", e);
		}
		return jr;
	} 

	/**
	 * 生成评论树
	 * 
	 * @param data
	 */
	public void createTree(List<SolutionCommentVO> data,StringBuffer str) {
		try {
			for (SolutionCommentVO comment : data) {
				
					List<SolutionCommentVO> commentList = (List<SolutionCommentVO>) dao.findForList("SolutionCommentMapper.getComment", comment);
					if (!commentList.isEmpty()) {
						for (SolutionCommentVO solutionCommentVO : commentList) {
							if(!"".equals(solutionCommentVO.getUserId()) && null != solutionCommentVO.getUserId()){
								str.append(solutionCommentVO.getUserId()+",");
							}
							if(!"".equals(solutionCommentVO.getToUserId()) && null != solutionCommentVO.getToUserId()){
								str.append(solutionCommentVO.getToUserId()+",");
							}
						}
						comment.setCommentList(commentList);
						createTree(commentList,str);
					}
				
			}
		
		
			if(!"".equals(str) && str != null){
				str.deleteCharAt(str.length()-1);
				Map<String, String> m = new HashMap<>();
				String s = str.toString();
				m.put("ids", s);
				String getAllUserListUrl = SystemConfig.getPropertiesString("web.allUser_list_url");
				String json = HttpClientUtil.doPost(getAllUserListUrl, m);
				if(!"".equals(json)){
					List<Map> parseArray = JSON.parseArray(json, Map.class);
					for (Map map : parseArray) {
						for (SolutionCommentVO com : data) {
							if(map.get("id").equals(com.getUserId())){
								com.setUsername((String)map.get("nickname"));
								com.setPhotoUrl((String)map.get("photoUrl"));
							}
							if(map.get("id").equals(com.getToUserId())){
								com.setToUserName((String)map.get("nickname"));
							}
							if(com != null){
								for (SolutionCommentVO com2 : data) {
									if(map.get("id").equals(com2.getUserId())){
										com2.setUsername((String)map.get("nickname"));
									}
									if(map.get("id").equals(com2.getToUserId())){
										com2.setToUserName((String)map.get("nickname"));
									}
								}
							}
						}
						
					}
				}
	    		
	    		
			}
		} catch (Exception e) {
			logger.error("获取评论树异常", e);
		}
	}
	
	@Override
	public JsonResult likeComment(PageData pd) {
		JsonResult jr = new JsonResult();
		String content = "";
		try {
			StationMessage message = new StationMessage();
			String cont = (String) dao.findForObject("SolutionCommentMapper.getCommentById", pd);
			// 提问被赞
			content = "您的针对解决方案"+pd.get("title")+"的提问"+cont+"被赞了，请点击此处<a href='javascript:;' class='newPath' data-name='"+pd.get("path")+"'>【此处】</a>查看";
			message.setContent(content);
			message.setToUserId((String) pd.get("userId"));
			dao.save("StationMessageMapper.insert", message);
			dao.save("SolutionCommentMapper.likeComment", pd);
			jr.setCode(200);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("点赞异常", e);
		}
		return jr;
	}
	
	@Override
	public JsonResult cancelLike(PageData pd) {
		JsonResult jr = new JsonResult();
		try {
			dao.delete("SolutionCommentMapper.cancelLike", pd);
			jr.setCode(200);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("取消点赞异常", e);
		}
		return jr;
	}


	/**
	 * 去重
	 * @param list
	 * @return
	 */
	public static List<String> removeDuplicate(List<String> list)  {       
		for( int i = 0 ; i < list.size()-1 ; i ++ ){       
	        for( int j = list.size()-1 ; j > i; j -- ){       
	            if(list.get(j).equals(list.get(i))){       
	               list.remove(j);       
	            }        
	        }        
	    }        
	     return list;       
	}

	@Override
	public JsonResult getAllUser(PageData pd) {
		JsonResult jr = new JsonResult();
		String getAllUserListUrl = SystemConfig.getPropertiesString("web.allUser_list_url");
		
		try {
        		String doGet = HttpClientUtil.doGet(getAllUserListUrl);
        		JSONObject parseObject = JSON.parseObject(doGet);
            	jr.setData(parseObject);
            	jr.setCode(200);
            
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("获取话题详情---->拼装用户信息失败:",e);
			e.printStackTrace();
		}
		return jr;
	}

	
	
}
