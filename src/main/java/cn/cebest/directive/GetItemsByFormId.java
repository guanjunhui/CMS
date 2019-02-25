package cn.cebest.directive;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.system.customForm.CustomFormAttribute;
import cn.cebest.service.system.customForm.CustomFormAttributeService;
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
 * 根据表单ID获取表单项
 */
@Component("form_itemsList")
public class GetItemsByFormId implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	private static final String FORMID="formId";//表单ID

	@Autowired
	private CustomFormAttributeService customFormAttributeService;

	/**
	 * 参数名称
	 */
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String formId=ParamsUtils.getString(FORMID, params);
		List<CustomFormAttribute> formIdList = null;
		try {
			formIdList = this.customFormAttributeService.getAttributeListByFormID(formId);
		    env.setVariable(Const.OUT_LIST2, getBeansWrapper().wrap(formIdList));
		} catch (Exception e) {
			logger.error("get the Items by the formId occured error!",e);
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
