package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.system.content.ContentType;
import cn.cebest.service.system.content.contentType.ContentTypeService;
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
 * 根据栏目id查询内容分类列表
 */
@Component("contentTypeListByPid_list")
public class ContentTypeListByPidDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 参数名称
	 */
	@Autowired
	private ContentTypeService contentService;
//	@Autowired
//	private TxtService txtService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		// 获取参数
		// 获取当前站点
		String pid = ParamsUtils.getString("pid", params);
		PageData pd = new PageData();
		pd.put("PID", pid);
		try {
			List<ContentType> content = contentService.findContentTypeListByColumnIdOrPid(pd);
			// 设置输出变量
			env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(content));
			if (body != null) {
				// 输出变量
				body.render(env.getOut());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BeansWrapper getBeansWrapper() {
		BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
		return beansWrapper;
	}
}
