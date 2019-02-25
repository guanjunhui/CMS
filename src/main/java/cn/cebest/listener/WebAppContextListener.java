package cn.cebest.listener;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.cebest.util.Const;
/**
 * 
* 类名称：WebAppContextListener.java
* 类描述： 
* 作者：qichangxin 
* 联系方式：
* @version 1.0
 */
public class WebAppContextListener implements ServletContextListener {

	
	
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
	}

	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		Const.WEB_APP_CONTEXT = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		ServletContext sc=event.getServletContext();
		initServletContextAttributes(sc);
    	//获取缓存文件的路径
//    	String	cachePath = sc.getRealPath(getCacheFilePath());
//    	EHcacheUtils.EHCACHE_CONFIG_PATH = cachePath;
//    	//初始化缓存配置
//    	EHcacheUtils.init();

	}

	/**
	 * 初始化系统全局属性
	 * @param sc
	 */
	private void initServletContextAttributes(ServletContext sc) {
		sc.setAttribute("contextPath", sc.getContextPath());
		sc.setAttribute("adminPath", sc.getContextPath()+Const.ADMIN_PREFIX);
		sc.setAttribute("memberPath", sc.getContextPath()+Const.MEMBER_PREFIX);
		sc.setAttribute("path", sc.getContextPath());
	}

    /**
     * 获取缓存文件的路径
     * @return
     */
	private String getCacheFilePath() {
		String common = File.separator;
		StringBuffer path = new StringBuffer(common);
		path.append("WEB-INF").append(common).append("classes").append(common).append("ehcache.xml");
		return path.toString();
	}

}
