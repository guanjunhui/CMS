package cn.cebest.service.system.clonesite.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.system.content.ContentType;
import cn.cebest.entity.system.content.Content_Type_Colum;
import cn.cebest.entity.system.content.Contenttype_Column;
import cn.cebest.service.system.clonesite.CloneSiteService;


@Service("cloneSiteService")
public class CloneSiteServiceImpl implements CloneSiteService{
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 根据网站ID查询所有内容分类数据
	 */
	@Override
	public List<ContentType> findContentTypeBySiteId(String siteID) throws Exception{		
		return (List<ContentType>) dao.findForList("CloneContentMapper.findTypeInfoBySiteId", siteID);		
	}
	/**
	 * 根据内容分类查询所有栏目与分类关系数据
	 */
	@Override
	public List<Contenttype_Column> findContentTypeColumnByTypeIds(String[] typeIDS) throws Exception{
		return (List<Contenttype_Column>)dao.findForList("CloneContentMapper.findTypeColumnByTypeIDS", typeIDS);
	}
	/**
	 * 根据网站ID查询所有内容数据
	 */
	@Override
	public List<Content> findContentBySiteId(String siteID) throws Exception{
		return (List<Content>)dao.findForList("CloneContentMapper.findContentListBySiteId", siteID);
	}
	/**
	 * 根据内容ID查询所有内容，栏目，分类的关系数据
	 */
	@Override
	public List<Content_Type_Colum> findCTCByContentIds(String[] contentIDS) throws Exception{
		return (List<Content_Type_Colum>)dao.findForList("CloneContentMapper.findContentTypeColumnByIDS", contentIDS);
	}
	
	@Override
	public void insertBatchCType(List<ContentType> list) throws Exception{
		dao.batchSave("CloneContentMapper.insertBatchCType", list);
	}
	@Override
	public void insertBatchTypeColumn(List<Contenttype_Column> list) throws Exception{
		dao.batchSave("CloneContentMapper.insertBatchTC", list);
	}
	@Override
	public void insertBatchContent(List<Content> list) throws Exception{
		dao.batchSave("CloneContentMapper.insertBatchContent", list);
	}
	@Override
	public void insertBatchCTC(List<Content_Type_Colum> list) throws Exception{
		dao.batchSave("CloneContentMapper.insertBatchCTC", list);
	}
	
	@Override
	public void deleteBeforCopyC(String siteID)throws Exception{
		dao.delete("CloneContentMapper.deleteBeforCopyC", siteID);
	}
	@Override
	public void deleteBeforCopyCC(String[] contentIDS)throws Exception{
		dao.delete("CloneContentMapper.deleteBeforCopyCC", contentIDS);
	}
	@Override
	public void deleteBeforCopyCT(String siteID)throws Exception{
		dao.delete("CloneContentMapper.deleteBeforCopyCT", siteID);
	}
	@Override
	public void deleteBeforCopyCTC(String[] contentTypeIDS)throws Exception{
		dao.delete("CloneContentMapper.deleteBeforCopyCTC", contentTypeIDS);
	}


}
