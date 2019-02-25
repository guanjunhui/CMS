package cn.cebest.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.exception.ChannelNotFoundException;
import cn.cebest.exception.DownloadException;
import cn.cebest.exception.MainTainException;
import cn.cebest.exception.PageNotFoundException;
import cn.cebest.interceptor.FrontSiteInterceptor;
import cn.cebest.util.Const;
import cn.cebest.util.Logger;
import cn.cebest.util.RequestUtils;
/**
 * 
* 类名称：MyExceptionResolver.java
* 类描述： 
* @author qichangxin
* 作者单位： 
* 联系方式：qcx@300.cn
* @version 1.0
 */
public class MyExceptionResolver implements HandlerExceptionResolver{
	protected Logger logger = Logger.getLogger(this.getClass());
//	public static final String Eoor_UnauthorizedException="org.apache.shiro.authz.UnauthenticatedException";
//	public static final String Eoor_MainTainException="cn.cebest.exception.MainTainException";
//	public static final String Eoor_PageNotFoundException="cn.cebest.exception.PageNotFoundException";
//	public static final String Eoor_ChannelNotFoundException="cn.cebest.exception.ChannelNotFoundException";
//	public static final String Eoor_AuthorizationException="org.apache.shiro.authz.AuthorizationException";
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
        RequestUtils.setAttribute(request, FrontSiteInterceptor.SITE_KEY, RequestUtils.getSite(request));
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String path = request.getContextPath();
        String basePath = scheme + FrontSiteInterceptor.SCHEM + serverName + Const.SPLIT_COLON + port + path;
        RequestUtils.setAttribute(request, FrontSiteInterceptor.BASE_PATH, basePath);
		
		ModelAndView mv = new ModelAndView();
		if(ex instanceof UnauthenticatedException || ex instanceof AuthorizationException){
			mv.setViewName("noauth");
		}else if(ex instanceof ChannelNotFoundException||ex instanceof DownloadException){
			mv.setViewName("404");
		}else if(ex instanceof PageNotFoundException){
			mv.setViewName("pageNotFound");
		}else if(ex instanceof MainTainException){
			mv.setViewName("maintain");
		}else{
			logger.info("==============异常开始============");	
			logger.error("application catch exception", ex);
			logger.info("==============异常结束============");		
			mv.setViewName("error");
		}
//		switch(ex.getClass().getName()){
//			case Eoor_UnauthorizedException:
//				mv.setViewName("noauth");break;
//			case Eoor_MainTainException:
//				mv.setViewName("maintain");break;
//			case Eoor_PageNotFoundException:
//				mv.setViewName("pageNotFound");break;
//			case Eoor_ChannelNotFoundException:
//				mv.setViewName("404");break;
//			case Eoor_AuthorizationException:
//				mv.setViewName("noauth");break;
//			default:
//				logger.info("==============异常开始============");	
//				logger.error("application catch exception", ex);
//				logger.info("==============异常结束============");		
//				mv.setViewName("error");
//				break;
//		}
		mv.addObject("exception", ex.toString().replaceAll("\n", "<br/>"));
		return mv;
	}

}
