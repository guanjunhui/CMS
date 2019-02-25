package cn.cebest.directive;


import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.system.content.Content;
import cn.cebest.service.system.content.content.ContentService;
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
 * 关于我们标签
 */
@Component("index_policyDetail")
public class PolicyDetailDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ContentService contentService;
	
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String contentId=ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		//根据内容id查询详情
		
		Content content = null;
		try {
			content = contentService.findTxt(contentId);
		} catch (Exception e) {
			logger.error("find the contentDetail ocurred error!",e);
		}
		//设置输出变量
        env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(content));
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
