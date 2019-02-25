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
import cn.cebest.service.system.infineon.ReviewService;
import cn.cebest.util.HttpClientUtil;
import cn.cebest.util.Logger;
import cn.cebest.util.SystemConfig;

@Service("ReviewService")
public class ReviewServiceImpl implements ReviewService {

	private static Logger logger = Logger.getLogger(ReviewServiceImpl.class);
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Override
	public Map<String, Object> getReviewList(Page page) {
		Map<String, Object> data = new HashMap<>();
		try {
			
			List<SolutionCommentVO> commentList = (List<SolutionCommentVO>) dao.findForList("PmmCommentMapper.getReviewlistPage", page);
			StringBuffer str = new StringBuffer();
			for (SolutionCommentVO solutionCommentVO : commentList) {
				if(!"".equals(solutionCommentVO.getUserId()) && null != solutionCommentVO.getUserId()){
					
					str.append(solutionCommentVO.getUserId()+",");
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

}
