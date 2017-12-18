package org.daijie.jdbc.interceptor;

import org.daijie.jdbc.DbContextHolder;
import org.daijie.jdbc.MultipleDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

/**
 * 默认数据源
 * @author daijie_jay
 * @date 2017年11月20日
 */
public class DefaultRoutingDataSource extends AbstractRoutingDataSource {
	
	@Autowired
	private MultipleDataSourceProperties properties;

    @Override
    protected Object determineCurrentLookupKey() {
    	if(StringUtils.isEmpty(properties.getDefaultName())){
    		return DbContextHolder.getDataSourceName();
    	}else{
    		return properties.getDefaultName();
    	}
    }
}
