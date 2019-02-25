package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.util.Const;
import cn.cebest.util.FrontUtils;
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
@Component("newmessage_list")
public class MessageTypeEtailListDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private MyMessageService messageService;
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		//获取参数
		String typeId=ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		String columId=ParamsUtils.getString("columId", params);
		String flag=ParamsUtils.getString("flag", params);
		String hot=ParamsUtils.getString("hot", params);
		String noTop=ParamsUtils.getString("noTop", params);
		//设置分页参数
		int currentPage = FrontUtils.getPageNo(params);
		int showCount = FrontUtils.getPageSize(params);
		Page page=new Page(currentPage,showCount);
		PageData pd = new PageData();
		pd.put("id",typeId);
		pd.put("hot",hot);
		pd.put("noTop",noTop);
		pd.put("columId",columId);
		if(null != flag && !"".equals(flag)) {
			if("1".equals(flag)) {
				pd.put("sort","sort");
			}else if("2".equals(flag)) {
				pd.put("top","top");
			}  
		} else {
			pd.put("time","time");
		}
		page.setPd(pd);
		List<NewMessage> list=null;
		try {
			//list = messageService.findMessageBytypeOneId(page);
			list = messageService.selectMessageBycolumtypeid(page);
		} catch (Exception e) {
			logger.error("find the message ocurred error!",e);
		}
		//设置输出变量
	    env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(list));
	    env.setVariable(Const.OUT_PAGE, getBeansWrapper().wrap(page));
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
