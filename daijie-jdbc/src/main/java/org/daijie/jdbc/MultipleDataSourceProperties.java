package org.daijie.jdbc;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ConfigurationProperties(prefix = "spring.datasource")
@EnableTransactionManagement
public class MultipleDataSourceProperties {
	
	private Class<? extends DataSource> dataSourceType;

	private String names;
	
	private String defaultName;
	
	private String jpaEntityPackages;

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
}
