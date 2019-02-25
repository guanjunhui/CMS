package cn.cebest.service.system.WarrantyClaim.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.system.WarrantyClaim.ContactUsService;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月10日
 * @company 中企高呈
 */
@Service
public class ContactUsServiceImpl implements ContactUsService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public void saveWarrantyClaim(PageData pd) throws Exception{
		this.dao.save("ContactUs1Mapper.save", pd);
	}

	@Override
	public List<PageData> ListPage(Page page)  throws Exception{
		return (List<PageData>) dao.findForList("ContactUs1Mapper.listPage", page);
	}

	@Override
	public void delContactUs(PageData pd) throws Exception {
		dao.delete("ContactUs1Mapper.delContactUs", pd);
	}

	@Override
	public void delsContactUs(String[] ids)  throws Exception {
		dao.delete("ContactUs1Mapper.delsContactUs", ids);
	}

	@Override
	public Object detailById(String id) throws Exception {
		return dao.findForObject("ContactUs1Mapper.detailById", id);
	}
}
