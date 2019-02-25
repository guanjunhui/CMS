package cn.cebest.util;


import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;

import cn.cebest.entity.system.AppUser;
import cn.cebest.entity.system.User;
import cn.cebest.entity.web.WebSite;



/**
 * 用于处理HTTP请求的工具类
 * @author qichangxin
 */
public class RequestUtils {
	
	/**
	 * 前台会员用户KEY
	 */
	public static final String MEMBER_KEY = "_member_user_key";
	
	/**
	 * 后台用户KEY
	 */
	public static final String ADMIN_KEY = "_admin_user_key";
	/**
	 * 站点KEY
	 */
	public static final String SITE_KEY = "_site_key";

	public static HttpSession getSession(HttpServletRequest request) {
		return request.getSession();
	}
	
	/**
	 * 获取response对象
	 */
	public static HttpServletResponse getResponse(HttpServletResponse response,String contentType,String encode) {
		response.setContentType(contentType);
		response.setCharacterEncoding(encode);
		return response;
	}
	
	public static ServletContext getServletContext(HttpServletRequest request) {
		return getSession(request).getServletContext();
	}
	
	
	/**
	 * 获取真实路径
	 */
	public static String getRealPath(HttpServletRequest request,String path) {
		return getServletContext(request).getRealPath(path);
	}
	
	/**
	 * 获取请求参数
	 */
	public static String getParameter(HttpServletRequest request,String name) {
		return request.getParameter(name);
	}
	
	/**
	 * 获取请求参数数组
	 */
	public static String[] getParameterValues(HttpServletRequest request,String name) {
		return request.getParameterValues(name);
	}
	
	/**
	 * 获取int类型请求参数
	 */
	public static int getIntParameter(HttpServletRequest request,String name) {
		return Integer.parseInt(getParameter(request,name));
	}

