package cn.cebest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.cebest.entity.system.User;
import cn.cebest.util.Const;
import cn.cebest.util.Jurisdiction;
/**
 * 
* 类名称：登录过滤，权限验证
* 类描述： 
* @author 中企高呈
* 作者单位： 
* 联系方式：
* 创建时间：2015年11月2日
* @version 1.6
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
//		String path = request.getServletPath();
		String path=request.getRequestURI();
		String matchPath=path.substring(path.lastIndexOf("/"));
		if(matchPath.matches(Const.NO_INTERCEPTOR_PATH)){
			return true;
		}else{
			User user = (User)Jurisdiction.getSession().getAttribute(Const.SESSION_USER);
			if(user!=null){
//				path = path.substring(1, path.length());
//				String[] paths=StringUtils.split(path, "/");
				boolean b = Jurisdiction.hasJurisdiction(path); //访问权限校验
				if(!b){
					response.sendRedirect(request.getContextPath() + Const.NOAUTH);
				}
				return b;
			}else{
				//登陆过滤
				response.sendRedirect(request.getContextPath() + Const.LOGIN);
				return false;		
			}
		}
	}
	
}
