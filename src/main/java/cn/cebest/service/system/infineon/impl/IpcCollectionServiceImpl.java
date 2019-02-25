package cn.cebest.service.system.infineon.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.IpcCollectionService;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年9月27日
 * @company 中企高呈
 */
@Service
public class IpcCollectionServiceImpl implements IpcCollectionService{
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Override
	public Map<String, Object> listPlanByUserId(Page page)throws Exception {
		PageData pd = page.getPd();
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> collections = new ArrayList<>();
		collections = (List<Map<String, Object>>) dao.findForList("IpcCollectionMapper.getPlanlistPage", page);
		data.put("data", collections);
		data.put("page", page);
		return data;
		
	}
	@Override
	public Map<String, Object> topicListByUserId(Page page) throws Exception {
		PageData pd = page.getPd();
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> collections = new ArrayList<>();
		collections = (List<Map<String, Object>>) dao.findForList("IpcCollectionMapper.getTopiclistPage", page);
		data.put("data", collections);
		data.put("page", page);
		return data;
	}
	@Override
	public void collection(PageData pd) throws Exception {
		Object obj = this.dao.findForObject("IpcCollectionMapper.findBySourceIdAndSourcetypeAndUserId", pd);
		if(!obj.toString().equals("0")){
			throw new Exception("您已收藏");
		}
		this.dao.save("IpcCollectionMapper.saveCollection", pd);
		
	}
	@Override
	public void cancel(PageData pd) throws Exception {
		//删除收藏表的数据
		this.dao.delete("IpcCollectionMapper.cancelCollection",pd);
	}

}
