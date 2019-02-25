package cn.cebest.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestContextFilter implements Filter{
	private Set<String> prefixIignores = new HashSet<String>();
	@Override
	public void destroy() {  
        
    }
	@Override
    public void doFilter(ServletRequest request, ServletResponse response,  
            FilterChain chain) throws IOException, ServletException {  
        HttpServletRequest httpRequest = (HttpServletRequest)request;  
        if (canIgnore(httpRequest)) {  
            chain.doFilter(request, response);  
            return;  
        }  
        
        HttpServletResponse httpResponse = (HttpServletResponse)response;  
        RequestContext.setRequest(httpRequest);  
        RequestContext.setResponse(httpResponse);  
        try{  
            chain.doFilter(request, response);
        }finally{ //不管有木有出现异常，finally块中代码都会执行;在这里，相当于只服务于当前请求。  
        	RequestContext.removeRequest();
        	RequestContext.removeResponse();  
        }  
    }
	@Override
    public void init(FilterConfig config) throws ServletException {  
        String cp = config.getServletContext().getContextPath();  
        String ignoresParam = config.getInitParameter("ignores");  
        String[] ignoreArray = ignoresParam.split(",");  
        for (String s : ignoreArray) {  
            prefixIignores.add(cp + s);  
        } 
    }
	
    private boolean canIgnore(HttpServletRequest request) {  
        String url = request.getRequestURI();  
        for (String ignore : prefixIignores) {  
            if (url.startsWith(ignore)) {  
                return true;  
            }  
        }  
        return false;  
    }
}
