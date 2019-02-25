package cn.cebest.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.service.system.blacklist.BlacklistManager;
import cn.cebest.util.PageData;

public class BlacklistInterceptor implements HandlerInterceptor {
	@Resource(name="blacklistService")
	private BlacklistManager blacklistService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ip=request.getHeader("x-forwarded-for");;
		if (ip == null) {  
	        ip= request.getRemoteAddr();  
	    }  
		PageData pd=new PageData();
		pd.put("IP", ip);
		Integer i=blacklistService.findByIp(pd);
		if(i==null || i==0){
			return true;
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
