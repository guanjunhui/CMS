package cn.cebest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.cebest.util.Logger;
import cn.cebest.util.RequestWrapper;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.XssHttpServletRequestWrapper;



public class XssFilter implements Filter{
	protected Logger logger = Logger.getLogger(this.getClass());
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String casIp = SystemConfig.getPropertiesString("web.cas_ip");
		if(casIp.equals(request.getRemoteAddr())){
			chain.doFilter(request, response);
			return;
		}
		RequestWrapper xssRequest = new RequestWrapper((HttpServletRequest) request);
		try {
			 chain.doFilter(xssRequest, response);
		} catch (Exception e) {
			if(e.getMessage().contains("Could not resolve view with name")){
				HttpServletRequest req = (HttpServletRequest)request;
				req.getRequestDispatcher("/channel/dasdasdas").forward(request, response);
			}else{
				chain.doFilter(xssRequest, response);
			}
		}
	}

	@Override
	public void destroy() {
		
	}

}
