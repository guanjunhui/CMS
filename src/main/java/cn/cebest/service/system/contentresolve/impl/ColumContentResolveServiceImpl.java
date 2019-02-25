package cn.cebest.service.system.contentresolve.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.bo.TypeInfoBo;
import cn.cebest.entity.system.Recruit;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.system.content.ContentType;
import cn.cebest.entity.system.download.FileResources;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.entity.system.product.Product;
import cn.cebest.entity.system.product.Product_Type;
import cn.cebest.entity.web.Banner;
import cn.cebest.service.system.contentresolve.ColumContentResolveService;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;

@Service
public class ColumContentResolveServiceImpl implements ColumContentResolveService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@SuppressWarnings("unchecked")
	@Override
	public Page<List<ContentInfoBo>> findContentlistPageByColumID(Page<List<ContentInfoBo>> page,String columType) throws Exception {
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		PageData pd=page.getPd();
		String columId=pd.getString("ID");
		String keyword=pd.getString("keyword");
		List<String> ids = null;
		List<ContentInfoBo> contentDataList=null;
		pd.put("columId", columId);
		pd.put("keyword", keyword);
		switch(columType){
			case Const.COLUM_TYPE_1://内容栏目
				ids=(List<String>) dao.findForList("ContentMapper.selectIdsByColumID", pd);
				if(ids!=null&&ids.size()>0) {
					pd.put("idArry", ids.toArray(new String[ids.size()]));
					List<Content> contentList=(List<Content>) dao.findForList("ContentMapper.selectlistPageInfoByIdArry", page);
					contentDataList=this.convertContent(contentList);
					if(CollectionUtils.isNotEmpty(contentDataList)){
						resultList.addAll(contentDataList);
					}
				}
				break;
			case Const.COLUM_TYPE_2://资讯栏目
				ids=(List<String>) dao.findForList("MessageMapper.selectIdsByColumID", pd);
				if(ids!=null&&ids.size()>0) {
					pd.put("idArry", ids.toArray(new String[ids.size()]));
					List<NewMessage> messageList=(List<NewMessage>) dao.findForList("MessageMapper.selectlistPageInfoByIdArry", page);
					contentDataList=this.convertMessage(messageList);
					if(CollectionUtils.isNotEmpty(contentDataList)){
						resultList.addAll(contentDataList);
					}
				}
				break;
			case Const.COLUM_TYPE_3://产品栏目
				ids=(List<String>) dao.findForList("ProductMapper.selectIdsByColumID", pd);
				if(ids!=null&&ids.size()>0) {
					pd.put("idArry", ids.toArray(new String[ids.size()]));
					List<Product> productList=(List<Product>) dao.findForList("ProductMapper.selectlistPageInfoByIdArry", page);
					contentDataList=this.convertProduct(productList);
					if(CollectionUtils.isNotEmpty(contentDataList)){
						resultList.addAll(contentDataList);
					}
				}
				break;
			case Const.COLUM_TYPE_4://招聘栏目
				List<Recruit> employList=(List<Recruit>) dao.findForList("EmployMapper.selectlistPageInfoByIdArry", page);
				contentDataList=this.convertEmploy(employList);
				if(CollectionUtils.isNotEmpty(contentDataList)){
					resultList.addAll(contentDataList);
				}
				break;
			case Const.COLUM_TYPE_5://下载栏目
				ids=(List<String>) dao.findForList("fileResourceMapper.selectIdsByColumID", pd);
				if(ids!=null&&ids.size()>0) {
					pd.put("idArry", ids.toArray(new String[ids.size()]));
					List<FileResources> fileList=(List<FileResources>) dao.findForList("fileResourceMapper.selectlistPageInfoByIdArry", page);
					contentDataList=this.convertFile(fileList);
					if(CollectionUtils.isNotEmpty(contentDataList)){
						resultList.addAll(contentDataList);
					}
				}
				break;
		    default:break;
	    }
		page.setData(resultList);
		return page;
	}
	
	public List<ContentInfoBo> convertContent(List<Content> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>(dataList.size());
		for(Content data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getId());
			bo.setName(data.getContentTitle());
			bo.setCreatTime(data.getCreatedTime());
			bo.setUpdateTime(data.getUpdateTime());
			bo.setColumList(data.getColumConfigList());
			bo.setStatus(data.getContentStatus());
			bo.setTop(data.getTop());
			bo.setRecommend(data.getRecommend());
			bo.setHot(data.getHot());
			bo.setSort(data.getSort());
			//分类转换
			List<TypeInfoBo> typeList=new ArrayList<>();
			if(CollectionUtils.isNotEmpty(data.getContentTypeList())){
				for(ContentType type:data.getContentTypeList()){
					TypeInfoBo contentTypeBo=new TypeInfoBo();
					contentTypeBo.setId(type.getId());
					contentTypeBo.setName(type.getTypeName());
					typeList.add(contentTypeBo);
				}
			}
			bo.setTypeList(typeList);
			bo.setType(Const.COLUM_TYPE_1);
			resultList.add(bo);
		}
		return resultList;
	}

	public List<ContentInfoBo> convertMessage(List<NewMessage> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		for(NewMessage data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getId());
			bo.setName(data.getMessage_title());
			bo.setCreatTime(data.getCreated_time());
			bo.setUpdateTime(data.getUpdate_time());
			bo.setColumList(data.getColumConfigList());
			bo.setTop(data.getTop());
			bo.setRecommend(data.getRecommend());
			bo.setHot(data.getHot());
			bo.setStatus(data.getStatus());
			bo.setSort(data.getSort());
			//分类转换
			List<TypeInfoBo> typeList=new ArrayList<>();
			if(CollectionUtils.isNotEmpty(data.getMessageTypeList())){
				for(NewMessageType type:data.getMessageTypeList()){
					TypeInfoBo contentTypeBo=new TypeInfoBo();
					contentTypeBo.setId(type.getId());
					contentTypeBo.setName(type.getType_name());
					typeList.add(contentTypeBo);
				}
			}
			bo.setTypeList(typeList);
			bo.setType(Const.COLUM_TYPE_2);
			resultList.add(bo);
		}
		return resultList;
	}

	public List<ContentInfoBo> convertProduct(List<Product> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>(dataList.size());
		for(Product data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getId());
			bo.setName(data.getName());
			bo.setCreatTime(data.getCreated_Time());
			bo.setUpdateTime(data.getUpdate_Time());
			bo.setColumList(data.getColumConfigList());
			bo.setStatus(data.getProduct_Status());
			bo.setTop(data.getTop());
			bo.setRecommend(data.getRecommend());
			bo.setHot(data.getHot());
			bo.setSort(data.getSort());
			//分类转换
			List<TypeInfoBo> typeList=new ArrayList<>();
			if(CollectionUtils.isNotEmpty(data.getProductTypeList())){
				for(Product_Type type:data.getProductTypeList()){
					TypeInfoBo contentTypeBo=new TypeInfoBo();
					contentTypeBo.setId(type.getId());
					contentTypeBo.setName(type.getType_name());
					typeList.add(contentTypeBo);
				}
			}
			bo.setTypeList(typeList);
			bo.setType(Const.COLUM_TYPE_3);
			resultList.add(bo);
		}
		return resultList;
	}

	public List<ContentInfoBo> convertEmploy(List<Recruit> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		for(Recruit data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getId());
			bo.setName(data.getRecruitTitle());
			bo.setCreatTime(data.getCreated_Time());
			bo.setUpdateTime(data.getUpdateTime());
			bo.setStatus(data.getStatus());
			bo.setTop(data.getIftop());
			bo.setRecommend(data.getIfrecommend());
			bo.setHot(data.getIfhot());
			bo.setSort(data.getSort());
			bo.setType(Const.COLUM_TYPE_4);
			bo.setColumList(data.getColumConfigList());
			resultList.add(bo);
		}
		return resultList;
	}

	public List<ContentInfoBo> convertFile(List<FileResources> dataList){
		if(CollectionUtils.isEmpty(dataList)){
			return null;
		}
		List<ContentInfoBo> resultList=new ArrayList<ContentInfoBo>();
		for(FileResources data:dataList){
			ContentInfoBo bo=new ContentInfoBo();
			bo.setId(data.getFileid());
			bo.setName(data.getTitle());
			bo.setCreatTime(data.getCreated_time());
			bo.setUpdateTime(data.getUpdate_time());
			bo.setColumList(data.getColumConfigList());
			bo.setStatus(data.getStatus());
			bo.setTop(data.getTop());
			bo.setRecommend(data.getRecommend());
			bo.setHot(data.getHot());
			bo.setSort(data.getSort());
			bo.setType(Const.COLUM_TYPE_5);
			bo.setDownload_count(data.getDownload_count());
			//分类转换
			List<TypeInfoBo> typeList=new ArrayList<>();
			if(CollectionUtils.isNotEmpty(data.getFileTypeList())){
				for(FileType type:data.getFileTypeList()){
					TypeInfoBo contentTypeBo=new TypeInfoBo();
					contentTypeBo.setId(type.getDownload_id());
					contentTypeBo.setName(type.getDownload_name());
					typeList.add(contentTypeBo);
				}
			}
			bo.setTypeList(typeList);
			resultList.add(bo);
		}
		return resultList;
	}

	@Override
	public Page<List<Banner>> findBannerlistPageByColumID(Page<List<Banner>> page, String columType) throws Exception {
		List<Banner> resultList=new ArrayList<Banner>();
		PageData pd=page.getPd();
		String columId=pd.getString("ID");
		List<String> ids = null;
		List<Banner> bannerDataList=null;
		pd.put("columId", columId);
		ids=(List<String>) dao.findForList("BannerMapper.selectIdsByColumID", pd);
		if(ids!=null&&ids.size()>0) {
			pd.put("idArry", ids.toArray(new String[ids.size()]));
			List<Banner> bannerList=(List<Banner>) dao.findForList("BannerMapper.selectlistPageInfoByIdArry", page);
			//bannerDataList=this.convertContent(bannerList);
			if(CollectionUtils.isNotEmpty(bannerList)){
				resultList.addAll(bannerList);
			}
		}
		page.setData(resultList);
		return page;
	}
}
