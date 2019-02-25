package cn.cebest.directive;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.service.system.columconfig.ColumconfigService;
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
 * 查询对应类型下的所有资讯
 */
@Component("framework_detail")
public class IframeDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	@Resource(name = "columconfigService")
	private ColumconfigService columconfigService;
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		//获取参数
		String columId=ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		PageData pd = new PageData();
		pd.put("ID", columId);
		ColumConfig col=null;
		try {
			col= columconfigService.selectColumnById(pd);
		} catch (Exception e) {
			logger.error("find the message ocurred error!",e);
		}
		//设置输出变量
        env.setVariable("iframe", getBeansWrapper().wrap(col));
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
