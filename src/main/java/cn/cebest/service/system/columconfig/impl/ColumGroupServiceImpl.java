package cn.cebest.service.system.columconfig.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.system.columconfig.ColumGroupService;
import cn.cebest.util.PageData;

@Service("columGroupService")
public class ColumGroupServiceImpl implements ColumGroupService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Override
	public void save(PageData pd) throws Exception {
		dao.save("ColumGroupMapper.save", pd);
	}

	@Override
	public void update(PageData pd) throws Exception {
		dao.update("ColumGroupMapper.edit", pd);
	}

	@Override
	public void delete(PageData pd) throws Exception {
		dao.delete("ColumGroupMapper.delete", pd);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("ColumGroupMapper.findById", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findAll(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("ColumGroupMapper.findAll", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> gropulistPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("ColumGroupMapper.gropulistPage", page);
	}

	@Override
	public String findByName(PageData pd) throws Exception {
		return (String) dao.findForObject("ColumGroupMapper.findByName", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findPermAllList(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("ColumGroupMapper.findColumAllList", pd);
	}
	
	
	@Override
	public void deleteBeforCopy(String siteID) throws Exception {
		dao.delete("ColumGroupMapper.deleteBeforCopy", siteID);
	}

}
