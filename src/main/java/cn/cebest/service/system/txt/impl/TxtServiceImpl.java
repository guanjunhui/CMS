package cn.cebest.service.system.txt.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.service.system.txt.TxtService;
import cn.cebest.util.PageData;

@Service
public class TxtServiceImpl implements TxtService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public void save(PageData pd) throws Exception {
		dao.save("TxtMapper.save", pd);
	}

	@Override
	public void update(PageData pd) throws Exception {
		dao.update("TxtMapper.update", pd);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("TxtMapper.findById", pd);
	}

	@Override
	public void del(String id) throws Exception {
		dao.delete("TxtMapper.del", id);
	}

	@Override
	public void delAll(String[] ids) throws Exception {
		dao.delete("TxtMapper.delAll", ids);
	}


}
