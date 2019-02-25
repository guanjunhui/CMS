package cn.cebest.directive;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.cebest.util.ParamsUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 翻页包含标签
 */
@Component("page_clude")
public class PaginationDirective implements TemplateDirectiveModel {

	/**
	 * 分页模板路径
	 */
	public static final String TPL_STYLE_PAGE = "/WEB-INF/htmlFront/include/page_";
	/**
	 * 模板后缀
	 */
	public static final String TPL_SUFFIX = ".html";
	/**
	 * UTF-8编码
	 */
	public static final String UTF8 = "UTF-8";
	
	public static final String PAGESTYLE="pageStyle";

	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String sysPage=ParamsUtils.getString(PAGESTYLE, params);
		env.include(TPL_STYLE_PAGE+sysPage+TPL_SUFFIX, UTF8, true);
	}
}
