package cn.cebest.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.system.Dictionaries;
import cn.cebest.service.system.dictionaries.DictionariesManager;
import cn.cebest.util.Const;
import cn.cebest.util.Logger;
import cn.cebest.util.ParamsUtils;
import cn.cebest.util.SystemConfig;
import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 字典数据获取
 */
@Component("dictionaries_list")
public class DictionariesDirective implements TemplateDirectiveModel {
	
	private static final Logger LOGGER = Logger.getLogger(SystemConfig.class);
 
	/**
	 * 输入参数，常量编码。
	 */
	public static final String PARAM_CODE = "bianma";
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String bianma =ParamsUtils.getString(PARAM_CODE, params);
		Dictionaries dictionaries=null;
		//根据编码获取字典数据
		try {
			dictionaries = dictionariesManager.listParentAndChildDict(bianma);
		} catch (Exception e) {
			LOGGER.error("find the dictionaries By BianMa happend error!", e);
		}
		List<Dictionaries> childList =dictionaries.getSubDict();
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
		if(CollectionUtils.isNotEmpty(childList)){
			paramWrap.put(Const.OUT_BEAN, ObjectWrapper.DEFAULT_WRAPPER.wrap(childList));
		}
		//将paramWrap的值复制到variable中
		ParamsUtils.addParamsToVariable(env, paramWrap);
	    if (body != null) {
		    body.render(env.getOut());
        }  
	}
	@Autowired
	private DictionariesManager dictionariesManager;
}
