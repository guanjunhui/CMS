package cn.cebest.util;

import static cn.cebest.interceptor.FrontSiteInterceptor.SITE_KEY;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.util.UrlPathHelper;

import cn.cebest.entity.web.WebSite;
import freemarker.core.Environment;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;;
/**
 * 前台工具类
 */
public class FrontUtils {

	/**
	 * 当前页
	 */
	public static String CURRENTPAGE="currentPage";
	
	/**
	 * 每页显示记录数
	 */
	public static String SHOWCOUNT="showCount";

	
	/**
	 * 页面翻页地址
	 */
	public static final String HREF = "pageHref";
	
	/**
	 * 是否为栏目
	 */
	public static final String ISCHANNEL = "isChannel";

	/**
	 * 类型
	 */
	public static final String TYPE = "type";

	/**
	 * 类型ID
	 */
	public static final String TYPEID = "typeId";
	
	/**
	 * 是否选中
	 */
	public static final String ISSELECT = "isSelect";


	/**
	 * 标签中获得站点
	 * 
	 * @param env
	 * @return
	 * @throws TemplateModelException
	 */
	public static WebSite getSite(Environment env)
			throws TemplateModelException {
		TemplateModel model = env.getGlobalVariable(SITE_KEY);
		if (model instanceof AdapterTemplateModel) {
			return (WebSite) ((AdapterTemplateModel) model)
					.getAdaptedObject(WebSite.class);
		} else {
			throw new TemplateModelException("'" + SITE_KEY
					+ "' not found in DataModel");
		}
	}

	/**
	 * 为前台模板设置分页相关数据
	 * 
	 * @param request
	 * @param map
	 */
	public static void frontPageData(HttpServletRequest request,
			ModelMap map) {
		UrlPathHelper helper = new UrlPathHelper();
		String href = helper.getOriginatingRequestUri(request);
		int pageNo=RequestUtils.getIntParameter(request, CURRENTPAGE, Const.INT_0);
		String isChannel=RequestUtils.getParameter(request, ISCHANNEL);
		String type=RequestUtils.getParameter(request, TYPE);
		String typeId=RequestUtils.getParameter(request, TYPEID);
		String isSelect=RequestUtils.getParameter(request, ISSELECT);
		map.put("area", RequestUtils.getParameter(request, "area"));
		frontPageData(pageNo, href, isChannel, type, typeId, isSelect, map);
	}

	/**
	 * 为前台模板设置分页相关数据
	 * @param pageNo
	 * @param href
	 * @param isChannel
	 * @param type
	 * @param typeId
	 * @param isSelect
	 * @param map
	 */
	public static void frontPageData(int pageNo, String href, String isChannel,
			String type,String typeId,String isSelect,ModelMap map) {
		map.put(CURRENTPAGE, pageNo);
		map.put(HREF, href);
		map.put(ISCHANNEL, isChannel);
		map.put(TYPE, type);
		map.put(TYPEID, typeId);
		map.put(ISSELECT, isSelect);
	}

	/**
	 * 获得分页数据
	 * @param pageNo
	 */
	public static int getPageNo(Map<String, TemplateModel> params) throws TemplateException{
		TemplateModel model = params.get(CURRENTPAGE);
		if (model == null) {
			return Const.INT_0;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				return Const.INT_0;
			}
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
							throw new IllegalArgumentException("dont get the param["+CURRENTPAGE+"]");
			}
		} else if (model instanceof TemplateNumberModel) {
			return ((TemplateNumberModel) model).getAsNumber().intValue();
		} else {
			throw new IllegalArgumentException("dont get the param["+CURRENTPAGE+"]");
		}
	}
	
	/**
	 * 获得每页展示条数
	 * @param pageNo
	 */
	public static int getPageSize(Map<String, TemplateModel> params) throws TemplateException{
		TemplateModel model = params.get(SHOWCOUNT);
		if (model == null) {
			return Const.INT_10;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				return Const.INT_10;
			}
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
							throw new IllegalArgumentException("dont get the param["+SHOWCOUNT+"]");
			}
		} else if (model instanceof TemplateNumberModel) {
			return ((TemplateNumberModel) model).getAsNumber().intValue();
		} else {
			throw new IllegalArgumentException("dont get the param["+SHOWCOUNT+"]");
		}
	}

}
