package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.web.Banner;
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

@Component("bannerDeatil_list")
public class BannerDetailListDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private BannerManagerService bannerManagerService;
	/**
	 * 每页显示记录数
	 */
	public static String SHOWCOUNT="showCount";
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		 
		// 获取参数
		String columId=ParamsUtils.getString(Const.PARAM_CURRENTID,params);
		int showCount = FrontUtils.getPageSize(params);
		PageData pd = new PageData();
		pd.put("columId", columId);
		pd.put("showCount", showCount);
		List<Banner> list=null;
		try {
			list= bannerManagerService.selectBannerListDetailByColumnId(pd);
			
		} catch (Exception e) {
			logger.error("find the banner with columID["+columId+"] occured error!", e);
		}
		// 设置输出变量
		env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(list));
		if (body != null) {
			// 输出变量
			body.render(env.getOut());
		}
	}

	public static BeansWrapper getBeansWrapper() {
		BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
		return beansWrapper;
	}
}
