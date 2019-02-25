package cn.cebest.directive;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.bo.TypeInfoBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.content.ContentType;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.entity.system.product.Product_Type;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.content.contentType.ContentTypeService;
import cn.cebest.service.system.download.FileTypeService;
import cn.cebest.service.system.newMessage.MyMessageTypeService;
import cn.cebest.service.system.product.ProductTypeService;
import cn.cebest.util.Const;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;
import cn.cebest.util.ParamsUtils;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 根据栏目ID获取对应的分类(以及子栏目的分类)
 */
@Component("colum_typeList")
public class GetTypeByColumId implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	private static final String COULMID="columId";//栏目ID

	@Autowired
	private ColumconfigService columconfigService;
	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private FileTypeService fileTypeService;
	@Autowired
	private MyMessageTypeService messageTypeService;
	@Autowired
	private ContentTypeService contentTypeService;
	/**
	 * 参数名称
	 */
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String columId=ParamsUtils.getString(COULMID, params);
		ColumConfig columCurrent=null;
		try {
			columCurrent = this.columconfigService.findColumconfigPoById(new PageData("ID", columId));
			//查询每个栏目对应的分类
			this.setType(columCurrent);
			List<TypeInfoBo> typeList=columCurrent.getTypeList();
			this.sort(typeList);
		    env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(typeList));
		} catch (Exception e) {
			logger.error("get the type by the columId occured error!",e);
		}
        if (body != null) {
            body.render(env.getOut());
        }  
	}
	
	public void sort(List<TypeInfoBo> typeList){
		if(CollectionUtils.isEmpty(typeList)){
			return;
		}
		Collections.sort(typeList, new Comparator<TypeInfoBo>(){
			@Override
			public int compare(TypeInfoBo o1, TypeInfoBo o2) {
				int A=o1.getSort()==null?Integer.MAX_VALUE:o1.getSort().intValue();
				int B=o2.getSort()==null?Integer.MAX_VALUE:o2.getSort().intValue();
				return A-B;
			}
		});
		for(TypeInfoBo type:typeList){
			this.sort(type.getChildList());
		}
	}
	
	//查询栏目下所有的分类
	public void setType(ColumConfig colum) throws Exception{
		List<TypeInfoBo> resultTypeList=new ArrayList<TypeInfoBo>();
		switch(colum.getColumType()){
			case Const.TEMPLATE_TYPE_1://内容模板
				List<ContentType> contentTypeList = this.contentTypeService.findContentTypeByColumnIds(colum.getId());
				this.convertContentTypeList(contentTypeList, resultTypeList, colum.getId());
				break;
			case Const.TEMPLATE_TYPE_2://资讯模板
				List<NewMessageType> messageTypeList=this.messageTypeService.findMessage_TypeByColumnIds(colum.getId());
				this.convertMesageTypeList(messageTypeList, resultTypeList,colum.getId());
				break;
			case Const.TEMPLATE_TYPE_3://产品模板
				//查询栏目下的分类
				List<Product_Type> productTypeList=this.productTypeService.
					findProduct_TypeByColumnIds(colum.getId());
				//根据栏目查询分类对应的产品数量
				List<PageData> countList=this.productTypeService.
						selectCountByTypeAndColumID(colum.getId());
				
				this.convertProductTypeList(productTypeList, resultTypeList,colum.getId(),countList);
				
				break;
			case Const.TEMPLATE_TYPE_5://下载模板
				List<FileType> fileTypeList=this.fileTypeService.findFileTypeByColumnIds(colum.getId());
				this.convertFileTypeList(fileTypeList, resultTypeList,colum.getId());
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
		colum.setTypeList(resultTypeList);
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
			typeInfoBo.setImageUrl(contentType.getImgurl());
			typeInfoBo.setSummary(contentType.getTypeSummary());
			if(CollectionUtils.isNotEmpty(contentType.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertContentTypeList(contentType.getChildList(), typeInfoBo.getChildList(),columId);
			}
		}
	}
	
	//转换咨询分类
	public void convertMesageTypeList(List<NewMessageType> typeList,
			List<TypeInfoBo> resultList,String columId){
		if(CollectionUtils.isEmpty(typeList)){
			return;
		}
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
			typeInfoBo.setSummary(newMessageType.getType_summary());
			typeInfoBo.setType_txt(newMessageType.getType_detail());
			typeInfoBo.setImageUrl(newMessageType.getImgurl());
			if(CollectionUtils.isNotEmpty(newMessageType.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertMesageTypeList(newMessageType.getChildList(), typeInfoBo.getChildList(),columId);
			}
		}
	}
	
	//转换产品分类
	public void convertProductTypeList(List<Product_Type> typeList,
			List<TypeInfoBo> resultList,String columId,List<PageData> countList){
		if(CollectionUtils.isEmpty(typeList)){
			return;
		}
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
			typeInfoBo.setImageUrl(type.getImgurl());
			
			typeInfoBo.setPid(type.getPid());
			if(CollectionUtils.isNotEmpty(countList)){
				for(PageData countMap:countList){
					if(type.getId().equals(countMap.getString("typeId"))){
						typeInfoBo.setCount((Long)countMap.get("count"));
					}
				}
			}
			if(CollectionUtils.isNotEmpty(type.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertProductTypeList(type.getChildList(), typeInfoBo.getChildList(),columId,countList);
			}
		}
	}

	//转换下载分类
	public void convertFileTypeList(List<FileType> typeList,
			List<TypeInfoBo> resultList,String columId){
		if(CollectionUtils.isEmpty(typeList)){
			return;
		}
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
			typeInfoBo.setSummary(type.getSummary());
			typeInfoBo.setType_txt(type.getTXT());
			if(CollectionUtils.isNotEmpty(type.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertFileTypeList(type.getChildList(), typeInfoBo.getChildList(),columId);
			}
		}
	}

    public static BeansWrapper getBeansWrapper(){
        BeansWrapper beansWrapper =
                         new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
        return beansWrapper;
    }
}
