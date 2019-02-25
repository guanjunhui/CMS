package cn.cebest.directive;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.service.system.newMessage.MyMessageService;
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
@Component("newmessage_detail")
public class MessageDetailDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private MyMessageService messageService;
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		//获取参数
		String id=ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		String columId=ParamsUtils.getString("columId", params);
		String sort=ParamsUtils.getString("sort", params);
		PageData pd = new PageData();
		pd.put("id", id);
		pd.put("columId", columId);
		pd.put("sort", sort);
		NewMessage mess=null;
		try {
			//mess = messageService.findMessageById(id);
			mess = messageService.findMessageBycolumtypeId(pd);
		} catch (Exception e) {
			logger.error("find the message ocurred error!",e);
		}
		//设置输出变量
        env.setVariable("message", getBeansWrapper().wrap(mess));
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
