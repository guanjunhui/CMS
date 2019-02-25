package cn.cebest.directive;


import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.service.system.columconfig.ColumconfigService;
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
 * 通过栏目ID获取对应的内容，只取最新的一条
 */
@Component("colum_contentdata_one")
public class ColumContentDataDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	

	@Autowired
	private ColumconfigService columconfigService;
	
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		//获取参数
		String currentId=ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		
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
