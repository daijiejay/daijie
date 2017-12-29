package org.daijie.jdbc.interceptor;

import org.daijie.jdbc.DbContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 默认数据源
 * @author daijie_jay
 * @date 2017年11月20日
 */
public class DefaultRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
    	return DbContextHolder.getDataSourceName();
    }
}
