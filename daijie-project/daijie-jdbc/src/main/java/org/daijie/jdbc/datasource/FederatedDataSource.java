package org.daijie.jdbc.datasource;

import java.sql.Connection;

/**
 * @author daijie
 * @since 2019/5/23
 */
public class FederatedDataSource extends AbstractPoolDataSource {
    @Override
    public Connection createConnection() {
        return null;
    }
}
