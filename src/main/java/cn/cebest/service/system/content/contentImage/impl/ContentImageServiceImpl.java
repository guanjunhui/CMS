package cn.cebest.service.system.content.contentImage.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.system.content.contentImage.ContentImageService;
import cn.cebest.util.PageData;

@Service("contentImageService")
public class ContentImageServiceImpl implements ContentImageService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Override
	public List<PageData> contentImageList(Page page) throws Exception {
		return (List<PageData>)dao.findForList("ContentImageMapper.contentImageList", page);
	}

	@Override
	public void save(PageData pd) throws Exception {
		dao.save("ContentImageMapper.save", pd);
		dao.save("ContentImageMapper.saveContent_image", pd);
	}

	@Override
	public void delete(PageData pd) throws Exception {
		dao.delete("ContentImageMapper.delete", pd);
		dao.delete("ContentImageMapper.deleteContent_image", pd);
	}

}
