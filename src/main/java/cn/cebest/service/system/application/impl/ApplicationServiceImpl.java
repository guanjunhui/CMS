package cn.cebest.service.system.application.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.application.Application;
import cn.cebest.service.system.application.ApplicationService;
import cn.cebest.util.PageData;

@Service("ApplicationService")
public class ApplicationServiceImpl implements ApplicationService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	//所有申请列表展示
	@SuppressWarnings("unchecked")
	@Override
	public List<Application> contentlistPage(Page page) throws Exception {///////////////////////////////////
		return (List<Application>) dao.findForList("ApplicationMapper.selectApplicationslistPage", page);
	}
	//根据id查找具体的申请列表
	@Override
	public Application findContentById(PageData pd) throws Exception {
		return (Application) dao.findForObject("ApplicationMapper.findApplicationById", pd);
	}
	//更新申请列表
	@Override
	public void editContent(Application content) throws Exception {
		dao.update("ApplicationMapper.editApplication", content);
	}
	//保存申请列表
	@Override
	public void saveContent(Application content) throws Exception {
		dao.save("ApplicationMapper.saveApplication", content);
	}
	/**
	 *导出全部资讯列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> listAllApplication(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ApplicationMapper.listAllApplication", pd);
	}
}
