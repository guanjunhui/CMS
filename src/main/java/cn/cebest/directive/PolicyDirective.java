package cn.cebest.directive;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.download.FileTypeService;
import cn.cebest.util.Const;
import cn.cebest.util.FrontUtils;
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
 * 关于我们标签
 */
@Component("index_policy")
public class PolicyDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private FileTypeService fileTypeService;
	@Autowired
	private ColumconfigService columconfigService;
	@Autowired
	private ContentService contentService;
	
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String columId=ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		//获取当前站点
		WebSite site = FrontUtils.getSite(env);
		//当前栏目
		ColumConfig columCurrent=null;
		//所有栏目
		List<ColumConfig> columAllList=null;
		//推荐内容
		List<ContentInfoBo> contentInfoList=new ArrayList<ContentInfoBo>();
		//所有的下载分类ID
		List<String> typeIdList = null;
		try {
			columCurrent = this.columconfigService.findColumconfigPoById(new PageData("ID", columId));
			columAllList=this.columconfigService.findAllList(new PageData("siteid", site.getId()));
			//查询栏目下所有的子栏目(树状结构)
			this.appendChild(columCurrent, columAllList);
			//所有的下载分类ID
			typeIdList=new ArrayList<String>();
			//查询每个栏目对应的分类
			this.setType(columCurrent,typeIdList);
			//查询顶级栏目下的推荐内容数据
			if(this.selectContent(columCurrent) != null){
				contentInfoList.addAll(this.selectContent(columCurrent));
			}
			//查询子级栏目下的推荐内容数据
			this.getContentByColum(columCurrent.getSubConfigList(), typeIdList,contentInfoList);
			
		} catch (Exception e) {
			logger.error("find the colum or search colum's type into database ocurred error!",e);
		}
		try {
			//通过下载分类ID集合获取下载分类下的推荐数据 
			if(typeIdList != null && typeIdList.size()>0){
				List<ContentInfoBo> fileList=fileTypeService.findAllGroomData(typeIdList);
				if(CollectionUtils.isNotEmpty(fileList)){
					contentInfoList.addAll(fileList);
				}
			}
			contentInfoList.removeAll(Collections.singleton(null));
		} catch (Exception e) {
			logger.error("find the content by typeId into database ocurred error!",e);
		}
		
		Collections.sort(contentInfoList, new Comparator<ContentInfoBo>(){
			@Override
			public int compare(ContentInfoBo o1, ContentInfoBo o2) {
				String A=o1.getCreatTime();
				String B=o2.getCreatTime();
				return A.compareTo(B)*Const.INT_UNSIGINED_1;
			}
		});

		
		//设置输出变量
        env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(contentInfoList));
        
		if (body != null) {
	        body.render(env.getOut());
	    }  
	}
	
	public void getContentByColum(List<ColumConfig> columList,List<String> typeIdList,
			List<ContentInfoBo> contentInfoList) throws Exception{
		if(CollectionUtils.isEmpty(columList)) return;
		for(ColumConfig colum:columList){
			contentInfoList.addAll(this.selectContent(colum));//查询栏目下的推荐数据
			this.setType(colum,typeIdList);//查询下载的所有分类
			this.getContentByColum(colum.getSubConfigList(), typeIdList,contentInfoList);
		}
	}
	
	//查询内容数据栏目下的推荐内容
	public List<ContentInfoBo> selectContent(ColumConfig colum) throws Exception{
		List<ContentInfoBo> contentInfoList=new ArrayList<ContentInfoBo>();
		if(Const.TEMPLATE_TYPE_1.equals(colum.getColumType())){
			contentInfoList = contentService.findContentInfoBoById(colum.getId());
		}
		return contentInfoList;
	}
	
	
    public static BeansWrapper getBeansWrapper(){
        BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
        return beansWrapper;
    }
    
  	
  	//查找所有的分类
  	public void convertFileTypeList(List<FileType> typeList,List<String> typeIdList){
  		if(CollectionUtils.isEmpty(typeList)){
  			return;
  		}
  		for(FileType type:typeList){
  			typeIdList.add(type.getDownload_id());
  			if(CollectionUtils.isNotEmpty(type.getChildList())){
  				this.convertFileTypeList(type.getChildList(), typeIdList);
  			}
  		}
  	}
  	
	//查找子栏目
	private void appendChild(ColumConfig config,List<ColumConfig> allList){
		for(int i=0;i<allList.size();i++){
			ColumConfig po=allList.get(i);
			if(po.getParentid().equals(config.getId())){
				if(CollectionUtils.isEmpty(config.getSubConfigList())){
					config.setSubConfigList(new ArrayList<ColumConfig>());
				}
				config.getSubConfigList().add(po);
				allList.remove(i);
				i--;
				this.appendChild(po, allList);
			}
		}
	}

	//查询下载栏目下所有的分类
	public void setType(ColumConfig colum,List<String> resultTypeList) throws Exception{
		if(Const.TEMPLATE_TYPE_5.equals(colum.getColumType())){
			List<FileType> fileTypeList=this.fileTypeService.findFileTypeByColumnIds(colum.getId());
			this.convertFileTypeList(fileTypeList, resultTypeList);
		}
	}
}
