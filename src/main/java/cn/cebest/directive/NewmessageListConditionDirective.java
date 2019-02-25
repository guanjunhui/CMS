package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

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

@Component("newmessage_list_condition")
public class NewmessageListConditionDirective implements TemplateDirectiveModel{
	
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 参数名称
	 */
	private static final String HOTPar = "HOT";
	private static final String CURRENTIDPar = "CURRENTID";
	@Resource(name = "MyMessageService")
	private MyMessageService myMessageService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		//获取参数
		String HOT=ParamsUtils.getString("HOT", params);
		String CURRENTID=ParamsUtils.getString("currentId", params);
		PageData pd = new PageData();
		pd.put(HOTPar, HOT);
		pd.put(CURRENTIDPar, CURRENTID.split(","));
		List<PageData> pageDatas = null;
		try {
			//根据产品Id和获取产品详情
			pageDatas = myMessageService.findnewsList(pd);
		} catch (Exception e) {
			logger.error("find the message by Condition ["+HOT+"]ocurred error!",e);
		}
		//设置输出变量
        env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(pageDatas));
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
