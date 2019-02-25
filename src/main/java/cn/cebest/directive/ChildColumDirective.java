package cn.cebest.directive;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
 * 栏目详细标签
 */
@Component("childColum_list")
public class ChildColumDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 参数名称
	 */
	private static final String COLUMID = "columId";

	@Autowired
	private ColumconfigService columconfigService;
	
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		//获取参数
		String columId=ParamsUtils.getString(COLUMID, params);
		String COLUM_DISPLAY=ParamsUtils.getString("COLUM_DISPLAY", params);
		PageData pd = new PageData();
		pd.put("ID", columId);
		pd.put("COLUM_DISPLAY", COLUM_DISPLAY);
		List<PageData> datalist=null;
		try {
			datalist = columconfigService.findColumconfigByPid(pd);
		} catch (Exception e) {
			logger.error("find the colum by ID ["+columId+"]ocurred error!",e);
		}
		//设置输出变量
        env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(datalist));
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
