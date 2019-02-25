package cn.cebest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author 中企高呈
 */
public class SystemConfig implements BeanFactoryAware {

	private static final Logger LOGGER = Logger.getLogger(SystemConfig.class);

	public static List<String> getList(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				list.add(properties.getProperty(key));
			}
		}
		return list;
	}

	public static Set<String> getSet(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptySet();
		}
		Set<String> set = new TreeSet<String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				set.add(properties.getProperty(key));
			}
		}
		return set;
	}

	public static Map<String, String> getMap(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyMap();
		}
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		int len = prefix.length();
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				map.put(key.substring(len), properties.getProperty(key));
			}
		} 
		return map;
	}

	public static Properties getProperties(String prefix) {
		Properties props = new Properties();
		if (properties == null || prefix == null) {
			return props;
		}
		Enumeration<?> en = properties.propertyNames();
		String key;
		int len = prefix.length();
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				props.put(key.substring(len), properties.getProperty(key));
			}
		}
		return props;
	}

	public static String getPropertiesString(String prefix) {
		return getPropertiesString(prefix,"");
	}

	public static String getPropertiesString(String prefix, String defaultStr) {
		String property = defaultStr;
		if (properties == null || prefix == null) {
			return property;
		}

		if (catchMap.containsKey(prefix)) {
			property = catchMap.get(prefix);
		}
		return property;
	}

	public static Map<String, Object> getBeanMap(String prefix) {
		Map<String, String> keyMap = getMap(prefix);
		if (keyMap.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(keyMap.size());
		String key, value;
		for (Map.Entry<String, String> entry : keyMap.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			resultMap.put(key, beanFactory.getBean(value, Object.class));
		}
		return resultMap;
	}

	public static Properties getProperties(File file) {
		Properties props = new Properties();
		InputStream in;
		try {
			in = new FileInputStream(file);
			props.load(in);
		} catch (FileNotFoundException e) {
			LOGGER.error("Don't find the file:" + file.getAbsolutePath(), e);
		} catch (IOException e) {
			LOGGER.error("Access the file hapeend error:" + file.getAbsolutePath(), e);
		}
		return props;
	}

	public static String getPropertyValue(File file, String key) {
		Properties props = getProperties(file);
		return (String) props.get(key);
	}

	private static BeanFactory beanFactory;
	private static Properties properties;
	private static Map<String, String> catchMap = new HashMap<String, String>();

	public void setProperties(Properties properties) {
		SystemConfig.properties = properties;
		Enumeration<?> en = properties.propertyNames();
		String key;
		String value;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			value = properties.getProperty(key);
			catchMap.put(key, value);
			LOGGER.info("加载配置文件缓存：key=" + key + " , value=" + value);
		}
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		SystemConfig.beanFactory = beanFactory;
	}

}
