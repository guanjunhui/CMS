package cn.cebest.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.web.Banner;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.service.web.banner.BannerManagerService;
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

@Component("parent_colum")
public class ParentColumDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private ColumconfigService columconfigService; 
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		 
		// 获取参数
		String columId=ParamsUtils.getString("columId",params);
		logger.info("columId**********************************:"+columId);
		try {
			Object obj = this.columconfigService.getParentName(columId);
			logger.info("result**********************************:"+obj);
			// 设置输出变量
			env.setVariable("parentCloum", getBeansWrapper().wrap(obj));
			if (body != null) {
				// 输出变量
				body.render(env.getOut());
			}
		} catch (Exception e) {
			logger.error("find the seoContent with columID["+columId+"] occured error!", e);
		}
	}

	public static BeansWrapper getBeansWrapper() {
		BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
		return beansWrapper;
	}
}
