package org.daijie.jdbc.generator.config;

/**
 * 数据库基本配置
 * @author daijie
 * @since 2019/9/16
 */
public class DatasourceConfiguration {

    /**
     * 驱动类名
     */
    private String driverClassName;

    /**
     * 数据库地址
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String user;

    /**
     * 数据库密码
     */
    private String password;

    public DatasourceConfiguration(String driverClassName, String url, String user, String password) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
