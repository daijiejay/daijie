package org.daijie.jdbc;

import org.daijie.jdbc.exception.JdbcException;

import java.util.Map;

/**
 * 数据源属性验证工具
 * @author daijie_jay
 * @since 2018年5月28日
 */
public class ValidateJdbcProperty {
	
	/**
	 * 验证数据源属性是否为空且获取属性值，为空则抛出异常
	 * String类型
	 * @param map 数据源属性集
	 * @param key 数据源属性key
	 * @return String
	 */
	public static String validPropertyString(Map<String, Object> map, String key){
		Object value = map.get(key);
		if(value == null || "".equals(value.toString())){
			throw new JdbcException("没有配置数据源属性：" + key);
		}
		return value.toString();
	}
	
	/**
	 * 验证数据源属性是否为空且获取属性值，为空则抛出异常
	 * String类型
	 * @param map 数据源属性集
	 * @param key 数据源属性key
	 * @param dataSourceName 数据据属性名
	 * @return String
	 */
	public static String validPropertyString(Map<String, Object> map, String key, String dataSourceName){
		Object value = map.get(key);
		if(value == null || "".equals(value.toString())){
			throw new JdbcException("没有配置数据源属性：spring.datasource." + dataSourceName +"." + key);
		}
		return value.toString();
	}
	
	/**
	 * 验证数据源属性是否为空且获取属性值，为空则抛出异常
	 * int类型
	 * @param map 数据源属性集
	 * @param key 数据源属性key
	 * @return String
	 */
	public static int validPropertyInt(Map<String, Object> map, String key){
		Object value = map.get(key);
		if(value == null){
			throw new JdbcException("没有配置数据源属性：" + key);
		}
		return (int) value;
	}
	
	/**
	 * 验证数据源属性是否为空且获取属性值，为空则抛出异常
	 * int类型
	 * @param map 数据源属性集
	 * @param key 数据源属性key
	 * @param dataSourceName 数据据属性名
	 * @return String
	 */
	public static int validPropertyInt(Map<String, Object> map, String key, String dataSourceName){
		Object value = map.get(key);
		if(value == null){
			throw new JdbcException("没有配置数据源属性：spring.datasource." + dataSourceName +"." + key);
		}
		return (int) value;
	}
	
	/**
	 * 验证数据源属性是否为空且获取属性值，为空则抛出异常
	 * float类型
	 * @param map 数据源属性集
	 * @param key 数据源属性key
	 * @return String
	 */
	public static float validPropertyFloat(Map<String, Object> map, String key){
		Object value = map.get(key);
		if(value == null){
			throw new JdbcException("没有配置数据源属性：" + key);
		}
		return (float) value;
	}
	
	/**
	 * 验证数据源属性是否为空且获取属性值，为空则抛出异常
	 * float类型
	 * @param map 数据源属性集
	 * @param key 数据源属性key
	 * @param dataSourceName 数据据属性名
	 * @return String
	 */
	public static float validPropertyFloat(Map<String, Object> map, String key, String dataSourceName){
		Object value = map.get(key);
		if(value == null){
			throw new JdbcException("没有配置数据源属性：spring.datasource." + dataSourceName +"." + key);
		}
		return (float) value;
	}
	
	/**
	 * 验证数据源属性是否为空且获取属性值，为空则抛出异常
	 * long类型
	 * @param map 数据源属性集
	 * @param key 数据源属性key
	 * @return String
	 */
	public static long validPropertyLong(Map<String, Object> map, String key){
		Object value = map.get(key);
		if(value == null){
			throw new JdbcException("没有配置数据源属性：" + key);
		}
		return (long) value;
	}
	
	/**
	 * 验证数据源属性是否为空且获取属性值，为空则抛出异常
	 * long类型
	 * @param map 数据源属性集
	 * @param key 数据源属性key
	 * @param dataSourceName 数据据属性名
	 * @return String
	 */
	public static long validPropertyLong(Map<String, Object> map, String key, String dataSourceName){
		Object value = map.get(key);
		if(value == null){
			throw new JdbcException("没有配置数据源属性：spring.datasource." + dataSourceName +"." + key);
		}
		return (long) value;
	}
}
