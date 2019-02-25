package cn.cebest.filter.shiro;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.oscache.util.StringUtil;

import cn.cebest.entity.system.AppUser;
import cn.cebest.entity.system.User;
import cn.cebest.interceptor.shiro.ShiroRealm;
import cn.cebest.service.system.appuser.AppuserManager;
import cn.cebest.service.system.statistics.StatisticsManager;
import cn.cebest.service.system.user.UserManager;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.PrintUtil;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.Tools;
import cn.cebest.util.UuidUtil;
import cn.cebest.util.security.CaptchaErrorException;
import cn.cebest.util.security.UsernamePasswordExtendToken;
import net.sf.json.JSONObject;

/**
 * ShiroAuthenticationFilter自定义登录认证filter
 */
public class ShiroAuthenticationFilter extends FormAuthenticationFilter {
	
	private Logger logger = LoggerFactory.getLogger(ShiroAuthenticationFilter.class);
	
	public static final String CAPTCHA_PARAM="code";
	/**
	 * 返回URL
	 */
	public static final String RETURN_URL = "returnUrl";

	@Override
	protected boolean executeLogin(ServletRequest request,ServletResponse response) throws Exception {
		AuthenticationToken token = createToken(request, response);
		if (token == null) {
			throw new IllegalStateException("create AuthenticationToken error");
		}
		HttpServletRequest req = (HttpServletRequest) request;
		String username = (String) token.getPrincipal();
		boolean adminLogin=false;
		if (req.getRequestURI().startsWith(req.getContextPath() + getManagePrefix())){
			adminLogin=true;
		}
		//验证码校验
		Session session = Jurisdiction.getSession();
		//获取session中的验证码
		String sessionCode = (String)session.getAttribute(Const.SESSION_SECURITY_CODE);	
		String code = request.getParameter(CAPTCHA_PARAM);
		if(adminLogin){//后台登陆
			if(StringUtil.isEmpty(sessionCode) || StringUtil.isEmpty(code)){//判断验证码
				return onLoginFailure(token,new CaptchaErrorException(),request, response);
			}
			if(!sessionCode.equalsIgnoreCase(code)){
				return onLoginFailure(token,new CaptchaErrorException(),request, response);
			}
		}
		//判断用户是否有效
		if(isDisabled(req,username,adminLogin)){
			return onLoginFailure(token,new DisabledAccountException(),request, response);
		}
		
		try {
			String password=new String((char[])token.getCredentials());
			UsernamePasswordExtendToken customToken=new UsernamePasswordExtendToken(String.valueOf(token.getPrincipal()),password,adminLogin);
			Subject subject = getSubject(request, response);
			subject.login(customToken);
			return this.onLoginSuccess(token,adminLogin,subject, request, response);
		} catch (AuthenticationException e) {
			return onLoginFailure(token, e, request, response);
		}
	}

	public boolean onPreHandle(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		Session session = Jurisdiction.getSession();
		User user = (User)session.getAttribute(Const.SESSION_USER);	//读取session中的用户信息(单独用户信息)
		boolean isAllowed = isAccessAllowed(request, response, mappedValue);
		if(user==null){
			Subject subject = getSubject(request, response);
	        PrincipalCollection principals = subject.getPrincipals();
	        if (principals != null && !principals.isEmpty()) {
	        	RealmSecurityManager securityManager =  
	        		     (RealmSecurityManager) SecurityUtils.getSecurityManager();
	        	ShiroRealm userRealm = (ShiroRealm) securityManager.getRealms().iterator().next();  
	            userRealm.clearCachedAuthenticationInfo(principals);
	            isAllowed=false;
	        }
		}
		//登录跳转
		if (isAllowed || (!isLoginRequest(request, response) && isPermissive(mappedValue))) {
			try {
				issueSuccessRedirect(request, response);
			} catch (Exception e) {
				logger.error("", e);
			}
			return false;
		}
		return isAllowed || this.onAccessDenied(request, response, mappedValue);
	}
	
	@Override  
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {  
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (logger.isTraceEnabled()) {
					logger.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request, response);
			}
			if (logger.isTraceEnabled()) {
				logger.trace("Login page view.");
			}

