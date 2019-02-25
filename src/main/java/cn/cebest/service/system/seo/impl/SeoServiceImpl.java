package cn.cebest.service.system.seo.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.system.Seo;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.util.PageData;
@Service("seoService")
public class SeoServiceImpl implements SeoService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 为内容分类写的方法
	 * */
	@Override
	public void saveSeoContent(Map map) throws Exception {
		dao.save("SeoMapper.insetSeoContent", map);

	}
	
	/**
	 * 为内容分类写的方法
	 * */
	@Override
	public Map querySeoForContent(Map map) throws Exception {
		return (Map) dao.findForObject("SeoMapper.querySeoForContent", map);
	}
	
	/**
	 * 为内容分类写的方法
	 * */
	@Override
	public Integer updateSeoContent(Map map) throws Exception {
		return (Integer) dao.update("SeoMapper.updateSeoContent", map);
	}
	
	@Override
	public void insetSeo(PageData pd) throws Exception {
		dao.save("SeoMapper.insetSeo", pd);

	}

	@Override
	public void deleteSeo(PageData pd) throws Exception {
		dao.delete("SeoMapper.deleteSeo", pd);

	}

	@Override
	public void updateSeo(PageData pd) throws Exception {
		dao.update("SeoMapper.updateSeo", pd);

	}

	@Override
	public PageData querySeoForObject(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("SeoMapper.querySeoForObject", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> querySeoForList(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>) dao.findForList("SeoMapper.querySeoForList", pd);
	}
	
	@Override
	public Seo selectSeoForObject(String masterId) throws Exception {
		// TODO Auto-generated method stub
		return (Seo) dao.findForObject("SeoMapper.selectSeoForObject", masterId);
	}
}
