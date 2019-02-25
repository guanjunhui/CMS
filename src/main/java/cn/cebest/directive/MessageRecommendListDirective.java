package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
 * 根据栏目ID查询推荐到该栏目的新闻资讯内容
 * @author lhb
 *
 */
@Component("messageRecommend_list")
public class MessageRecommendListDirective implements TemplateDirectiveModel{

	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 参数名称
	 */
	private static final String COLUMID = "columId";
	private static final String TYPEID = "typeId";
	
	@Autowired
	//private MyMessageClient messageService;
	private MyMessageService messageService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		// 获取参数
		String columId = ParamsUtils.getString(COLUMID, params);
		String typeId = ParamsUtils.getString(TYPEID, params);
		
		PageData pd = new PageData();
		pd.put("columId",columId);
		if(StringUtils.isNotEmpty(typeId)) pd.put("typeId",typeId);
		List<NewMessage> newmessagelist = null;
		try {
			//根据 栏目Id Or 分类Id or 两者一起 获取推荐资讯列表
		    newmessagelist = messageService.selectRecommendMessageByColumnIdOrTypeId(pd);
		} catch (Exception e) {
			logger.error("get the message Recommend list by by the columId or typeId ocurred error!", e);
		}
		env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(newmessagelist));
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
