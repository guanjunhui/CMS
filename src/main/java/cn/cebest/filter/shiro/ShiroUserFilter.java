package cn.cebest.filter.shiro;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import cn.cebest.interceptor.ManageSiteInterceptor;
import cn.cebest.util.Logger;

/**
 * UserFilter
 */
public class ShiroUserFilter extends AccessControlFilter {
	private static final Logger logger = Logger.getLogger(ManageSiteInterceptor.class);

	//未登录重定向到登陆页
	protected void redirectToLogin(ServletRequest req, ServletResponse resp)
			throws IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String loginUrl=this.getLoginUrl(request);
		//后台地址跳转到后台登录地址，前台需要登录的跳转到shiro配置的登录地址
		WebUtils.issueRedirect(request, response, loginUrl);
	}
	
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) 
    		throws IOException {
    	
    	if (isLoginRequest(request, response)) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
            boolean isAuthic=subject.getPrincipal() != null;
        	if(isNoLoginRequest(request,response) && isAuthic){
				return false;
        	}
            return isAuthic;
        }
    }
    
	protected boolean isLoginRequest(ServletRequest req, ServletResponse resp) {
		return pathsMatch(getLoginUrl(), req)|| pathsMatch(getManageLogin(), req);
	}

	protected boolean isNoLoginRequest(ServletRequest req, ServletResponse resp) {
		boolean flag=false;
		for(String path:manageNoLogin){
			if(pathsMatch(path, req)){
				flag=true;
				break;
			}
		}
		return flag;
	}
	
	public String getLoginUrl(HttpServletRequest request){
		if (request.getRequestURI().startsWith(request.getContextPath() + getManagePrefix())) {
			 return getManageLogin();
		} else {
			return getLoginUrl();
		}
	}
	
	private String managePrefix;
	private String manageLogin;
	private Set<String> manageNoLogin;
	
	public String getManagePrefix() {
		return managePrefix;
	}
	public void setManagePrefix(String managePrefix) {
		this.managePrefix = managePrefix;
	}
	public String getManageLogin() {
		return manageLogin;
	}
	public void setManageLogin(String manageLogin) {
		this.manageLogin = manageLogin;
	}
	public Set<String> getManageNoLogin() {
		return manageNoLogin;
	}
	public void setManageNoLogin(Set<String> manageNoLogin) {
		this.manageNoLogin = manageNoLogin;
	}
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        saveRequestAndRedirectToLogin(request, response);
        return false;
	}

}
