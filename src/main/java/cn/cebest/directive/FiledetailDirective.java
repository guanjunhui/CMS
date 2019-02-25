package cn.cebest.directive;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.cebest.entity.system.download.FileResources;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.util.Const;
import cn.cebest.util.FrontUtils;
import cn.cebest.util.ParamsUtils;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("file_detail")
public class FiledetailDirective implements TemplateDirectiveModel {
	@Resource(name = "fileResourceService")
	private FileResourceService fileResourceService;

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {

		// 获取当前站点
		WebSite site = FrontUtils.getSite(env);
		String siteId = site.getSiteId();
		// 获取参数
		String contentId = ParamsUtils.getString("contentId", params);
		FileResources fiellist = null;

		try {
			fiellist = fileResourceService.findFileResourceAndDownloadFileById(contentId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(fiellist));

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
