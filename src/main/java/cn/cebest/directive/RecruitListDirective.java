package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.Recruit;
import cn.cebest.service.system.employ.EmployService;
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

@Component("recruit_list")
public class RecruitListDirective implements TemplateDirectiveModel {

	protected Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private EmployService employService;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		//获取参数
		String columId = ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		String employ_type=ParamsUtils.getString("employ_type", params);
		//设置分页参数
		int currentPage = FrontUtils.getPageNo(params);
		int showCount = FrontUtils.getPageSize(params);
		Page page=new Page(currentPage,showCount);		
		PageData pd = new PageData();
		pd.put("thiscolum", columId);
		pd.put("EMPLOY_TYPE", employ_type);
		
		page.setPd(pd);
		List<Recruit> list=null;
		try {
			list = employService.findlistPageAllByColumId(page);
		} catch (Exception e) {
			logger.error("find the colum ocurred error!",e);
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
