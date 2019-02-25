package cn.cebest.directive;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.web.WebSite;
import cn.cebest.exception.MainTainException;
import cn.cebest.service.system.columconfig.ColumconfigService;
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
 * 栏目列表标签
 */
@Component("colum_list")
public class ColumListDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 参数名称
	 */
	private static final String GROUPID = "groupId";

	@Autowired
	private ColumconfigService columconfigService;
	
//	@Autowired
//	private ContentResolveService contentResolveService;

	
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		//获取当前站点
		WebSite site = FrontUtils.getSite(env);
		String siteId = site.getSiteId();
		//获取参数
		String groupId=ParamsUtils.getString(GROUPID, params);
		PageData pd = new PageData();
		pd.put("siteid", siteId);
		pd.put("COLUM_DISPLAY", Const.YES);
		if(StringUtils.isNotEmpty(groupId)){
			pd.put("COLUMGROUP_ID", groupId);
		}
		List<ColumConfig> list=null;
		try {
			list = columconfigService.findTopAndChildList(pd);
		} catch (Exception e) {
			logger.error("find the colum ocurred error!",e);
		}
		if(CollectionUtils.isEmpty(list)){
			throw new MainTainException("the site["+site.getSiteDomian()+":"+site.getSiteLanguage()+"] does not find the colum in database!");
		}
		//查找栏目下对应的置顶信息
		/*List<ContentInfoBo> contenList=null;
		try {
			contenList=this.contentResolveService.findTopContentList();
		} catch (Exception e) {
			logger.error("find the top content ocurred error!",e);
		}
		for(ColumConfig configTop:list){
			configTop.setContentList(new ArrayList<ContentInfoBo>());
			for(int i=Const.INT_0;i<contenList.size();i++){
				ContentInfoBo content=contenList.get(i);
				if(configTop.getId().equals(content.getColumId())){
					configTop.getContentList().add(content);
					
				}
			}
			//设置子栏目的置顶内容
			if(CollectionUtils.isNotEmpty(configTop.getSubConfigList())){
				this.searchContent(configTop,configTop.getSubConfigList(), contenList);
			}
		}*/
		//设置输出变量
        env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(list));
        if (body != null) {
        	//输出变量
            body.render(env.getOut());
        }  
	}
	
	/**
	 * 查询置顶内容
	 */
	public void searchContent(ColumConfig configTop,List<ColumConfig> list,List<ContentInfoBo> contenList){
		if(CollectionUtils.isEmpty(contenList)){
			return;
		}
		for(ColumConfig columConfig:list){
			for(int i=Const.INT_0;i<contenList.size();i++){
				ContentInfoBo content=contenList.get(i);
				if(columConfig.getId().equals(content.getColumId())){
					if(!configTop.hashContainContent(content.getId())){
						configTop.getContentList().add(content);
						
					}
				}
			}
			//设置子栏目的置顶内容
			if(CollectionUtils.isNotEmpty(columConfig.getSubConfigList())){
				this.searchContent(configTop,columConfig.getSubConfigList(), contenList);
			}
		}
	}
	
    public static BeansWrapper getBeansWrapper(){
        BeansWrapper beansWrapper =
                         new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
        return beansWrapper;
    }
}
