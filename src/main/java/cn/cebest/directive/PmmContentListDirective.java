package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.content.content.ContentService;
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

@Component("pmm_content_list")
public class PmmContentListDirective implements TemplateDirectiveModel {

	protected Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private ContentService contentService;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		//得到当前站点
		WebSite site = FrontUtils.getSite(env);
		//获取当前站点id
		String siteId = site.getSiteId();
		String flag=ParamsUtils.getString("flag", params);
		//获取参数
		String currentId = ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		String sort = ParamsUtils.getString("sort", params);
		String recommend = ParamsUtils.getString("recommend", params);
		String hot = ParamsUtils.getString("hot", params);
		//设置分页参数
		int currentPage = FrontUtils.getPageNo(params);
		int showCount = FrontUtils.getPageSize(params);
		Page page=new Page(currentPage,showCount);
		
		String contentType=ParamsUtils.getString("contentType", params);
		
		PageData pd = new PageData();
		pd.put("SITEID", siteId);
		pd.put("colum_id", currentId);
		pd.put("sort", sort);
		pd.put("hot", hot);
		pd.put("TYPEID", contentType);
		//pd.put("CONTENT_STATUS", 1);
		if(flag!=null && sort == null){
			pd.put("NAME", "NAME");
		}else if(flag==null && sort == null){
			pd.put("CREATED_TIME", "CREATED_TIME");
		}
		page.setPd(pd);
		List<Content> list = null;
		try {
 			list = contentService.selectlistPageByColumIDpmm(page);
		} catch (Exception e) {
			logger.error("find the colum ocurred error!",e);
		}
		//设置输出变量
        env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(list));
        env.setVariable(Const.OUT_PAGE, getBeansWrapper().wrap(page));
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
