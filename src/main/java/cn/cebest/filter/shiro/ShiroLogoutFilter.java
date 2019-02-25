package cn.cebest.filter.shiro;

import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

/**
 * logoutFilter
 */
public class ShiroLogoutFilter extends LogoutFilter {
	/**
	 * 返回URL
	 */
	public static final String RETURN_URL = "returnUrl";

	protected String getRedirectUrl(ServletRequest req, ServletResponse resp,Subject subject) {
		HttpServletRequest request = (HttpServletRequest) req;
		String redirectUrl = request.getParameter(RETURN_URL);
		if (StringUtils.isBlank(redirectUrl)) {
			if (request.getRequestURI().startsWith(request.getContextPath() + getManagePrefix())) {
				redirectUrl = getManageLogin();
			} else {
				redirectUrl = getRedirectUrl();
			}
		}
		return redirectUrl;
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
	
}
