package org.daijie.jdbc.datasource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author daijie
 * @since 2019/5/23
 */
public class SimpleDataSource extends AbstractDataSource {

    public SimpleDataSource(DataSource dataSource) {
        Map<Object, DataSource> targetDataSources = new HashMap<>();
        targetDataSources.put(AbstractDataSource.DATA_SOURCE, dataSource);
        this.setTargetDataSources(targetDataSources);
        this.freshDefaultDataSource(AbstractDataSource.DATA_SOURCE);
    }
}
