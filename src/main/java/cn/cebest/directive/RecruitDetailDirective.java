package cn.cebest.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.service.system.dictionaries.DictionariesManager;
import cn.cebest.service.system.employ.EmployService;
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
 * 查询详细信息
 */
@Component("recruit_detail")
public class RecruitDetailDirective implements TemplateDirectiveModel {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private EmployService employService;
	
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	/**
	 * 参数名称
	 */
	private static final String RECRUITID = "recruitId";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		//获取参数
		String recruitId=ParamsUtils.getString(RECRUITID, params);
		PageData obj = null;
		PageData pd = new PageData();
		//List<PageData> diclist = null;
		String kong = "";
		pd.put("ID", recruitId);
		try {
			obj = employService.findById(pd);
			
			String xueliID = obj.getString("EDUCATION_REQUIRED");
			String ageID = obj.getString("AGE_REQUIRED");
			String jingyanID = obj.getString("WORK_AGE");
			String xingzhiID = obj.getString("WORK_CATEGORY");
			String sexID = obj.getString("SEX");
			
			String EDUCATION = dictionariesService.findNameById(xueliID);
			String AGE = dictionariesService.findNameById(ageID);
			String WORK_AGE = dictionariesService.findNameById(jingyanID);
			String CATEGORY = dictionariesService.findNameById(xingzhiID);
			String SEX = dictionariesService.findNameById(sexID);
			if(EDUCATION != null && "" != EDUCATION){
				obj.put("EDUCATION_REQUIRED", EDUCATION);
			}else{
				obj.put("EDUCATION_REQUIRED", kong);
			}
			
			if(AGE != null && "" != AGE){
				obj.put("AGE_REQUIRED", AGE);
			}else{
				obj.put("AGE_REQUIRED",kong);
			}
			if(WORK_AGE != null && "" != WORK_AGE){
				obj.put("WORK_AGE", WORK_AGE);
			}else{
				obj.put("WORK_AGE",kong);
			}
			if(CATEGORY != null && "" != CATEGORY){
				obj.put("WORK_CATEGORY", CATEGORY);
			}else{
				obj.put("WORK_CATEGORY", kong);
			}
			if(SEX != null && "" != SEX){
				obj.put("SEX", SEX);
			}else{
				obj.put("SEX", kong);
			}
		} catch (Exception e) {
			logger.error("find the colum ocurred error!",e);
		}
		//设置输出变量
        env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(obj));
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
