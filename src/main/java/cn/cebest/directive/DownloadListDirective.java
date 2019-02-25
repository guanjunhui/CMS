package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.download.FileResources;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.download.FileResourceService;
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
@Component("download_list")
public class DownloadListDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 参数名称
	 */
	private static final String CURRENTID = "currentId";
	private static final String ISCHANNEL = "isChannel";
	private static final String COLUMNID = "columnId";
	@Resource(name = "fileResourceService")
	private FileResourceService fileResourceService;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		// 获取当前站点
		WebSite site = FrontUtils.getSite(env);
		String siteId = site.getSiteId();
		// 获取参数
		String currentId = ParamsUtils.getString(CURRENTID, params);
		String isChannel = ParamsUtils.getString(ISCHANNEL, params);
		String columnId = ParamsUtils.getString(COLUMNID, params);
		List<FileResources> fiellist = null;
		// 设置分页参数
		int currentPage = FrontUtils.getPageNo(params);
		int showCount = FrontUtils.getPageSize(params);
		Page page = new Page(currentPage, showCount);

		PageData pd = page.getPd();
		pd.put("columnId", columnId);
		pd.put("id", currentId);
		if (isChannel != null && isChannel.equals("0")) {
			pd.put("TYPE", "TYPE");
		} else {
			pd.put("COLUMNID", columnId);
		}
		try {
			fiellist = fileResourceService.findFileFileResourceAndDownloadFileByColumnId(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(fiellist));
		env.setVariable(Const.OUT_PAGE, getBeansWrapper().wrap(page));

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
