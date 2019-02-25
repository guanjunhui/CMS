package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.web.WebSite;
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

@Component("friendly_link")
public class FriendlyLinkDirective implements TemplateDirectiveModel{
	protected Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private ColumconfigService columconfigService;
	/**
	 * 参数名称
	 */
	private static final String GROUPID = "groupId";

	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
        //获取当前站点
		WebSite site = FrontUtils.getSite(env);
		String siteId = site.getSiteId();
		//获取参数
		String ColumGroupId=ParamsUtils.getString(GROUPID, params);
		PageData pd = new PageData();
		pd.put("SITEID", siteId);
		pd.put("COLUMGROUP_ID", ColumGroupId);
		
		List<PageData> list=null;
		try {
			list = columconfigService.findAllTree(pd);
		} catch (Exception e) {
			logger.error("find the colum ocurred error!",e);
			e.printStackTrace();
		}
		//设置输出变量
        env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(list));
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
