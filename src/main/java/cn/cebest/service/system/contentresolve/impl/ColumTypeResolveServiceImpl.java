package cn.cebest.service.system.contentresolve.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.TypeInfoBo;
import cn.cebest.entity.system.content.ContentType;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.entity.system.product.Product_Type;
import cn.cebest.service.system.content.contentType.ContentTypeService;
import cn.cebest.service.system.contentresolve.ColumTypeResolveService;
import cn.cebest.service.system.download.FileTypeService;
import cn.cebest.service.system.newMessage.MyMessageTypeService;
import cn.cebest.service.system.product.ProductTypeService;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;

/**
 * 分类转换
 * 
 * */
@Service
public class ColumTypeResolveServiceImpl implements ColumTypeResolveService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Autowired
	private MyMessageTypeService messageTypeService;
	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private FileTypeService fileTypeService;
	@Autowired
	private ContentTypeService contentTypeService;

	/**
	 *根据栏目获取其所有分类 
	 */
	public List<TypeInfoBo> findTypeByColumID(String columId,String columType) throws Exception{
		return this.setType(columId,columType);
	}

	//查询栏目下所有的分类
	public List<TypeInfoBo> setType(String columId,String columType) throws Exception{
		List<TypeInfoBo> resultTypeList=new ArrayList<TypeInfoBo>();
		switch(columType){
			case Const.TEMPLATE_TYPE_1://内容模板
				List<ContentType> contentTypeList = this.contentTypeService.findContentTypeByColumnIds(columId);
				this.convertContentTypeList(contentTypeList, resultTypeList, columId);
				break;
			case Const.TEMPLATE_TYPE_2://资讯模板
				List<NewMessageType> messageTypeList=this.messageTypeService.findMessage_TypeByColumnIds(columId);
				this.convertMesageTypeList(messageTypeList, resultTypeList,columId);
				break;
			case Const.TEMPLATE_TYPE_3://产品模板
				List<Product_Type> productTypeList=this.productTypeService.
					findProduct_TypeByColumnIds(columId);
				this.convertProductTypeList(productTypeList, resultTypeList,columId);
				break;
			case Const.TEMPLATE_TYPE_5://下载模板
				List<FileType> fileTypeList=this.fileTypeService.findFileTypeByColumnIds(columId);
				this.convertFileTypeList(fileTypeList, resultTypeList,columId);
				break;
			default:break;
		}
		Collections.sort(resultTypeList, new Comparator<TypeInfoBo>(){
			@Override
			public int compare(TypeInfoBo o1, TypeInfoBo o2) {
				int A=o1.getSort()==null?Integer.MAX_VALUE:o1.getSort().intValue();
				int B=o2.getSort()==null?Integer.MAX_VALUE:o2.getSort().intValue();
				return A-B;
			}
		});
		return resultTypeList;
	}
	
	/**
	 *根据栏目获取其所有分类 (分页)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<List<TypeInfoBo>> findTypelistPageByColumID(Page<List<TypeInfoBo>> page,String columType) throws Exception{
		List<TypeInfoBo> resultList=new ArrayList<TypeInfoBo>();
		PageData pd=page.getPd();
		String ID=pd.getString("ID");
		pd.put("columId", ID);
		switch(columType){
			case Const.TEMPLATE_TYPE_1://内容模板
				if(ID!=null&&ID!="") {
					List<ContentType> contentTypeList=(List<ContentType>) dao.findForList("ContentTypeMapper.findTypelistPageByColumnIds", page);
					List<ContentType> contentTypeChildList=(List<ContentType>) dao.findForList("ContentTypeMapper.findTypeChildByColumnIds", page);
					List<ContentType> listTop = new ArrayList<>();
					List<String> ids = new ArrayList<>();
					for (ContentType contentType : contentTypeChildList) {
						ids.add(contentType.getId());
					}
					if (!CollectionUtils.isEmpty(contentTypeChildList)) {
						for (int i = 0; i < contentTypeChildList.size(); i++) {
							if (!ids.contains(contentTypeChildList.get(i).getpId())) {
								this.appendChildContentType(contentTypeChildList.get(i), contentTypeChildList);
								listTop.add(contentTypeChildList.get(i));
							}
						}
						for (int i = 0; i < contentTypeList.size(); i++) {
							for (int j = 0; j < listTop.size(); j++) {
								if (contentTypeList.get(i) != null && listTop.get(j) != null && contentTypeList.get(i).getId().equals(listTop.get(j).getpId())) {
									if (CollectionUtils.isEmpty(contentTypeList.get(i).getChildList())) {
										contentTypeList.get(i).setChildList(new ArrayList<ContentType>());
									}
									contentTypeList.get(i).getChildList().add(listTop.get(j));
								}
							}
						}
					}
					this.convertContentTypeList(contentTypeList, resultList, ID);
				}
				break;
			case Const.TEMPLATE_TYPE_2://资讯模板
				if(ID!=null&&ID!="") {
					List<NewMessageType> messageTypeList=(List<NewMessageType>) dao.findForList("MessageTypeMapper.findTypelistPageByColumnIds", page);
					List<NewMessageType> messageTypeChildList=(List<NewMessageType>) dao.findForList("MessageTypeMapper.findTypeChildByColumnIds", page);
					List<NewMessageType> listTop=new ArrayList<>();
					List<String> ids=new ArrayList<>();
					for (NewMessageType messageType : messageTypeChildList) {
						ids.add(messageType.getId());
					}
					if (!CollectionUtils.isEmpty(messageTypeChildList)) {
						for (int i=0;i<messageTypeChildList.size();i++) {
							if(!ids.contains(messageTypeChildList.get(i).getPid())){
								this.appendChildMessageType(messageTypeChildList.get(i), messageTypeChildList);
								listTop.add(messageTypeChildList.get(i));
							}
						}
						for (int i = 0; i < messageTypeList.size(); i++) {
							for(int j=0;j < listTop.size();j++){
								if (messageTypeList.get(i) != null && listTop.get(j) != null && messageTypeList.get(i).getId().equals(listTop.get(j).getPid())) {
									if (CollectionUtils.isEmpty(messageTypeList.get(i).getChildList())) {
										messageTypeList.get(i).setChildList(new ArrayList<NewMessageType>());
									}
									messageTypeList.get(i).getChildList().add(listTop.get(j));
								}
							}
						}
					}
					this.convertMesageTypeList(messageTypeList, resultList,ID);
				}
				break;
			case Const.TEMPLATE_TYPE_3://产品模板
				if(ID!=null&&ID!="") {
					List<Product_Type> productTypeList=(List<Product_Type>) dao.findForList("productTypeMapper.findTypelistPageByColumnIds", page);
					List<Product_Type> productTypeChildList=(List<Product_Type>) dao.findForList("productTypeMapper.findTypeChildByColumnIds", page);
					
					List<Product_Type> listTop = new ArrayList<>();
					List<String> ids = new ArrayList<>();
					for (Product_Type productType : productTypeChildList) {
						ids.add(productType.getId());
					}
					if (!CollectionUtils.isEmpty(productTypeChildList)) {
						for (int i = 0; i < productTypeChildList.size(); i++) {
							if (!ids.contains(productTypeChildList.get(i).getPid())) {
								this.appendChildProductType(productTypeChildList.get(i), productTypeChildList);
								listTop.add(productTypeChildList.get(i));
							}
						}
						for (int i = 0; i < productTypeList.size(); i++) {
							for(int j=0;j < listTop.size();j++){
								if (productTypeList.get(i) != null && listTop.get(j) != null && productTypeList.get(i).getId().equals(listTop.get(j).getPid())) {
									if (CollectionUtils.isEmpty(productTypeList.get(i).getChildList())) {
										productTypeList.get(i).setChildList(new ArrayList<Product_Type>());
									}
									productTypeList.get(i).getChildList().add(listTop.get(j));
								}
							}
						}
					}
					
					this.convertProductTypeList(productTypeList, resultList,ID);
				}
				break;
			case Const.TEMPLATE_TYPE_5://下载模板
				if(ID!=null&&ID!="") {
					List<FileType> fileTypeList=(List<FileType>) dao.findForList("fileTypeMapper.findTypelistPageByColumnIds", page);
					List<FileType> fileTypeChildList=(List<FileType>) dao.findForList("fileTypeMapper.findTypeChildByColumnIds", page);
					List<FileType> listTop = new ArrayList<>();
					List<String> ids = new ArrayList<>();
					for (FileType fileType : fileTypeChildList) {
						ids.add(fileType.getDownload_id());
					}
					if (!CollectionUtils.isEmpty(fileTypeChildList)) {
						for (int i = 0; i < fileTypeChildList.size(); i++) {
							if (!ids.contains(fileTypeChildList.get(i).getPid())) {
								this.appendChildFileType(fileTypeChildList.get(i), fileTypeChildList);
								listTop.add(fileTypeChildList.get(i));
							}
						}
						for (int i = 0; i < fileTypeList.size(); i++) {
							for(int j=0;j < listTop.size();j++){
								if (fileTypeList.get(i) != null && listTop.get(j) != null && fileTypeList.get(i).getDownload_id().equals(listTop.get(j).getPid())) {
									if (CollectionUtils.isEmpty(fileTypeList.get(i).getChildList())) {
										fileTypeList.get(i).setChildList(new ArrayList<FileType>());
									}
									fileTypeList.get(i).getChildList().add(listTop.get(j));
								}
							}
						}
					}
					this.convertFileTypeList(fileTypeList, resultList,ID);
				}
				break;
			default:break;
		}
		Collections.sort(resultList, new Comparator<TypeInfoBo>(){
			@Override
			public int compare(TypeInfoBo o1, TypeInfoBo o2) {
				int A=o1.getSort()==null?Integer.MAX_VALUE:o1.getSort().intValue();
				int B=o2.getSort()==null?Integer.MAX_VALUE:o2.getSort().intValue();
				return A-B;
			}
		});
		page.setData(resultList);
		return page;
	}
	
	
	//转换内容分类
	public void convertContentTypeList(List<ContentType> typeList,
			List<TypeInfoBo> resultList,String columId){
		if(CollectionUtils.isEmpty(typeList)){
			return;
		}
		Integer sum = 0;
		for(ContentType contentType:typeList){
			TypeInfoBo typeInfoBo=new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(contentType.getId());
			typeInfoBo.setName(contentType.getTypeName());
			typeInfoBo.setTemPlatePath(contentType.getTemplate()!=null
					?contentType.getTemplate().getTemFilepath():StringUtils.EMPTY);
			typeInfoBo.setSort(contentType.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_1);
			typeInfoBo.setColumId(columId);
			typeInfoBo.setColumName(contentType.getColumConfigList().get(sum).getColumName());
			typeInfoBo.setStatus(contentType.getTypeStatus());
			if(CollectionUtils.isNotEmpty(contentType.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertContentTypeList(contentType.getChildList(), typeInfoBo.getChildList(),columId);
			}
		}
	}
	
	//转换咨询分类
	public void convertMesageTypeList(List<NewMessageType> typeList,
			List<TypeInfoBo> resultList,String columId){
		if(CollectionUtils.isEmpty(typeList)) return;
		Integer sum = 0;
		for(NewMessageType newMessageType:typeList){
			TypeInfoBo typeInfoBo=new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(newMessageType.getId());
			typeInfoBo.setName(newMessageType.getType_name());
			typeInfoBo.setTemPlatePath(newMessageType.getTemplate()!=null
					?newMessageType.getTemplate().getTemFilepath():StringUtils.EMPTY);
			typeInfoBo.setSort(newMessageType.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_2);
			typeInfoBo.setColumId(columId);
			if(!CollectionUtils.isEmpty(newMessageType.getColumConfigList())){
				typeInfoBo.setColumName(newMessageType.getColumConfigList().get(sum).getColumName());
			}
			typeInfoBo.setStatus(newMessageType.getType_status());
			if(CollectionUtils.isNotEmpty(newMessageType.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertMesageTypeList(newMessageType.getChildList(), typeInfoBo.getChildList(),columId);
			}
		}
	}
		
	//转换产品分类
	public void convertProductTypeList(List<Product_Type> typeList,
			List<TypeInfoBo> resultList,String columId){
		if(CollectionUtils.isEmpty(typeList)) return;
		Integer sum = 0;
		for(Product_Type type:typeList){
			TypeInfoBo typeInfoBo=new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(type.getId());
			typeInfoBo.setName(type.getType_name());
			typeInfoBo.setTemPlatePath(type.getTemplate()!=null
					?type.getTemplate().getTemFilepath():StringUtils.EMPTY);
			typeInfoBo.setSort(type.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_3);
			typeInfoBo.setColumId(columId);
			if(type.getColumConfigList().size()>0){
				typeInfoBo.setColumName(type.getColumConfigList().get(sum).getColumName());
			}
			typeInfoBo.setStatus(type.getType_status());
			if(CollectionUtils.isNotEmpty(type.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertProductTypeList(type.getChildList(), typeInfoBo.getChildList(),columId);
			}
		}
	}

	//转换下载分类
	public void convertFileTypeList(List<FileType> typeList,
			List<TypeInfoBo> resultList,String columId){
		if(CollectionUtils.isEmpty(typeList)) return;
		Integer sum = 0;
		for(FileType type:typeList){
			TypeInfoBo typeInfoBo=new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(type.getDownload_id());
			typeInfoBo.setName(type.getDownload_name());
			typeInfoBo.setTemPlatePath(type.getTemplate()!=null
					?type.getTemplate().getTemFilepath():StringUtils.EMPTY);
			typeInfoBo.setSort(type.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_5);
			typeInfoBo.setColumId(columId);
			typeInfoBo.setColumName(type.getColumConfigList().get(sum).getColumName());
			typeInfoBo.setStatus(type.getStatus());
			if(CollectionUtils.isNotEmpty(type.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertFileTypeList(type.getChildList(), typeInfoBo.getChildList(),columId);
			}
		}
	}
	
	private void appendChildContentType(ContentType root, List<ContentType> allList) {
		for (int i = 0; i < allList.size(); i++) {
			ContentType pt = allList.get(i);
			if (pt != null && pt.getpId().equals(root.getId())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<ContentType>());
				}
				root.getChildList().add(pt);
				this.appendChildContentType(pt, allList);
			}
		}
	}
	
	private void appendChildMessageType(NewMessageType root,List<NewMessageType> allList){
		for (int i = 0; i < allList.size(); i++) {
			NewMessageType mt = allList.get(i);
			if (mt != null && mt.getPid().equals(root.getId())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<NewMessageType>());
				}
				root.getChildList().add(mt);
				this.appendChildMessageType(mt, allList);
			}
		}
	}
	
	private void appendChildProductType(Product_Type root, List<Product_Type> allList) {
		for (int i = 0; i < allList.size(); i++) {
			Product_Type pt = allList.get(i);
			if (pt != null && pt.getPid().equals(root.getId())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<Product_Type>());
				}
				root.getChildList().add(pt);
				this.appendChildProductType(pt, allList);
			}
		}
	}
	
	private void appendChildFileType(FileType root, List<FileType> allList) {
		for (int i = 0; i < allList.size(); i++) {
			FileType pt = allList.get(i);
			if (pt != null && pt.getPid().equals(root.getDownload_id())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<FileType>());
				}
				root.getChildList().add(pt);
				this.appendChildFileType(pt, allList);
			}
		}
	}
}
