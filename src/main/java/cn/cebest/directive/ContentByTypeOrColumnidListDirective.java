package cn.cebest.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.cebest.entity.system.Recruit;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.system.download.FileResources;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.entity.system.product.Product;
import cn.cebest.entity.system.product.Product_Type;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.service.system.employ.EmployService;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.service.system.product.ProductService;
import cn.cebest.service.system.txt.TxtService;
import cn.cebest.util.Const;
import cn.cebest.util.FrontUtils;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;
import cn.cebest.util.ParamsUtils;
import cn.cebest.util.RequestUtils;
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
@Component("content_ByTypeOrColumnid")
public class ContentByTypeOrColumnidListDirective implements TemplateDirectiveModel {

	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private ContentService contentService;
	@Autowired
	private MyMessageService mymessageService;
	@Autowired
	private ProductService productService;
	@Autowired
	private EmployService employService;
	@Autowired
	private FileResourceService fileResourceService;
	/**
	 * 参数名称
	 */
	private static final String CONTENTID = "contentId";
	private static final String TYPE = "type";
	private static final String COLUMNID = "columnId";
	private static final String TYPEID = "typeId";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		// 获取参数
		// 获取当前站点
		WebSite site = FrontUtils.getSite(env);
		String siteId = site.getSiteId();
		String contentId = ParamsUtils.getString(CONTENTID, params);
		String type = ParamsUtils.getString(TYPE, params);
		String columnId = ParamsUtils.getString(COLUMNID, params);
		String typeId = ParamsUtils.getString(TYPEID, params);
		PageData pd = new PageData();
		pd.put("contentId", contentId);
		pd.put("SITEID", siteId);
		pd.put("type", type);
		pd.put("columnId", columnId);
		pd.put("typeId", typeId);
		try {
			if(Const.TEMPLATE_TYPE_1.equals(type) ){
				Content content = contentService.findContentByTypeOrColumnid(pd);
				// 设置输出变量
				env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(content));
			}else if(Const.TEMPLATE_TYPE_2.equals(type)){
				NewMessage content = mymessageService.findMessageByTypeOrColumnid(pd);
				// 设置输出变量
				env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(content));
			}else if(Const.TEMPLATE_TYPE_3.equals(type)){
				Product content = productService.findProductByTypeOrColumnid(pd);
				// 设置输出变量
				env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(content));
			}else if(Const.TEMPLATE_TYPE_4.equals(type)){
				Recruit content = employService.findRecruitByTypeOrColumnid(pd);
				// 设置输出变量
				env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(content));
			}else if(Const.TEMPLATE_TYPE_5.equals(type)){
				FileResources content = fileResourceService.findFileResourcesByTypeOrColumnid(pd);
				// 设置输出变量
				env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(content));
			}
		} catch (Exception e) {
			logger.error("find the colum ocurred error!", e);
		}
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
