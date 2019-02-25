package cn.cebest.service.system.contentresolve.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.content.Content;
import cn.cebest.service.system.contentresolve.ContentResolveService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.PageData;

@Service
public class ContentResolveImplService implements ContentResolveService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public List<ContentInfoBo> findTopContentList() throws Exception {
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		//产品数据转换
		List<PageData> productData=(List<PageData>) dao.findForList("ProductMapper.findTopContentList", null);
		List<ContentInfoBo> productDataList=this.convertProduct(productData);
		if(CollectionUtils.isNotEmpty(productDataList)){
			resultList.addAll(productDataList);
		}
		//咨询数据转换
		List<PageData> messageData=(List<PageData>) dao.findForList("MessageMapper.findTopContentList", null);
		List<ContentInfoBo> messageDataList=this.convertMessage(messageData);
		if(CollectionUtils.isNotEmpty(messageDataList)){
			resultList.addAll(messageDataList);
		}
		//下载数据转换
		List<PageData> fileData=(List<PageData>) dao.findForList("fileResourceMapper.findTopContentList", null);
		List<ContentInfoBo> fileDataList=this.convertFile(fileData);
		if(CollectionUtils.isNotEmpty(fileDataList)){
			resultList.addAll(fileDataList);
		}
		//内容数据转换
		List<PageData> contentData=(List<PageData>) dao.findForList("ContentMapper.findTopContentList", null);
		List<ContentInfoBo> contentDataList=this.convertContent(contentData);
		if(CollectionUtils.isNotEmpty(contentDataList)){
			resultList.addAll(contentDataList);
		}
		//招聘数据转换
		List<PageData> employData=(List<PageData>) dao.findForList("EmployMapper.findTopContentList", null);
		List<ContentInfoBo> employDataList=this.convertContent(employData);
		if(CollectionUtils.isNotEmpty(employDataList)){
			resultList.addAll(employDataList);
		}
		return resultList;
	}
	
	@Override
	public List<ContentInfoBo> findRecommendContentList(Map<String, Object> map) throws Exception {
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		//获取所有推荐内容
		List<Content> findContentAllByAll = (List<Content>) dao.findForList("ContentMapper.selectAllByAll", map);
		List<ContentInfoBo> contentList=this.convertRecommodContent(findContentAllByAll);
		if(CollectionUtils.isNotEmpty(contentList)){
			resultList.addAll(contentList);
		}
		//获取所有推荐咨询
//		List<PageData> findMessageAllByAll = (List<PageData>) dao.findForList("MessageMapper.selectAllByAll", map);
//		List<ContentInfoBo> messageList = this.convertRecommodMessage(findMessageAllByAll);
//		if(CollectionUtils.isNotEmpty(messageList)){
//			resultList.addAll(messageList);
//		}
		return resultList;
	}

	
	public List<ContentInfoBo> convertProduct(List<PageData> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>(dataList.size());
		for(PageData data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getString("ID"));
			bo.setName(data.getString("NAME"));
//			String date=DateUtil.dateFormat(data.getString("CREATED_TIME"),
//					DateUtil.TIME, DateUtil.MONTHDAY);
			bo.setCreatTime(data.getString("CREATED_TIME"));
			bo.setTopTime(data.getString("TOP_TIME"));
			bo.setColumId(data.getString("COLUMNID"));
			bo.setType(Const.COLUM_TYPE_3);
			resultList.add(bo);
		}
		return resultList;
	}
	
	public List<ContentInfoBo> convertMessage(List<PageData> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		for(PageData data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getString("ID"));
			bo.setName(data.getString("message_title"));
			String date=DateUtil.dateFormat((Date)data.get("created_time"), DateUtil.MONTHDAY);
			bo.setCreatTime(date);
			bo.setColumId(data.getString("COLUMNID"));
			bo.setType(Const.COLUM_TYPE_2);
			resultList.add(bo);
		}
		return resultList;
	}

	public List<ContentInfoBo> convertFile(List<PageData> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		for(PageData data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getString("FILEID"));
			bo.setName(data.getString("TITLE"));
			String date=DateUtil.dateFormat(data.getString("CREATED_TIME"),
					DateUtil.TIME, DateUtil.MONTHDAY);
			bo.setCreatTime(date);
			bo.setColumId(data.getString("COLUMNID"));
			bo.setType(Const.COLUM_TYPE_5);
			resultList.add(bo);
		}
		return resultList;
	}

	public List<ContentInfoBo> convertContent(List<PageData> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		for(PageData data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getString("ID"));
			bo.setName(data.getString("CONTENT_TITLE"));
			String date=DateUtil.dateFormat(data.getString("CREATED_TIME"),
					DateUtil.TIME, DateUtil.MONTHDAY);
			bo.setCreatTime(date);
			bo.setColumId(data.getString("COLUMCONFIG_ID"));
			bo.setType(Const.COLUM_TYPE_1);
			resultList.add(bo);
		}
		return resultList;
	}

	public List<ContentInfoBo> convertEmploy(List<PageData> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		for(PageData data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getString("ID"));
			String date=DateUtil.dateFormat((Date)data.get("RELEASE_TIME"), DateUtil.MONTHDAY);
			bo.setName(data.getString("RECRUIT_TITLE"));
			bo.setCreatTime(date);
			bo.setColumId(data.getString("COLUMCONFIG_ID"));
			bo.setType(Const.COLUM_TYPE_4);
			resultList.add(bo);
		}
		return resultList;
	}

	public List<ContentInfoBo> convertRecommodContent(List<Content> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		for(Content data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getId());
			bo.setName(data.getContentTitle());
			String date=DateUtil.dateFormat(data.getRecommend_time(),
					DateUtil.TIME, DateUtil.MONTHDAY);
			bo.setCreatTime(date);
			bo.setType(Const.COLUM_TYPE_1);
			bo.setDescription(data.getContentSummary());
			Set<ColumConfig> columConfigList = data.getColumConfigList();
			for (ColumConfig columConfig : columConfigList) {
				bo.setColumId(columConfig.getId());
				break;
			}
			bo.setImageUrl(data.getSurface_imageurl());
			bo.setTourl(data.getContentWbUrl());
			if(CollectionUtils.isNotEmpty(data.getImageList()))
			bo.setImageList(data.getImageList());
			resultList.add(bo);
		}
		return resultList;
	}

	public List<ContentInfoBo> convertRecommodMessage(List<PageData> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		for(PageData data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getString("ID"));
			bo.setName(data.getString("TITLE"));
			String date=DateUtil.dateFormat(data.getString("RECOMMEND_TIME"),
					DateUtil.TIME, DateUtil.MONTHDAY);
			bo.setCreatTime(date);
			bo.setType(Const.COLUM_TYPE_2);
			bo.setDescription(data.getString("DESCRIPTION"));
			bo.setColumId(data.getString("COLUMNID"));
			resultList.add(bo);
		}
		return resultList;
	}

}
