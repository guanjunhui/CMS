package cn.cebest.interceptor;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import cn.cebest.entity.web.WebSite;
import cn.cebest.util.Const.LANGUAGE;
import cn.cebest.util.RequestUtils;


/**
 *本地化语言环境设置拦截器
 * @author qichangxin
 * @since 2017.09.13
 */
public class FrontLocaleInterceptor extends HandlerInterceptorAdapter {

	/**
	 * Default name of the locale specification parameter: "locale".
	 */
	public static final String DEFAULT_PARAM_NAME = "locale";

	private String paramName = DEFAULT_PARAM_NAME;


	/**
	 * Set the name of the parameter that contains a locale specification
	 * in a locale change request. Default is "locale".
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * Return the name of the parameter that contains a locale specification
	 * in a locale change request.
	 */
	public String getParamName() {
		return this.paramName;
	}


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		if (localeResolver == null) { 
			throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
		}
		WebSite webSite=RequestUtils.getSite(request);
		String language=webSite.getSiteLanguage();
		Locale locale=null;
		if(LANGUAGE.Chinese.getValue().equalsIgnoreCase(language)){
			locale=Locale.CHINA;
		}else if(LANGUAGE.English.getValue().equalsIgnoreCase(language)){
			locale=Locale.US;
		}
		localeResolver.setLocale(request, response, locale);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
//		LocaleResolver localeResolver = RequestContextUtils
//				.getLocaleResolver(request);
//		if (localeResolver == null) {
//			throw new IllegalStateException(
//					"No LocaleResolver found: not in a DispatcherServlet request?");
//		}
//		if (modelAndView != null) {
//			modelAndView.getModelMap().addAttribute(paramName,
//					localeResolver.resolveLocale(request).toString());
//		}
	}


}
