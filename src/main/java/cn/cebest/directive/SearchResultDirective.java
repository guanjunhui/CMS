package cn.cebest.directive;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.solr.SearchResult;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.solr.SolrService;
import cn.cebest.util.Const;
import cn.cebest.util.FrontUtils;
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

@Component("searchResult_list")
public class SearchResultDirective implements TemplateDirectiveModel {
	@Resource(name = "solrService")
	private SolrService solrService;

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		// 获取当前站点
		WebSite site = FrontUtils.getSite(env);
		String siteId = site.getSiteId();
		// 获取参数
		String keyword = ParamsUtils.getString("keyword", params);
		// 设置分页参数
		int currentPage = FrontUtils.getPageNo(params);
		int showCount = FrontUtils.getPageSize(params);
		Page page = new Page(currentPage, showCount);
		SearchResult list = null;
		try {
			//list = (List<SearchResult>) solrService.findSolrList(newKeyWord,page);
			list = solrService.findSolrList(keyword,page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(list));
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