			return true;
		}

		if (logger.isTraceEnabled()) {
			logger.trace(
					"Attempting to access a path which requires authentication.  Forwarding to the Authentication url ["
							+ getLoginUrl() + "]");
		}
		saveRequestAndRedirectToLogin(request, response);
		return false;
    }
	
	/**
	 * 登录成功
	 */
	private boolean onLoginSuccess(AuthenticationToken token,boolean adminLogin,Subject subject,
			ServletRequest request, ServletResponse response)
			throws Exception {
		removeCaptcha();//清除验证码
		Object pd=null;
		//设置用户信息
		if(adminLogin){
			pd=setManageUser(String.valueOf(token.getPrincipal()),request);
		}else{
			pd=setMemberUser(String.valueOf(token.getPrincipal()));
		}
		if(pd==null){
			return onLoginFailure(token,new UnknownAccountException(),request, response);
		}
		//设置访问记录
		addStatistics(request);
		return super.onLoginSuccess(token, subject, request, response);
	}
	
	private PageData setManageUser(String userName,ServletRequest request) throws Exception{
		PageData pd=this.userManager.findByUsername(new PageData("USERNAME",userName));
		if(pd != null){
			pd.put("LAST_LOGIN",DateUtil.getTime().toString());
			userManager.updateLastLogin(pd);
			Session session = Jurisdiction.getSession();
			User user = new User();
			user.setUSER_ID(pd.getString("USER_ID"));
			user.setUSERNAME(pd.getString("USERNAME"));
			user.setPASSWORD(pd.getString("PASSWORD"));
			user.setNAME(pd.getString("NAME"));
			user.setRIGHTS(pd.getString("RIGHTS"));
			user.setROLE_ID(pd.getString("ROLE_ID"));
			user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
			user.setIP(pd.getString("IP"));
			user.setSTATUS(pd.getString("STATUS"));
			session.setAttribute(Const.SESSION_USER, user);//把用户信息放session中
		}
		//更新登录IP
		HttpServletRequest req = (HttpServletRequest) request;
		String ip=RequestUtils.getRemoteAddr(req);
		PageData pdIp = new PageData();
		pdIp.put("USERNAME", userName);
		pdIp.put("IP", ip);
		this.userManager.saveIP(pdIp);
		return pd;
	}
	
	private AppUser setMemberUser(String userName) throws Exception{
		AppUser user=this.appuserManager.findPoByUsername(new PageData("USERNAME",userName));
		if(user != null){
			Session session = Jurisdiction.getSession();
			session.setAttribute(Const.SESSION_MEMBER_USER, user);//把用户信息放session中
		}
		return user;
	}
	
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response)
			throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String successUrl = req.getParameter(RETURN_URL);
		if(!isAjax(request)){//判断是否为ajax请求
			if (StringUtils.isBlank(successUrl)) {
				if (req.getRequestURI().startsWith(req.getContextPath() + getManagePrefix())) {
					// 后台直接返回首页
					successUrl = getManageIndex();
					// 清除SavedRequest
					WebUtils.getAndClearSavedRequest(request);
					WebUtils.issueRedirect(request, response, successUrl, null,true);
					return;
				} else {
					successUrl = getSuccessUrl();
				}
			}
			WebUtils.redirectToSavedRequest(req, res, successUrl);
		}else{
			PrintUtil.printJson(res,new JsonResult(200,"登陆成功","success"));
		}
		
	}

	protected boolean isLoginRequest(ServletRequest req, ServletResponse resp) {
		return pathsMatch(getLoginUrl(), req)|| pathsMatch(getManageLogin(), req);
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException ae, ServletRequest request,
			ServletResponse response) {
		if (!isAjax(request)) {
			setFailureAttribute(request, ae);
			return true;
		}
		HttpServletResponse res = (HttpServletResponse) response;
		if(ae instanceof CaptchaErrorException){
			PrintUtil.printJson(res,new JsonResult(-1,"验证码错误","codeerror"));
		}else if(ae instanceof DisabledAccountException){
			PrintUtil.printJson(res,new JsonResult(-1,"账号被禁用","usererror"));
		}else if(ae instanceof IncorrectCredentialsException){
			PrintUtil.printJson(res,new JsonResult(-1,"密码错误","usererror"));
		} else if (ae instanceof UnknownAccountException) {
			PrintUtil.printJson(res,new JsonResult(-1,"账号不存在","usererror"));
		} else if (ae instanceof LockedAccountException ) {
			PrintUtil.printJson(res,new JsonResult(-1,"账号被锁定","usererror"));
		} else {
			PrintUtil.printJson(res,new JsonResult(-1,"登陆发生异常","failure"));
		}
		return false;
	}

	
	//查询用户是否禁用
	private boolean isDisabled(HttpServletRequest req,String username,boolean adminLogin) throws Exception{
		boolean flag=true;
		if (!adminLogin) {//前台会员登录
			PageData pageData=this.appuserManager.findByUsername(new PageData("USERNAME",username));
			if(pageData!=null&&!pageData.isEmpty()){
				String status=(String) pageData.get("STATUS");
				if(Const.VALID.equals(status)){
					flag=false;
				}
			}
		}else{
			PageData pageData=this.userManager.findByUsername(new PageData("USERNAME",username));
			if(pageData!=null&&!pageData.isEmpty()){
				String status=(String) pageData.get("STATUS");
				if(Const.VALID.equals(status)){
					flag=false;
				}
			}
		}
		return flag;
	}
	

	//清除登录验证码的session
	private void removeCaptcha() {
		Session session = Jurisdiction.getSession();
		session.removeAttribute(Const.SESSION_SECURITY_CODE);
	}
	
	/**
	 * 是否是Ajax请求
	 * @param request
	 * @return
	 */
	public static boolean isAjax(ServletRequest request){
		String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
		if("XMLHttpRequest".equalsIgnoreCase(header)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public void addStatistics(ServletRequest req){
		PageData pd = new PageData();
		HttpServletRequest request = (HttpServletRequest) req;
		String ip = RequestUtils.getRemoteAddr(request);
		String path="http://ip.taobao.com/service/getIpInfo.php?ip="+ip;
		try {
			URL url=new URL(path);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.setDoOutput(true);
			http.setDoInput(true);
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			http.setConnectTimeout(2000);
			http.setReadTimeout(2000);
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] bt = new byte[size];
			is.read(bt);
			String message=new String(bt,"UTF-8");
			JSONObject jsonMsg = JSONObject.fromObject(message);
			JSONObject jsonMsg2 = JSONObject.fromObject(jsonMsg.getString("data"));
			String country = jsonMsg2.getString("country");
			String area = jsonMsg2.getString("area");
			String region = jsonMsg2.getString("region");
			String city = jsonMsg2.getString("city");
			String isp = jsonMsg2.getString("isp");
			String AREA = country+" "+area+" "+region+" "+city;
			
			pd.put("IP", ip);								//IP
			pd.put("CDATE", DateUtil.getDay());				//日期
			pd.put("AREA", AREA);		 					//地区
			pd.put("OPERATOR", isp);						//运营商
		    pd.put("CTIME", Tools.date2Str(new Date()));	//时间
			PageData findByIpAndDate = statisticsService.findByIpAndDate(pd);
			if (findByIpAndDate != null && !findByIpAndDate.isEmpty()
					&& findByIpAndDate.size()>0) {
				long count =(Long) findByIpAndDate.get("CCOUNT");
				count++;
				pd.put("CCOUNT", count);
				statisticsService.updateCountByIpAndDate(pd);
			}else{
				pd.put("CCOUNT", Const.INT_1);
				pd.put("STATISTICS_ID", UuidUtil.get32UUID());//主键
				statisticsService.save(pd);
			}
		} catch (MalformedURLException e) {
			logger.error("format the url in access record occured error!", e.getMessage());
		} catch (IOException e) {
			logger.error("send or read the url data in access record occured error!", e.getMessage());
		}catch(Exception e){
			logger.error("save the  in access record data in db occured error!", e);
		}
	}

	@Autowired
	private AppuserManager appuserManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private StatisticsManager statisticsService;


	private String managePrefix;
	private String manageIndex;
	private String manageLogin;
	private Set<String> manageNoLogin;

	public String getManagePrefix() {
		return managePrefix;
	}

	public void setManagePrefix(String managePrefix) {
		this.managePrefix = managePrefix;
	}

	public String getManageIndex() {
		return manageIndex;
	}

	public void setManageIndex(String manageIndex) {
		this.manageIndex = manageIndex;
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
