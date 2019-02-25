package cn.cebest.util;

import javax.servlet.http.HttpServletResponse;

public class PrintUtil {
	
	/**
	 * 打印文本字符到response
	 */
	public static void printText(HttpServletResponse res,String text) {
		try {
			HttpServletResponse response = RequestUtils.getResponse(res,"text/plain;charset=UTF-8","UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
			response.setDateHeader("Expires",-1);
			response.getWriter().print(text);
		} catch (Exception e) {
			throw new RuntimeException("输出文本数据出错!",e);
		}
	}
	
	/**
	 * 打印文本字符到response
	 */
	public static void printHtml(HttpServletResponse res,String html) {
		try {
			HttpServletResponse response = RequestUtils.getResponse(res,"text/html;charset=UTF-8","UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
			response.setDateHeader("Expires",-1);
			response.getWriter().print(html);
		} catch (Exception e) {
			throw new RuntimeException("输出html数据出错!",e);
		}
	}
	
	/**
	 * 打印json字符到response
	 */
	public static void printJson(HttpServletResponse res,String json) {
		System.out.println(json);
		try {
			HttpServletResponse response = RequestUtils.getResponse(res,"text/json;charset=UTF-8","UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
			response.setDateHeader("Expires",-1);
			response.getWriter().print(json);
		} catch (Exception e) {
			throw new RuntimeException("输出json数据出错!",e);
		}
	}
	
	/**
	 * 打印json字符到response
	 */
	public static void printJson(HttpServletResponse res,Object obj) {
		try {
			String json=JsonUtil.toJson(obj);
			printJson(res,json);
		} catch (Exception e) {
			throw new RuntimeException("输出json数据出错!",e);
		}
	}
	
	/**
	 * 打印JsonResult到response
	 */
	public static void printJsonResult(HttpServletResponse res,int code,String msg) {
		printJson(res,jsonResult(code,msg));
	}
	
	/**
	 * 获取JsonResult对象
	 */
	public static JsonResult jsonResult(int code,String msg) {
		JsonResult result=new JsonResult(code,msg);
		return result;
	}
	
	/**
	 * 打印JsonResult到response
	 */
	public static void printJsonResult(HttpServletResponse res,int code,String msg,Object data) {
		printJson(res,jsonResult(code,msg,data));
	}
	
	/**
	 * 获取JsonResult对象
	 */
	public static JsonResult jsonResult(int code,String msg,Object data) {
		JsonResult result=new JsonResult(code,msg,data);
		return result;
	}
	
}