	/**
	 * 获取int类型请求参数,当出错时,返回defaultValue
	 */
	public static int getIntParameter(HttpServletRequest request,String name,int defaultValue) {
		try {
			return Integer.parseInt(getParameter(request,name));
		} catch(Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 设置attribute到request中
	 */
	public static void setAttribute(HttpServletRequest request,String name,Object value) {
		request.setAttribute(name, value);
	}
	
	/**
	 * 从request中获取attribute
	 */
	public static Object getAttribute(HttpServletRequest request,String name) {
		return request.getAttribute(name);
	}
	
	/**
	 * 设置attribute到session中
	 */
	public static void setSessionAttribute(HttpServletRequest request,String name,Object value) {
		getSession(request).setAttribute(name, value);
	}
	
	/**
	 * 从session中获取attribute
	 */
	public static Object getSessionAttribute(HttpServletRequest request,String name) {
		return getSession(request).getAttribute(name);
	}
	
	/**
	 * 清除session
	 * 
	 * @param request
	 * @param user
	 */
	public static void removeSession(HttpServletRequest request,String name) {
		getSession(request).removeAttribute(name);
	}
	
	/**
	 * 设置attribute到application中
	 */
	public static void setAppAttribute(HttpServletRequest request,String name,Object value) {
		getServletContext(request).setAttribute(name, value);
	}
	
	/**
	 * 从application中获取attribute
	 */
	public static Object getAppAttribute(HttpServletRequest request,String name) {
		return getServletContext(request).getAttribute(name);
	}
	
	/**
	 * 获得站点
	 * 
	 * @param request
	 * @return
	 */
	public static WebSite getSite(HttpServletRequest request) {
		return (WebSite) request.getAttribute(SITE_KEY);
	}

	/**
	 * 设置站点
	 * 
	 * @param request
	 * @param site
	 */
	public static void setSite(HttpServletRequest request,WebSite site) {
		request.setAttribute(SITE_KEY, site);
	}

	/**
	 * 获得站点ID
	 * 
	 * @param request
	 * @return
	 */
	public static String getSiteId(HttpServletRequest request) {
		return getSite(request).getId();
	}
	
	/**
	 * 获得前台会员用户
	 * 
	 * @param request
	 * @return
	 */
	public static AppUser getMemberUser(HttpServletRequest request) {
		return (AppUser) getSession(request).getAttribute(MEMBER_KEY);
	}

	/**
	 * 设置前台会员用户
	 * 
	 * @param request
	 * @param user
	 */
	public static void setMemberUser(HttpServletRequest request,AppUser user) {
		getSession(request).setAttribute(MEMBER_KEY, user);
	}
	
	/**
	 * 清除前台会员用户
	 * 
	 * @param request
	 * @param user
	 */
	public static void RemoveMemberUser(HttpServletRequest request) {
		removeSession(request,MEMBER_KEY);
	}

	/**
	 * 获得前台会员用户ID
	 * 
	 * @param request
	 * @return
	 */
	public static String getMemberUserId(HttpServletRequest request) {
		AppUser user = getMemberUser(request);
		if (user != null) {
			return user.getUserId();
		} else {
			return "";
		}
	}

	/**
	 * 获得后台用户
	 * 
	 * @param request
	 * @return
	 */
	public static User getAdminUser(HttpServletRequest request) {
		return (User) request.getAttribute(ADMIN_KEY);
	}

	/**
	 * 设置后台用户
	 * 
	 * @param request
	 * @param user
	 */
	public static void setAdminUser(HttpServletRequest request,User user) {
		request.setAttribute(ADMIN_KEY, user);
	}

	/**
	 * 获得后台用户ID
	 * 
	 * @param request
	 * @return
	 */
	public static String getAdminUserId(HttpServletRequest request) {
		User user = getAdminUser(request);
		if (user != null) {
			return user.getUSER_ID();
		} else {
			return "";
		}
	}
	
    /**
     * 获取客户端IP地址，此方法用在proxy环境中
     * @param req
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotBlank(ip)){
            String[] ips = StringUtils.split(ip,',');
            if(ips!=null){
                for(String tmpip : ips){
                    if(StringUtils.isBlank(tmpip))
                        continue;
                    tmpip = tmpip.trim();
                    if(isIPAddr(tmpip) && !tmpip.startsWith("10.") && !tmpip.startsWith("192.168.") && !"127.0.0.1".equals(tmpip)){
                        return tmpip.trim();
                    }
                }
            }
        }
        ip = request.getHeader("x-real-ip");
        if(isIPAddr(ip))
            return ip;
        ip = request.getRemoteAddr();
        if(ip.indexOf('.')==-1 || ip.equals("0:0:0:0:0:0:0:1"))
            ip = "127.0.0.1";
        return ip;
    }
	
    /**
     * 获取COOKIE
     * 
     * @param name
     */
    public static Cookie getCookie(HttpServletRequest request,String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return null;
        for (Cookie ck : cookies) {
            if (StringUtils.equalsIgnoreCase(name,ck.getName())) 
                return ck;          
        }
        return null;
    }
	

    /**
     * 获取COOKIE
     * 
     * @param name
     */
    public static String getCookieValue(HttpServletRequest request,String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return null;
        for (Cookie ck : cookies) {
            if (StringUtils.equalsIgnoreCase(name,ck.getName())) 
                return ck.getValue();           
        }
        return null;
    }
	
    /**
     * 设置COOKIE
     * 
     * @param name
     * @param value
     * @param maxAge
     */
    public static void setCookie(HttpServletRequest request,HttpServletResponse response,String name, String value, Integer maxAge) {
        setCookie(request,response,name,value,maxAge,true);
    }
	
    /**
     * 设置COOKIE
     * 
     * @param name
     * @param value
     * @param maxAge
     */
    public static void setCookie(HttpServletRequest request,HttpServletResponse response,String name,String value, Integer maxAge, boolean all_sub_domain) {
        Cookie cookie = new Cookie(name, value);
        if(maxAge!=null){
            cookie.setMaxAge(maxAge);
        }
        if(all_sub_domain){
            String serverName = request.getServerName();
            String domain = getDomainOfServerName(serverName);
            if(domain!=null && domain.indexOf('.')!=-1){
                cookie.setDomain('.' + domain);
            }
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }
	
    /**
     * 获取用户访问URL中的根域名
     * 例如: www.dlog.cn -> dlog.cn
     * @param req
     * @return
     */
    public static String getDomainOfServerName(String host){
        if(isIPAddr(host))
            return null;
        String[] names = StringUtils.split(host, '.');
        int len = names.length;
        if(len==1) return null;
        if(len==3){
            return makeup(names[len-2],names[len-1]);
        }
        if(len>3){
            String dp = names[len-2];
            if(dp.equalsIgnoreCase("com")||dp.equalsIgnoreCase("gov")||dp.equalsIgnoreCase("net")||dp.equalsIgnoreCase("edu")||dp.equalsIgnoreCase("org"))
                return makeup(names[len-3],names[len-2],names[len-1]);
            else
                return makeup(names[len-2],names[len-1]);
        }
        return host;
    }
    
    private static String makeup(String...ps){
        StringBuilder s = new StringBuilder();
        for(int idx = 0; idx < ps.length; idx++){
            if(idx > 0)
                s.append('.');
            s.append(ps[idx]);
        }
        return s.toString();
    }
    

    /**
     * 判断字符串是否是一个IP地址
     * @param addr
     * @return
     */
    public static boolean isIPAddr(String addr){
        if(StringUtils.isEmpty(addr))
            return false;
        String[] ips = StringUtils.split(addr, '.');
        if(ips.length != 4)
            return false;
        try{
            int ipa = Integer.parseInt(ips[0]);
            int ipb = Integer.parseInt(ips[1]);
            int ipc = Integer.parseInt(ips[2]);
            int ipd = Integer.parseInt(ips[3]);
            return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
                    && ipc <= 255 && ipd >= 0 && ipd <= 255;
        }catch(Exception e){}
        return false;
    }

	public static String getAddrByClientIp(HttpServletRequest request) {
		GetMethod getMethod = null;
		byte[] responseBody = null;
		String AddrString = "";
		String countryValue = "";
		try {
			String clientIp = getRemoteAddr(request);
			//String clientIp="128.134.0.0";
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setContentCharset("UTF-8");
			String ipLookUp = "http://ip.taobao.com/service/getIpInfo.php?ip=" + clientIp;
			getMethod = new GetMethod(ipLookUp);
			httpClient.executeMethod(getMethod);
			responseBody = getMethod.getResponseBody();
			AddrString = decodeUnicode(new String(responseBody));
			if (StringUtils.isNotBlank(AddrString)) {
				countryValue = StringUtils.substringBetween(AddrString, "\"" + "city" + "\":" + "\"", "\"" + ",");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (getMethod != null)
				getMethod.releaseConnection();
		}

		return countryValue;
		
	}
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
		aChar = theString.charAt(x++);
		if (aChar == '\\') {
		aChar = theString.charAt(x++);
		if (aChar == 'u') {
		int value = 0;
		for (int i = 0; i < 4; i++) {
		aChar = theString.charAt(x++);
		switch (aChar) {
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		value = (value << 4) + aChar - '0';
		break;
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		value = (value << 4) + 10 + aChar - 'a';
		break;
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		value = (value << 4) + 10 + aChar - 'A';
		break;
		default:
		throw new IllegalArgumentException(
		"Malformed encoding.");
		}
		}
		outBuffer.append((char) value);
		} else {
		if (aChar == 't') {
		aChar = '\t';
		} else if (aChar == 'r') {
		aChar = '\r';
		} else if (aChar == 'n') {
		aChar = '\n';
		} else if (aChar == 'f') {
		aChar = '\f';
		}
		outBuffer.append(aChar);
		}
		} else {
		outBuffer.append(aChar);
		}
		}
		return outBuffer.toString();
		}
}
