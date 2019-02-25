package cn.cebest.service.system.content.collection.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.content.Content;
import cn.cebest.service.system.content.collection.ContentCollectionService;
import cn.cebest.util.PageData;

@Service("contentCollectionService")
public class ContentCollectionServiceImpl implements ContentCollectionService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public void contentCollection(PageData pd) throws Exception {
		dao.save("ContentCollectionMapper.contentCollection", pd);
	}

	@Override
	public List<Content> contentCollectionlistpage(Page page) throws Exception {
		return (List<Content>) dao.findForList("ContentCollectionMapper.contentCollectionlistpage", page);
	}
	
	
}
