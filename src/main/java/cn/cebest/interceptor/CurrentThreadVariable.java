package cn.cebest.interceptor;

import cn.cebest.entity.web.WebSite;

/**
 * 线程变量
 */
public class CurrentThreadVariable {
	/**
	 * 当前用户线程变量
	 */
	private static ThreadLocal<WebSite> siteVariable = new ThreadLocal<WebSite>();


	/**
	 * 获得当前站点
	 * 
	 * @return
	 */
	public static WebSite getSite() {
		return siteVariable.get();
	}

	/**
	 * 设置当前站点
	 * 
	 * @param site
	 */
	public static void setSite(WebSite site) {
		siteVariable.set(site);
	}

	/**
	 * 移除当前站点
	 */
	public static void removeSite() {
		siteVariable.remove();
	}
}
