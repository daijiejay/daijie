package org.daijie.jdbc.datasource;

import org.daijie.jdbc.annotation.SelectDataSource;

import javax.sql.DataSource;

/**
 * @author daijie
 * @since 2019/5/28
 */
public class DataSourceManage {

    private static AbstractDataSource dataSource;

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSource(DataSource dataSource) {
        DataSourceManage.dataSource = (AbstractDataSource) dataSource;
    }

    public static DataSource getDataSource() {
        return DataSourceManage.dataSource.getDataSource(getDataSourceName());
    }

    public static DataSource getDataSource(Class<?> sessionFactoryClass) {
        SelectDataSource selectDataSource = sessionFactoryClass.getAnnotation(SelectDataSource.class);
        if (selectDataSource == null) {
            setDataSourceName(AbstractDataSource.DATA_SOURCE);
        } else {
            setDataSourceName(selectDataSource.name());
        }
        return DataSourceManage.dataSource.getDataSource(getDataSourceName());
    }

    public static void setDataSourceName(String name){
        if(name==null)throw new NullPointerException();
        contextHolder.set(name);
    }

    public static void setDataSourceName(String name, DataSource dataSource){
        if(name==null)throw new NullPointerException();
        setDataSourceName(name);
    }

    public static String getDataSourceName(){
        return contextHolder.get() == null ? AbstractDataSource.DATA_SOURCE:contextHolder.get();
    }

    public static void clearDataSourceName(){
        contextHolder.remove();
    }
}
