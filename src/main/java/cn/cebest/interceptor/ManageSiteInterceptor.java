package cn.cebest.interceptor;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.cebest.entity.system.User;
import cn.cebest.entity.web.WebSite;
import cn.cebest.exception.MainTainException;
import cn.cebest.interceptor.shiro.ShiroRealm;
import cn.cebest.service.system.session.SessionService;
import cn.cebest.service.system.site.SiteService;
import cn.cebest.util.Const;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.Logger;
import cn.cebest.util.RequestUtils;


/**
 * 站点请求拦截器
 */
public class ManageSiteInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(ManageSiteInterceptor.class);

	public static final String SITE_ID_PARAM = "_site_id_param";
	public static final String SITE_ID_COOKIE = "_site_id_cookie";
	public static final String CHANGESITE = "changeSite";

	@Autowired
	private SiteService webSiteService;
	@Autowired
	private SessionService sessionService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws ServletException {
		WebSite site = null;
		String path=request.getRequestURI();
		if(logger.getLog4jLogger().isInfoEnabled()){
			logger.info("the request url is:"+path);
		}
		//移除shiro缓存
		String changeSite = RequestUtils.getParameter(request,CHANGESITE);
		if (!StringUtils.isBlank(changeSite) 
				&& Const.YES.equals(changeSite)) {
			Subject subject = SecurityUtils.getSubject();
	        PrincipalCollection principals = subject.getPrincipals();
	        if (principals != null && !principals.isEmpty()) {
	        	RealmSecurityManager securityManager =  
	        		     (RealmSecurityManager) SecurityUtils.getSecurityManager();
	        	ShiroRealm userRealm = (ShiroRealm) securityManager.getRealms().iterator().next();  
	            userRealm.clearCachedAuthenticationInfo(principals);
	        }
		}

		site=getSite(request,response);
		if (site == null) {
			if(path.matches(Const.NO_RUNTIME_PATH)||path.matches(Const.NO_INTERCEPTOR_PATH_FB)){
				return true;
			}
			Session session = Jurisdiction.getSession();
			User user = (User)session.getAttribute(Const.SESSION_USER);
			if(user!=null){
				sessionService.SessionInvalid();
			}
			throw new MainTainException("the main site is not find,it's domain is ["+request.getServerName()+"]");
		}
		RequestUtils.setSite(request,site);
		CurrentThreadVariable.setSite(site);
		return true;
	}
	
	private WebSite getSite(HttpServletRequest request,HttpServletResponse response){
		WebSite site =getByParams(request,response);
		if(site==null){
			site=getByCookie(request);
		}
		if(site==null){
			site=getDefault(request);
		}
		return site;
	}

	/**
	 *切换站点时，从请求中获取站点 
	 */
	private WebSite getByParams(HttpServletRequest request,HttpServletResponse response) {
		String siteId = RequestUtils.getParameter(request,SITE_ID_PARAM);
		if (!StringUtils.isBlank(siteId)) {
			WebSite site=null;
			try {
				site = webSiteService.findSitePoById(siteId);
			} catch (Exception e) {
				throw new RuntimeException("find the site by ID happened error:"+e.getMessage());
			}
			if (site != null) {
				// 若使用参数选择站点，则应该把站点保存至cookie中才好。
				RequestUtils.setCookie(request,response,SITE_ID_COOKIE, site.getId(), null);
				return site;
			}
		}
		return null;
	}
	
	private WebSite getByCookie(HttpServletRequest request) {
		String siteId = RequestUtils.getCookieValue(request,SITE_ID_COOKIE);
		if (!StringUtils.isBlank(siteId)) {
			WebSite site=null;
			try {
				site = webSiteService.findSitePoById(siteId);
			} catch (Exception e) {
				throw new RuntimeException("find the site by ID happened error:"+e.getMessage());
			}
			return site;
		}
		return null;
	}

	private WebSite getDefault(HttpServletRequest request) {
		String server = request.getServerName();
		List<WebSite> list=null;
		WebSite site=null;
		try {
			list = webSiteService.findAllSiteByStatus(null);
		} catch (Exception e) {
			throw new RuntimeException("get site data happened error!",e);
		}
		for (WebSite s : list) {
			// 检查是否为主站
			if (s.getSiteDomian().equals(server)) {
				site = s;
				break;
			}
		}
		return site;
	}


}