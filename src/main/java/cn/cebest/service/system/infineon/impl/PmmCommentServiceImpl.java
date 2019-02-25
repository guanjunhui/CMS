package cn.cebest.service.system.infineon.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.infineon.SolutionCommentVO;
import cn.cebest.service.system.infineon.PmmCommentService;
import cn.cebest.util.HttpClientUtil;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;
import cn.cebest.util.SystemConfig;

@Service("PmmCommentService")
public class PmmCommentServiceImpl implements PmmCommentService {

private static Logger logger = Logger.getLogger(PmmCommentServiceImpl.class);
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Override
	public Map<String, Object> getCommentList(Page page) {
		Map<String, Object> data = new HashMap<>();
		try {
			
			List<SolutionCommentVO> commentList = (List<SolutionCommentVO>) dao.findForList("PmmCommentMapper.getCommentlistPage", page);
			StringBuffer str = new StringBuffer();
			for (SolutionCommentVO solutionCommentVO : commentList) {
				if(!"".equals(solutionCommentVO.getUserId()) && null != solutionCommentVO.getUserId()){
					
					str.append(solutionCommentVO.getUserId()+",");
				}
			}
			if(str.length()>0){
				str.deleteCharAt(str.length()-1);
				Map<String, String> m = new HashMap<>();
				String s = str.toString();
				m.put("ids", s);
				String getAllUserListUrl = SystemConfig.getPropertiesString("web.allUser_list_url");
				String json = HttpClientUtil.doPost(getAllUserListUrl, m);
				if(!"".equals(json)){
					List<Map> parseArray = JSON.parseArray(json, Map.class);
					for (Map map : parseArray) {
						for (SolutionCommentVO com : commentList) {
							if(map.get("id").equals(com.getUserId())){
								com.setUsername((String)map.get("nickname"));
							}
						}
						
					}
				}
			}
			
			data.put("data", commentList);
			data.put("page", page);
		} catch (Exception e) {
			logger.error("获取评论列表异常", e);
		}
		
		return data;
	}

	@Override
	public Map<String, Object> getContentList(Page page) {
		Map<String, Object> data = new HashMap<>();
		try {
			List<Map<String, Object>> list = (List<Map<String, Object>>) dao.findForList("PmmCommentMapper.getContentlistPage", page);
			data.put("data", list);
			data.put("page", page);
		} catch (Exception e) {
			logger.error("获取方案列表异常", e);
		}
		return data;
	}

	@Override
	public Map<String, Object> getCommentById(PageData pd) {
		Map<String, Object> data = new HashMap<>();
		try {
			SolutionCommentVO sc = (SolutionCommentVO) dao.findForObject("PmmCommentMapper.getCommentById", pd);
			if(sc != null && sc.getUserId() != null){
				Map<String, String> m = new HashMap<>();
				m.put("ids", sc.getUserId());
				String getAllUserListUrl = SystemConfig.getPropertiesString("web.allUser_list_url");
				String json = HttpClientUtil.doPost(getAllUserListUrl, m);
				if(!"".equals(json)){
					List<Map> parseArray = JSON.parseArray(json, Map.class);
					for (Map map : parseArray) {
						
							if(map.get("id").equals(sc.getUserId())){
								sc.setUsername((String)map.get("nickname"));
							}
						
						
					}
				}
			}
			data.put("data", sc);
		} catch (Exception e) {
			logger.error("获取评论详情异常", e);
		}
		return data;
	}

	@Override
	public void deleteComment(PageData pd) {
		try {
			dao.delete("PmmCommentMapper.deleteComment", pd);
			dao.delete("PmmCommentMapper.deleteCommentLike", pd);
		} catch (Exception e) {
			logger.error("删除评论异常", e);
		}
		
	}

}
