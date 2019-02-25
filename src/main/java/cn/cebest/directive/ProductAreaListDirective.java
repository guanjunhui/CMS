package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.product.Product;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.product.ProductService;
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
@Component("product_area_list")
public class ProductAreaListDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 参数名称
	 */
	private static final String COLUMID = "columId";
	private static final String TYPEID = "typeId";
	private static final String PROUCTID = "prouctId";
	
	private static final String OUT_LIST2 = "out_list2";
	private static final String OUT_LIST_OTHER = "out_list_other";
	@Resource(name = "productService")
	private ProductService productService;

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		// 获取当前站点
		WebSite site = FrontUtils.getSite(env);
		String siteId = site.getSiteId();
		// 获取参数
		String columId = ParamsUtils.getString(COLUMID, params);
		String typeId = ParamsUtils.getString(TYPEID, params);
		String prouctId = ParamsUtils.getString(PROUCTID, params);
		//设置分页参数
		int currentPage = FrontUtils.getPageNo(params);
		int showCount = FrontUtils.getPageSize(params);
		
		Page page=new Page(currentPage,showCount);
		PageData pd = new PageData();
		pd.put("columId",columId);
		pd.put("typeId",typeId);
		pd.put("prouctId",prouctId);
		
		page.setPd(pd);
		List<Product> productlist = null;
		try {
			//根据栏目Id和类型获取产品区域列表
			productlist = productService.queryProductAreaByColumnId(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(productlist));

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
