package cn.cebest.directive;


import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.cebest.entity.system.product.Product;
import cn.cebest.service.system.product.ProductService;
import cn.cebest.util.Const;
import cn.cebest.util.Logger;
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
 * 栏目详细标签
 */
@Component("product_biaoti")
public class ProductBiaoTiDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 参数名称
	 */
	private static final String CURRENTID = "currentId";
	@Resource(name = "productService")
	private ProductService productService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		//获取参数
		String productId=ParamsUtils.getString(CURRENTID, params);
		
		Product product=null;
		try {
			//根据产品Id和获取产品详情
			product = productService.findProducBiaoTiById(productId);
		} catch (Exception e) {
			logger.error("find the colum by ID ["+productId+"]ocurred error!",e);
		}
		//设置输出变量
        env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(product));
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
