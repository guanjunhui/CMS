package cn.cebest.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.solr.SearchResult;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.util.Const;
import cn.cebest.util.FrontUtils;
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

@Component("searchTopClmName")
public class SearchTopClmDirective implements TemplateDirectiveModel {
	@Autowired
	private ColumconfigService columconfigService;
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		// 获取当前站点
		WebSite site = FrontUtils.getSite(env);
		String siteId = site.getSiteId();
		// 获取参数
		String columId = ParamsUtils.getString("columId", params);
		// 当前栏目
		ColumConfig columCurrent = null;
		try {
			columCurrent = this.columconfigService.findColumconfigPoById(new PageData("ID", columId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 所有栏目
		List<ColumConfig> columAllList = null;
		try {
			PageData pageDate = new PageData();
			pageDate.put("siteid", site.getId());
			pageDate.put("COLUM_DISPLAY", Const.YES);
			columAllList = this.columconfigService.findAllList(pageDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 查询顶级栏目
		ColumConfig columConfigTop = null;
		List<ColumConfig> columAllListTop = new ArrayList<ColumConfig>();
		columAllListTop.addAll(columAllList);
		columConfigTop = this.searchTop(columCurrent, columAllListTop, columConfigTop);
		env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(columConfigTop));

		if (body != null) {
			// 输出变量
			body.render(env.getOut());
		}

	}

	public static BeansWrapper getBeansWrapper() {
		BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
		return beansWrapper;
	}
	
	//查找顶级栏目
		public ColumConfig searchTop(ColumConfig columCureent, List<ColumConfig> columAllList, ColumConfig columConfigTop) {
			if (Const.PARENT_FLAG.equals(columCureent.getParentid())) {
				return columCureent;
			}
			for (int i = 0; i < columAllList.size(); i++) {
				ColumConfig colum = columAllList.get(i);
				if (columCureent.getParentid().equals(colum.getId())) {
					columAllList.remove(i);
					columConfigTop = colum;
					columConfigTop = this.searchTop(colum, columAllList, columConfigTop);
					break;
				}
			}
			return columConfigTop;
		}
}
