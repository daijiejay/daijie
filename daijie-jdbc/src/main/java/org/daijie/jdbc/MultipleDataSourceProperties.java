package org.daijie.jdbc;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 多数据源属性配置
 * @author daijie_jay
 * @since 2018年1月2日
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class MultipleDataSourceProperties {
	
	private Class<? extends DataSource> dataSourceType;

	private String names;
	
	private String defaultName;
	
	private String jpaEntityPackages;
	
	private int transactionTimeout;

	public Class<? extends DataSource> getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(Class<? extends DataSource> dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getDefaultName() {
		return defaultName;
	}

	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}

	public String getJpaEntityPackages() {
		return jpaEntityPackages;
	}

	public void setJpaEntityPackages(String jpaEntityPackages) {
		this.jpaEntityPackages = jpaEntityPackages;
	}

	public int getTransactionTimeout() {
		return transactionTimeout;
	}

	public void setTransactionTimeout(int transactionTimeout) {
		this.transactionTimeout = transactionTimeout;
	}
}
