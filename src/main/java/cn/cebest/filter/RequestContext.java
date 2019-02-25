package cn.cebest.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestContext {
	private static final ThreadLocal<HttpServletRequest>  _request = 
			new ThreadLocal<HttpServletRequest>();  
    private static final ThreadLocal<HttpServletResponse> _response = 
    		new ThreadLocal<HttpServletResponse>();  
    
    public static void setRequest(HttpServletRequest request) {  
        _request.set(request);  
    }  
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = _request.get();  
        return request;  
    }  
    public static void removeRequest() {
        _request.remove();  
    }  
      
    public static void setResponse(HttpServletResponse response) {  
        _response.set(response);  
    }  
  
    public static HttpServletResponse getResponse() {  
        HttpServletResponse response = _response.get();  
        return response;  
    }  
  
    public static void removeResponse() {  
        _response.remove();  
    }
}
