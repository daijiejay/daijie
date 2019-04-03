package org.daijie.jdbc;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据源设置
 * 通过aop拦截service，设置contextHolder的数据源名称
 * @author daijie_jay
 * @since 2017年11月20日
 */
public class DbContextHolder {
	
	public static final String DATA_SOURCE = "dataSource";

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSourceName(String name){
        if(name==null)throw new NullPointerException();
        contextHolder.set(name);
    }
    
    public static void setDataSourceName(String name, JdbcTemplate jdbcTemplate, DataSource dataSource){
    	if(name==null)throw new NullPointerException();
    	setDataSourceName(name);
    	jdbcTemplate.setDataSource(dataSource);
    }

    public static String getDataSourceName(){
        return contextHolder.get() == null ? DbContextHolder.DATA_SOURCE:contextHolder.get();
    }

    public static void clearDataSourceName(){
        contextHolder.remove();
    }

}
