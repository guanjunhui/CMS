package org.apache.shiro.web.filter.authc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormAuthenticationFilter extends AuthenticatingFilter {
	public static final String DEFAULT_ERROR_KEY_ATTRIBUTE_NAME = "shiroLoginFailure";
	public static final String DEFAULT_USERNAME_PARAM = "username";
	public static final String DEFAULT_PASSWORD_PARAM = "password";
	public static final String DEFAULT_REMEMBER_ME_PARAM = "rememberMe";
	private static final Logger log = LoggerFactory.getLogger(FormAuthenticationFilter.class);

	private String usernameParam = "username";
	private String passwordParam = "password";
	private String rememberMeParam = "rememberMe";

	private String failureKeyAttribute = "shiroLoginFailure";

	public FormAuthenticationFilter() {
		setLoginUrl("/login.jsp");
	}

	public void setLoginUrl(String loginUrl) {
		String previous = getLoginUrl();
		if (previous != null) {
			this.appliedPaths.remove(previous);
		}
		super.setLoginUrl(loginUrl);
		if (log.isTraceEnabled()) {
			log.trace("Adding login url to applied paths.");
		}
		this.appliedPaths.put(getLoginUrl(), null);
	}

	public String getUsernameParam() {
		return this.usernameParam;
	}

	public void setUsernameParam(String usernameParam) {
		this.usernameParam = usernameParam;
	}

	public String getPasswordParam() {
		return this.passwordParam;
	}

	public void setPasswordParam(String passwordParam) {
		this.passwordParam = passwordParam;
	}

	public String getRememberMeParam() {
		return this.rememberMeParam;
	}

	public void setRememberMeParam(String rememberMeParam) {
		this.rememberMeParam = rememberMeParam;
	}

	public String getFailureKeyAttribute() {
		return this.failureKeyAttribute;
	}

	public void setFailureKeyAttribute(String failureKeyAttribute) {
		this.failureKeyAttribute = failureKeyAttribute;
	}

	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled()) {
					log.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request, response);
			}
			if (log.isTraceEnabled()) {
				log.trace("Login page view.");
			}

			return true;
		}

		if (log.isTraceEnabled()) {
			log.trace(
					"Attempting to access a path which requires authentication.  Forwarding to the Authentication url ["
							+ getLoginUrl() + "]");
		}

		saveRequestAndRedirectToLogin(request, response);
		return false;
	}

	protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
		return ((request instanceof HttpServletRequest)
				&& (WebUtils.toHttp(request).getMethod().equalsIgnoreCase("POST")));
	}

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		return createToken(username, password, request, response);
	}

	protected boolean isRememberMe(ServletRequest request) {
		return WebUtils.isTrue(request, getRememberMeParam());
	}

	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		issueSuccessRedirect(request, response);

		return false;
	}

	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("Authentication exception", e);
		}
		setFailureAttribute(request, e);

		return true;
	}

	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
		String className = ae.getClass().getName();
		request.setAttribute(getFailureKeyAttribute(), className);
	}

	protected String getUsername(ServletRequest request) {
		return WebUtils.getCleanParam(request, getUsernameParam());
	}

	protected String getPassword(ServletRequest request) {
		return WebUtils.getCleanParam(request, getPasswordParam());
	}
}