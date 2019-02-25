package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.txt.TxtService;
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
 * 查询详细信息
 */
@Component("content_detail")
public class ContentDetailDirective implements TemplateDirectiveModel {

	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private ContentService contentService;
	@Autowired
	private TxtService txtService;
	/**
	 * 参数名称
	 */
	private static final String CONTENTID = "contentId";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		// 获取参数
		// 获取当前站点
		WebSite site = FrontUtils.getSite(env);
		String siteId = site.getSiteId();
		String contentId = ParamsUtils.getString(CONTENTID, params);
		String currentId = ParamsUtils.getString("currentId", params);
		String contentTypeId = ParamsUtils.getString("contentTypeId", params);
		// ServletContext servletContext =
		// ParamsUtils.getContext(env).getWebApplicationContext().getServletContext();
		// List<Content> contentList = (List<Content>)
		// servletContext.getAttribute("contentList");
		PageData pd = new PageData();
		//有什么用?
 		//pd.put("ID", contentId);
		pd.put("SITEID", siteId);
		pd.put("currentId", currentId);
		pd.put("contentTypeId", contentTypeId);
		List<Content> contentList = null;
		Content content = null;
		Content frontContent = null;
		Content afterContent = null;
		try {
			contentList = contentService.findContent(pd);
			if (contentList != null && contentList.size() > 0) {
				for (int i = 0; i < contentList.size(); i++) {
					if (contentId.equals(contentList.get(i).getId())) {
						content = contentList.get(i);
						if (i - 1 >= 0) {
							frontContent = contentList.get(i - 1);
						}
						if (i + 1 < contentList.size()) {
							afterContent = contentList.get(i + 1);
						}
						break;
					}
				}
			}
			if (content != null && content.getId() != null) {
				contentService.updatePv(content.getId());
			}
		} catch (Exception e) {
			logger.error("find the colum ocurred error!", e);
		}
		// 设置输出变量
		env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(content));
		env.setVariable("frontContent", getBeansWrapper().wrap(frontContent));
		env.setVariable("afterContent", getBeansWrapper().wrap(afterContent));
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
