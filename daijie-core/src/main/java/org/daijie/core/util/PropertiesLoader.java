package org.daijie.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Properties文件载入工具类. 可载入多个properties文件, 相同的属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先.
 * @author daijie
 * @since 2017年6月5日
 */
public class PropertiesLoader {

	private static Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	private final Properties properties;

	public PropertiesLoader(String... resourcesPaths) {
		properties = loadProperties(resourcesPaths);
	}

	public Properties getProperties() {
		return properties;
	}

	/**
	 * 取出Property，但以System的Property优先.
	 * @param key 键
	 * @return String 值
	 */
	private String getValue(String key) {
		String systemProperty = System.getProperty(key);
		if (systemProperty != null) {
			return systemProperty;
		}
		return properties.getProperty(key);
	}

	/**
	 * 取出String类型的Property，但以System的Property优先,如果都為Null则抛出异常.
	 * @param key 键
	 * @return String 值
	 */
	public String getProperty(String key) {
		String value = getValue(key);
		if (value == null) {
			return null;
		}
		return value;
	}

	/**
	 * 取出String类型的Property，但以System的Property优先.如果都為Null則返回Default值.
	 * @param key 键
	 * @param defaultValue 当为空时默认值
	 * @return String 值
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getValue(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * 取出Integer类型的Property，但以System的Property优先.如果都為Null或内容错误则抛出异常.
	 * @param key 键
	 * @return Integer 值
	 */
	public Integer getInteger(String key) {
		String value = getValue(key);
		if (value == null) {
			return null;
		}
		return Integer.valueOf(value);
	}

	/**
	 * 取出Integer类型的Property，但以System的Property优先.如果都為Null則返回Default值，如果内容错误则抛出异常
	 * @param key 键
	 * @param defaultValue 当为空时默认值
	 * @return Integer 值
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Double类型的Property，但以System的Property优先.如果都為Null或内容错误则抛出异常.
	 * @param key 键
	 * @return Double 值
	 */
	public Double getDouble(String key) {
		String value = getValue(key);
		if (value == null) {
			return null;
		}
		return Double.valueOf(value);
	}

	/**
	 * 取出Double类型的Property，但以System的Property优先.如果都為Null則返回Default值，如果内容错误则抛出异常
	 * @param key 键
	 * @param defaultValue 当为空时默认值
	 * @return Double 值
	 */
	public Double getDouble(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Boolean类型的Property，但以System的Property优先.如果都為Null抛出异常,如果内容不是true/false则返回false.
	 * @param key 键
	 * @return Double 值
	 */
	public Boolean getBoolean(String key) {
		String value = getValue(key);
		if (value == null) {
			return null;
		}
		return Boolean.valueOf(value);
	}

	/**
	 * 取出Boolean类型的Property，但以System的Property优先.如果都為Null則返回Default值,如果内容不为true/false则返回false.
	 * @param key 键
	 * @param defaultValue 当为空时默认值
	 * @return Double 值
	 */
	public Boolean getBoolean(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}

	/**
	 * 载入多个文件, 文件路径使用Spring Resource格式.
	 * @param resourcesPaths 路径
	 * @return Properties 配置
	 */
	private Properties loadProperties(String... resourcesPaths) {
		Properties props = new Properties();
		for (String location : resourcesPaths) {
			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				props.load(is);
			} catch (IOException ex) {
				logger.info("Could not load properties from path:" + location + ", " + ex.getMessage());
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
		return props;
	}
}

