package cn.cebest.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SystemConfigTest {

	public static void main(String[] args) {
		//得到Spring配置文件对象
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "classpath*:spring/ApplicationContext-main.xml", "classpath:spring/ApplicationContext-dataSource.xml", "classpath:spring/ApplicationContext-shiro.xml" });
		
		String jdbcurl = SystemConfig.getPropertiesString("jdbc.url", "111");
		System.out.println("jdbc.url=" + jdbcurl);
		String driverClassName = SystemConfig.getPropertiesString("driverClassName");
		System.out.println("driverClassName=" + driverClassName);
	}

}
