package org.daijie.mybatis.core;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 重写查找数据源
 * @author daijie_jay
 * @since 2017年11月20日
 */
public class ReadWriteSplitRoutingDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getDbType();
    }
}
