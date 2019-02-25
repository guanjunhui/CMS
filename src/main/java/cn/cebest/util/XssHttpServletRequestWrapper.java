package cn.cebest.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.util.HtmlUtils;


public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper
{
	 HttpServletRequest orgRequest = null;

	 public XssHttpServletRequestWrapper(HttpServletRequest request) {
		 super(request);
		 orgRequest = request;
	 }

	 /**
	  * 覆盖getParameter方法，将参数名和参数值都做xss过滤。
	  * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取
	  * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
	  */
	  @Override
	  public String getParameter(String name) {
		  String value = super.getParameter(xssEncode(name));
		  if (value != null && !"".equals(value)) {
			  value = xssEncode(value);
		  }
		  return value;
	  }
	  
	  @Override    
      public String[] getParameterValues(String name) {    
	        String[] values = super.getParameterValues(name);    
	        if(values != null) {    
	            int length = values.length;    
	            String[] escapseValues = new String[length];    
	            for(int i = 0; i < length; i++){    
	                escapseValues[i] = xssEncode(values[i]);    
	            }    
	            return escapseValues;    
	        }    
	        return super.getParameterValues(name);    
	    }    

	  /**
	  * 覆盖getHeader方法，将参数名和参数值都做xss过滤。
	  * 如果需要获得原始的值，则通过super.getHeaders(name)来获取
	  * getHeaderNames 也可能需要覆盖
	  */
	  @Override
	  public String getHeader(String name) {
		  String value = super.getHeader(xssEncode(name));
		  if (value != null) {
		  value = xssEncode(value);
		  }
		  return value;
	  }

	  /**
	  * 获取最原始的request
	  * 
	  * @return
	  */
	  public HttpServletRequest getOrgRequest() {
		  return orgRequest;
	  }

	  /**
	  * 获取最原始的request的静态方法
	  * 
	  * @return
	  */
	  public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
		  if (req instanceof XssHttpServletRequestWrapper) {
		  return ((XssHttpServletRequestWrapper) req).getOrgRequest();
		  }
		  return req;
	  }
	  
	  public String escape(String s)
	    {
	    	StringBuilder sb = new StringBuilder(s.length() + 16);
	        for (int i = 0; i < s.length(); i++)
	        {
	            char c = s.charAt(i);
	            switch (c)
	            {
	            case '>':
	                sb.append('＞');// 全角大于号
	                break;
	            case '<':
	                sb.append('＜');// 全角小于号
	                break;
	            case '\'':
	                sb.append('‘');// 全角单引号
	                break;
	            case '\"':
	                sb.append('“');// 全角双引号
	                break;
	            case '\\':
	                sb.append('＼');// 全角斜线
	                break;
	            case '%':
	        	sb.append('％'); // 全角冒号
	        	break;
	            default:
	                sb.append(c);
	                break;
	            }

	        }
	        return sb.toString();
	    }
		
		
		@Override
		public Map<String, String[]> getParameterMap() {
			Map<String, String[]> paraMap = super.getParameterMap();
			Map<String, String[]> result = new HashMap<String, String[]>(paraMap.size());
	        // 对于paraMap为空的直接return
	        if (null == paraMap || paraMap.isEmpty()) {
	            return paraMap;
	        }
	        for (Entry<String, String[]> entry : paraMap.entrySet()) {
	            String key = entry.getKey();
	            String[] values     = entry.getValue();
	            if (null == values) {
	                continue;
	            }
	            String[] newValues  = new String[values.length];
	            for (int i = 0; i < values.length; i++) {
	                newValues[i] = xssEncode(values[i]);
	            }
	            result.put(key, newValues);
	        }
	        return result;
		}
		
		  
		  
		  
		  /**
		     * 获取request的属性时，做xss过滤
		     */
		   /* @Override
		    public Object getAttribute(String name) {
		        Object value = orgRequest.getAttribute(name);
		        if (null != value && value instanceof String) {
		            value = xssEncode((String) value);
		        }
		        return value;
		    }*/

	    
	    /**
	     * 将容易引起xss漏洞的半角字符直接替换成全角字符
	     * 
	     * @param s
	     * @return
	     */
	    public String xssEncode(String s)
	    {
	      	if (s == null || s.isEmpty())
	        {
	            return s;
	        }
	        
	      	String result = stripXSS(s);
	      	if (null != result)
	      	{
	      		result = escape(result);
	      	}
	      	
	      	return result;
	    }
	    
	    private String stripXSS(String value) 
	   
	    {
	    	return HtmlUtils.htmlEscape(value);
	    	        /*if (value != null) 
	    	        {
	    	            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
	    	            // avoid encoded attacks.
	    	            // value = ESAPI.encoder().canonicalize(value);
	    	            // Avoid null characters
	    	            value = value.replaceAll("", "");
	    	            // Avoid anything between script tags
	    	            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            //自定义过滤(只保留a-z,A-Z,0-9,&,=)
	    	            scriptPattern = Pattern.compile("[`~!@#$%^&*()_\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            //value = value.replaceAll("%","");
	    	            // Avoid anything in a src='...' type of expression
	    	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            // Remove any lonesome </script> tag
	    	            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            // Remove any lonesome <script ...> tag
	    	            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            // Avoid eval(...) expressions
	    	            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            // Avoid expression(...) expressions
	    	            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            // Avoid javascript:... expressions
	    	            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            // Avoid vbscript:... expressions
	    	            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            // Avoid onload= expressions
	    	            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            
	    	            scriptPattern = Pattern.compile("<iframe>(.*?)</iframe>", Pattern.CASE_INSENSITIVE);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            
	    	            scriptPattern = Pattern.compile("</iframe>", Pattern.CASE_INSENSITIVE);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	            // Remove any lonesome <script ...> tag
	    	            scriptPattern = Pattern.compile("<iframe(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	    	            value = scriptPattern.matcher(value).replaceAll("");
	    	        }
	    	        return value;*/
	    	}
}
