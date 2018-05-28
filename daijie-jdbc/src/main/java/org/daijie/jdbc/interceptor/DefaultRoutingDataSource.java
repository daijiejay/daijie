package org.daijie.jdbc.interceptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.daijie.jdbc.DbContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 默认数据源
 * @author daijie_jay
 * @since 2017年11月20日
 */
public class DefaultRoutingDataSource extends AbstractRoutingDataSource {
	
	/**
	 * 多数据源集合
	 */
	private Map<Object, DataSource> targetDataSources;

    @Override
    protected Object determineCurrentLookupKey() {
    	return DbContextHolder.getDataSourceName();
    }
    
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
    	super.setTargetDataSources(targetDataSources);
    	Map<Object, DataSource> dataSources = new HashMap<Object, DataSource>();
    	Iterator<Entry<Object, Object>> iterator = targetDataSources.entrySet().iterator();
    	while (iterator.hasNext()) {
    		Entry<Object, Object> entry = iterator.next();
    		dataSources.put(entry.getKey(), (DataSource) entry.getValue());
		}
		this.targetDataSources = dataSources;
	}
    
    public Map<Object, DataSource> getTargetDataSources(){
    	return this.targetDataSources;
    }

    /**
     * 刷新默认数据源
     * @param resolvedDefaultDataSource 默认数据源
     */
	public void freshDefaultDataSource(DataSource resolvedDefaultDataSource) {
		super.setDefaultTargetDataSource(resolvedDefaultDataSource);
		super.afterPropertiesSet();
	}
}
