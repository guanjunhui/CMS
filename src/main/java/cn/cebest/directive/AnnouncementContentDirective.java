package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.content.Content;
import cn.cebest.service.system.content.content.ContentService;
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

@Component("announcement_content")
public class AnnouncementContentDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	private static final String COULMID="columId";
	@Autowired
	private ContentService contentService;

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		// 获取参数
		// 获取当前站点
		String columId=ParamsUtils.getString(COULMID, params);

		Page page=new Page();
		PageData pd=new PageData();
		pd.put("colum_id", columId);
		page.setPd(pd);
		try {
			List<Content> contentlistPage = contentService.contentFrontlistPage(page);
			// 设置输出变量
			env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(contentlistPage));
			if (body != null) {
				// 输出变量
				body.render(env.getOut());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BeansWrapper getBeansWrapper() {
		BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
		return beansWrapper;
	}
}
