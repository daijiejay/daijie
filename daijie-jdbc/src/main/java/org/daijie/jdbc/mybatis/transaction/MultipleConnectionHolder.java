package org.daijie.jdbc.mybatis.transaction;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.jdbc.datasource.ConnectionHolder;

public class MultipleConnectionHolder extends ConnectionHolder {
	
	private Set<ConnectionHolder> connectionHolders = new HashSet<ConnectionHolder>();

	public MultipleConnectionHolder(Connection connection) {
		super(connection);
	}

	public Set<ConnectionHolder> getConnectionHolders() {
		return connectionHolders;
	}

	public void setConnectionHolders(Set<ConnectionHolder> connectionHolders) {
		this.connectionHolders = connectionHolders;
	}
}