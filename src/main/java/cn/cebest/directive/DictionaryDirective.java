package cn.cebest.directive;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.cebest.service.system.dictionaries.DictionariesManager;
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
 * 查询详细信息
 */
@Component("recruit_special")
public class DictionaryDirective implements TemplateDirectiveModel {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		//获取参数
		String educationId=ParamsUtils.getString("educationId", params);
		String EDUCATION=null;
		try {
			EDUCATION= dictionariesService.findNameById(educationId);
			
		} catch (Exception e) {
			logger.error("find the colum ocurred error!",e);
		}
		//设置输出变量
        env.setVariable("education", getBeansWrapper().wrap(EDUCATION));
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
