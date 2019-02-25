package cn.cebest.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
@Component("message_surface")
public class MessageSurfaceDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private MyMessageService messageService;
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		List<NewMessage> mess=new ArrayList<NewMessage>();
		String columnId=ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		PageData pd=new PageData();
		pd.put("columnId", columnId);
		try {
			mess= messageService.findNewRecommendMessage(pd);
		} catch (Exception e) {
			logger.error("find the message ocurred error!",e);
		}
		//设置输出变量
        env.setVariable("message_list", getBeansWrapper().wrap(mess));
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
