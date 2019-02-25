package cn.cebest.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.contentresolve.ContentResolveService;
import cn.cebest.util.Const;
import cn.cebest.util.FrontUtils;
import cn.cebest.util.Logger;
import cn.cebest.util.ParamsUtils;
import cn.cebest.util.SystemConfig;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
/**
 * 推荐标签
 * */
@Component("recommend_list")
public class RecommendedConsultingDirective implements TemplateDirectiveModel{

	private static final Logger LOGGER = Logger.getLogger(SystemConfig.class);
	
	@Autowired
	private  ContentResolveService contentResolveService;
	
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		//获取当前站点
		WebSite site = FrontUtils.getSite(env);
		String siteId = site.getSiteId();
		String columId=ParamsUtils.getString("columId", params);
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("SITEID", siteId);
		paramMap.put("RECOMMEND", Const.YES);
		paramMap.put("COLUM_ID",columId);
		
		List<ContentInfoBo> contentList=null;
		try {
			contentList=this.contentResolveService.findRecommendContentList(paramMap);
		} catch (Exception e) {
			LOGGER.error("find recommend info error!",e);
		}
		//设置输出变量
        env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(contentList));
        if (body != null) {
        	//输出变量
            body.render(env.getOut());
        } 
		
	}
	
    public static BeansWrapper getBeansWrapper(){
        BeansWrapper beansWrapper =
                         new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
        return beansWrapper;
    }
}
