package cn.cebest.controller.base;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.session.Session;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.User;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;
import cn.cebest.util.PrintUtil;
import cn.cebest.util.UuidUtil;

/**
 * @author 中企高呈
 * 修改时间：2015、12、11
 */
public class BaseController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 6357869213649815390L;
	
	/** new PageData对象
	 * @return
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**得到ModelAndView
	 * @return
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	/**得到request对象
	 * @return
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	public HttpServletResponse getResponse() {
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		return response;
	}

	/**得到session对象
	 * @return
	 */
	public HttpSession getSession() {
		HttpServletRequest request = getRequest();
		return request.getSession();
	}
	
	/**得到ContextPath
	 * @return
	 */
	public String getContextPath() {
		return this.getRequest().getContextPath();
	}


	/**得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
	/**得到分页列表的信息
	 * @return
	 */
	public Page getPage(){
		return new Page();
	}
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}
	
	protected HttpSession getSession(HttpServletRequest request) {
		return request.getSession();
	}
	
	protected ServletContext getServletContext(HttpServletRequest request) {
		return getSession(request).getServletContext();
	}
	
	
//	/**
//	 * 获取response对象
//	 */
//	protected HttpServletResponse getResponse(String contentType,String encode) {
//		return RequestUtils.getResponse();
//	}
	
	
	
	/**
	 * 打印文本字符到response
	 */
	protected void printText(HttpServletResponse res,String text) {
		PrintUtil.printText(res,text);
	}
	
	/**
	 * 打印文本字符到response
	 */
	protected void printHtml(HttpServletResponse res,String html) {
		PrintUtil.printHtml(res,html);
	}
	
	/**
	 * 打印json字符到response
	 */
	protected void printJson(String json) {
		PrintUtil.printJson(this.getResponse(),json);
	}
	
	/**
	 * 打印json字符到response
	 */
	protected void printJson(Object obj) {
		PrintUtil.printJson(this.getResponse(),obj);
	}
	
	
	/**
	 * 打印JsonResult到response
	 */
	protected void printJsonResult(int code,String msg) {
		PrintUtil.printJsonResult(this.getResponse(),code, msg);
	}
	
	/**
	 * 获取JsonResult对象
	 */
	protected JsonResult jsonResult(int code,String msg) {
		JsonResult result=new JsonResult(code,msg);
		return result;
	}
	
	/**
	 * 打印JsonResult到response
	 */
	protected void printJsonResult(int code,String msg,Object data) {
		printJson(jsonResult(code,msg,data));
	}
	
	/**
	 * 获取JsonResult对象
	 */
	protected JsonResult jsonResult(int code,String msg,Object data) {
		JsonResult result=new JsonResult(code,msg,data);
		return result;
	}
	
	protected User getManageUser(){
		Session session = Jurisdiction.getSession();
		return (User) session.getAttribute(Const.SESSION_USER);
	}
	
	protected String getManageUserId(){
				
		return getManageUser().getUSER_ID();
	}


}
