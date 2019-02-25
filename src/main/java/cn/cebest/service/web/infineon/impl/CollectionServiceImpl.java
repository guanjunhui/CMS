package cn.cebest.service.web.infineon.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.infineon.Collection;
import cn.cebest.service.web.infineon.CollectionService;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;

@Service("collectionService")
public class CollectionServiceImpl implements CollectionService {

	private static Logger logger = Logger.getLogger(CollectionServiceImpl.class);
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 根据用户id查询收藏列表
	 */
	@Override
	public JsonResult listByUserId(Page page) {
		JsonResult jr = new JsonResult();
		try {
			PageData pd = page.getPd();
			String resourceType = (String) pd.get("resourceType");
			if(resourceType != null && !"".equals(resourceType)){
				Map<String, Object> data = new HashMap<>();
				List<Map<String, Object>> collections = new ArrayList<>();
				//根据资源类型查询  1解决方案   2合作伙伴信息   3视频  4文档
				if("1".equals(resourceType)){
					collections = (List<Map<String, Object>>) dao.findForList("CollectionMapper.getSolutionlistPage", page);
				}else if("2".equals(resourceType)){
					collections = (List<Map<String, Object>>) dao.findForList("CollectionMapper.getPartnerlistPage", page);
				}else if("3".equals(resourceType)){
					collections = (List<Map<String, Object>>) dao.findForList("CollectionMapper.getVideolistPage", page);
					
				}
				data.put("data", collections);
				data.put("page", page);
				jr.setCode(200);
				jr.setData(data);
			}
			
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("查询收藏列表出现异常", e);
		}
		return jr;
	}

	@Override
	public JsonResult addCollection(Collection collection) {
		JsonResult jr = new JsonResult();
		try {
			dao.save("CollectionMapper.saveCollection", collection);
			jr.setData(collection);
			jr.setCode(200);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("添加收藏异常", e);
		}
		return jr;
	}

	@Override
	public JsonResult getColl(Collection collection) {
		JsonResult jr = new JsonResult();
		try {
			String id = (String) dao.findForObject("CollectionMapper.getColl", collection);
			jr.setData(id);
			jr.setCode(200);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("查询收藏信息异常", e);
		}
		return jr;
	}

	@Override
	public JsonResult delColl(String id) {
		JsonResult jr = new JsonResult();
		try {
			dao.delete("CollectionMapper.delColl", id);
			jr.setCode(200);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("取消收藏异常", e);
		}
		return jr;
	}

}
