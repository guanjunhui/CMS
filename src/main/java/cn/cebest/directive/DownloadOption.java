package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.cebest.entity.system.download.FileType;
import cn.cebest.service.system.download.FileTypeService;
import cn.cebest.util.Const;
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

@Component("downloadOption_list")
public class DownloadOption implements TemplateDirectiveModel{
	@Resource(name="fileTypeService")
	private FileTypeService service;

	private static final String COLUMID="columId";
	private static final String SHOWCOUNT="showcount";
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		List<FileType> resultlist=null;
		try {
			String showcount=ParamsUtils.getString(SHOWCOUNT, params);
			PageData pd=new PageData();
			pd.put(SHOWCOUNT, Integer.valueOf(showcount).intValue());
			resultlist=service.findFileTypeOption(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(resultlist));
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
