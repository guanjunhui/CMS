package cn.cebest.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring工具类
 */
@Component
public class SpringUtil implements ApplicationContextAware {  
  
    private static ApplicationContext applicationContext;  
  
    /** 
     * 实现ApplicationContextAware接口的回调方法，设置spring上下文环境 
     * @param applicationContext 
     */  
    @Override
	public void setApplicationContext(ApplicationContext applicationContext) {  
        SpringUtil.applicationContext = applicationContext;  
    }  
  
    /**
     * 返回spring上下文环境对象
     * @return
     */
    public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    }
  
    /** 
     * 获取spring bean
     * @param name 
     * @throws BeansException 
     */  
    public static Object getBean(String name) throws BeansException {  
        return applicationContext.getBean(name);  
    }
    
    /**
	 * 获取spring bean
	 * @param clazz
	 * @return
	 */
    public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
}