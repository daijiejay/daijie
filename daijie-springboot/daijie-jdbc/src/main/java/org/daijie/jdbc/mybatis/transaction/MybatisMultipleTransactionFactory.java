package org.daijie.jdbc.mybatis.transaction;

import javax.sql.DataSource;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;

/**
 * 创建Transaction的工厂类
 * @author daijie_jay
 * @since 2018年5月28日
 */
public class MybatisMultipleTransactionFactory extends SpringManagedTransactionFactory {

	@Override
	public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
		return new MybatisMultipleTransaction(dataSource);
	}

}
