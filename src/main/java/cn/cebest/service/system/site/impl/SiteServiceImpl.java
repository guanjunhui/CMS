package cn.cebest.service.system.site.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.SiteMain;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.site.SiteService;
import cn.cebest.util.PageData;

@Service("siteService")
public class SiteServiceImpl implements SiteService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Override
	public List<PageData> sitelistPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("SiteMapper.sitelistPage", page);
	}

	@Override
	public void delSite(PageData pd) throws Exception {
		dao.update("SiteMapper.updateSite", pd);
	}

	@Override
	public void delAllSite(String[] arraySITE_IDS) throws Exception {
		dao.update("SiteMapper.updateAllSite", arraySITE_IDS);
	}

	@Override
	public void saveSite(PageData pd) throws Exception {
		dao.save("SiteMapper.saveSite", pd);
	}

	@Override
	public PageData findSiteById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("SiteMapper.findSiteById", pd);
	}

	@Override
	public void editSite(PageData pd) throws Exception {
		dao.update("SiteMapper.edit", pd);
	}

	@Override
	public void changeStatus(PageData pd) throws Exception {
		dao.update("SiteMapper.changeStatus", pd);
	}
	
	@Override
	public void updateSiteIndexStatus(PageData pd) throws Exception {
		dao.update("SiteMapper.updateSiteIndexStatus", pd);
	}
	
	@Override
	public List<WebSite> findAllSiteByStatus(String status) throws Exception {
		return (List<WebSite>) dao.findForList("SiteMapper.findAllSiteByStatus", status);
	}

	@Override
	public List<SiteMain> findAllSiteMainInfo() throws Exception {
		return (List<SiteMain>) dao.findForList("SiteMapper.findAllSiteMainInfo", null);
	}

	@Override
	public WebSite findSitePoById(String siteId) throws Exception {
		return (WebSite)dao.findForObject("SiteMapper.findSitePoById", siteId);
	}

}
