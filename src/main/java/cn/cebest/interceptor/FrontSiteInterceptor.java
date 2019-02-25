package cn.cebest.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.site.SiteService;
import cn.cebest.util.Const;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.SystemConfig;

/**
 * 站点请求拦截器
 */
public class FrontSiteInterceptor extends HandlerInterceptorAdapter {
	protected Logger logger = Logger.getLogger(this.getClass());

	public static final String LOCAL_PARAM = "local";
	public static final String SCHEM = "://";
	public static final String BASE_PATH = "basePath";
	public static final String AUTO_PATH = "autoPath";
	public static final String STR_PATH = "strPath";
	public static final String SITE_KEY = "site";
	public static final String IMAGE_PATH = "imgPath";
	public static final String FILE_PATH = "filePath";
	public static final String STATIC_DOMAIN = "staticDomain";
	public static final String IMG_DOMAIN = "imgDomain";
	public static final String FILE_DOMAIN = "fileDomain"; 

	@Autowired
	private SiteService webSiteService;
	
	@Autowired
	private ColumconfigService columconfigService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
		WebSite site = null;
		site=getSiteByParam(request);
		if(site==null){
			List<WebSite> list = null;
			try {
				list = webSiteService.findAllSiteByStatus(null);
			} catch (Exception e) {
				throw new RuntimeException("get site data happend error!", e);
			}
			site=getSiteByCookie(request,list);
			if(site==null){
				site=getSiteByDefault(request,list);
			}
			if (site == null || Const.INVALID.equals(site.getSiteStatus())) {
				throw new RuntimeException("the site is not find");
			}
		}
		RequestUtils.setCookie(request, response, LOCAL_PARAM, site.getSiteLanguage(), null, false);
		RequestUtils.setSite(request, site);
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView view) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int port = request.getServerPort();
		String path = request.getContextPath();
		String basePath = scheme + SCHEM + serverName + Const.SPLIT_COLON + port + path;
		String autoPath = "https" + SCHEM + serverName + path;
		String strPath = scheme + SCHEM + serverName + Const.SPLIT_COLON + port;
		RequestUtils.setAttribute(request, IMAGE_PATH, Const.FILEPATHIMG);
		RequestUtils.setAttribute(request, FILE_PATH, Const.FILEPATHFILE);
		RequestUtils.setAttribute(request, BASE_PATH, basePath);
		RequestUtils.setAttribute(request, AUTO_PATH, autoPath);
		RequestUtils.setAttribute(request, STR_PATH, strPath);
		RequestUtils.setAttribute(request, SITE_KEY, RequestUtils.getSite(request));
		RequestUtils.setAttribute(request, STATIC_DOMAIN, SystemConfig.getPropertiesString(STATIC_DOMAIN, basePath));
		RequestUtils.setAttribute(request, IMG_DOMAIN, SystemConfig.getPropertiesString(IMG_DOMAIN, basePath));
		RequestUtils.setAttribute(request, FILE_DOMAIN, SystemConfig.getPropertiesString(FILE_DOMAIN, basePath));
	}
	
	private WebSite getSiteByCookie(HttpServletRequest request,List<WebSite> list){
		WebSite site = null;
		int size = list.size();
		if (size == Const.INT_0) {
			throw new RuntimeException("no site record in database!");
		} else if (size == Const.INT_1) {
			site = list.get(Const.INT_0);
		} else {
			String local=RequestUtils.getCookieValue(request,LOCAL_PARAM);
			if(StringUtils.isEmpty(local)){
				return null;
			}
			String server = request.getServerName();
			for (WebSite s : list) {
				// 检查域名
				  if (s.getSiteDomian().equals(server) && s.getSiteLanguage().equalsIgnoreCase(local)) {
					  site = s;
				      break; 
				  }
			}
		}
		return site;
	}
	
	private WebSite getSiteByDefault(HttpServletRequest request,List<WebSite> list){
		WebSite site = null;
		String server = request.getServerName();
		for (WebSite s : list) {
			// 检查域名
			  if (s.getSiteDomian().equals(server)) {
				  site=s;
				  if (s.getIfStatic().equals(Const.YES)) {
					  site=s;
					  break;
				  }
			  }
		}
		return site;
	}
	
	private WebSite getSiteByParam(HttpServletRequest request){
		Map<String,Object> pathVariables = (Map<String,Object>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);  
		WebSite site=null;
		if(pathVariables!=null && !pathVariables.isEmpty()){
			String id = (String)pathVariables.get("columId");
			if(StringUtils.isBlank(id)){
				id=(String)request.getParameter("columnId");
			}
			if(StringUtils.isNotBlank(id)){
				try {
					ColumConfig columCurrent = this.columconfigService.findColumconfigPoById(new PageData("ID", id));
					if(columCurrent==null){
						return null;
					}
					site = webSiteService.findSitePoById(columCurrent.getSiteid());
				} catch (Exception e) {
					logger.error("拦截器查询栏目站点出现异常:",e);
				}
			}
		}
		return site;
	}
}