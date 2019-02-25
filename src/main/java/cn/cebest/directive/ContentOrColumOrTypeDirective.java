package cn.cebest.directive;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.system.content.Content;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.txt.TxtService;
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

/**
 * 获取url地址栏信息标签
 */
@Component("contentOrColumOrType")
public class ContentOrColumOrTypeDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	private static final String CONTENTID="contentId";
	private static final String COLUMID="columId";
	private static final String TYPEID="typeid";
	private static final String CONTENTORCOLUMORTYPE="contentOrColumOrTypeList";
	/**
	 * 参数名称
	 */
	@Autowired
	private ContentService contentService;
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String contentId=ParamsUtils.getString(CONTENTID, params);
		String columId=ParamsUtils.getString(COLUMID, params);
		String typeid=ParamsUtils.getString(TYPEID, params);
        PageData pd = new PageData();
        pd.put("contentId", contentId);
        pd.put("columId", columId);
        pd.put("typeid", typeid);
		try {
			List<PageData> listpd = contentService.findContentOrColumOrType(pd);
			env.setVariable(CONTENTORCOLUMORTYPE, getBeansWrapper().wrap(listpd));
		} catch (Exception e) {
			logger.error("find the content txt ocurred error!",e);
		}
        if (body != null) {
            body.render(env.getOut());
        }  
	}
	
    public static BeansWrapper getBeansWrapper(){
        BeansWrapper beansWrapper =
                         new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
        return beansWrapper;
    }
}
