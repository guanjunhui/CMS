package cn.cebest.directive;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.service.system.newMessage.MyMessageTypeService;
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
 * 根据分类id获取该分类的详细信息
 * @author blue
 *
 */
@Component("id_typeDetail")
public class GetTypeDetailById implements TemplateDirectiveModel{
	protected Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private MyMessageTypeService myMessageTypeService;

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		//获取参数
		String typeId=ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		NewMessageType  newMessageType = null;
		try {
			//查询该分类id下详细信息
			newMessageType = myMessageTypeService.findTypeInfoById(typeId);
		} catch (Exception e) {
			logger.error("get the type by the columId occured error!",e);
		}
		//设置输出变量
	    env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(newMessageType));
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
