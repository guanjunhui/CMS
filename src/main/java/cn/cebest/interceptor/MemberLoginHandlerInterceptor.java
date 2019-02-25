package cn.cebest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.cebest.entity.system.AppUser;
import cn.cebest.util.RequestUtils;
/**
 * 
* 类名称：会员登录拦截
* 类描述： 
* @author 中企高呈
* 作者单位： 
* 联系方式：
* 创建时间：2017年09月21日
* @version 1.6
 */
public class MemberLoginHandlerInterceptor extends HandlerInterceptorAdapter{

	public static final String LOGIN = "/member/login";//登录地址
	public static final String INDEX = "/member/index";//登录地址
	public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(logout)|(code)|(app)|(static)|(main)|(websocket)|(fhadmin)|(web)|(fh_static_1)|(fh_static_2)|(fh_static_3)|(fh_static_4)|(ueditor)|(uploadImgs)).*";	//不对匹配该值的访问路径拦截（正则）

	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String path=request.getRequestURI();
		if(path.matches(NO_INTERCEPTOR_PATH)){
			return true;
		}else{
			AppUser user = RequestUtils.getMemberUser(request);
			if(user!=null){
				return true;
			}else{
				//登陆过滤
				response.sendRedirect(request.getContextPath() + LOGIN);
				return false;		
			}
		}
	}
	
}
