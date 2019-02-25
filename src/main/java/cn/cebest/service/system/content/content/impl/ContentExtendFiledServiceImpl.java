package cn.cebest.service.system.content.content.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.system.content.content.ContentExtendFiledService;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.PageData;

@Service("contentExtendFiledServiceImpl")
public class ContentExtendFiledServiceImpl implements ContentExtendFiledService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Override
	public List<PageData> getData(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ExtendFiledMapper.selectExtendFiled", pd);
	}

	@Override
	public void delete(String[] id) throws Exception {
		
		dao.delete("ExtendFiledMapper.deleteExtendFiled",id);
		
	}

	@Override
	public void save(PageData pd) throws Exception {
		dao.save("ExtendFiledMapper.insertExtendFiled", pd);
	}

	@Override
	public void update(PageData pd) throws Exception {
		dao.update("ExtendFiledMapper.updateExtendFiled", pd);
		
	}

	@Override
	public Map<String, Object> findById(PageData pd) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("filed", dao.findForObject("ExtendFiledMapper.selectExtendFiled", pd));
		return map;
	}

	@Override
	public Integer findCount(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (Integer) dao.findForObject("ExtendFiledMapper.selectExtendFiledCount", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<List<PageData>> getData(Page<List<PageData>> page) throws Exception {
		List<PageData> data=(List<PageData>) dao.findForList("ExtendFiledMapper.selectExtendFiledlistPage", page);
		page.setData(data);
		return page;
	}

	@Override
	public void updateSort(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map findIdByName(PageData pd) throws Exception {
		
		return (Map) dao.findForObject("ExtendFiledMapper.findIdByName", pd);
	}
	
}
