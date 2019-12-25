package org.daijie.jdbc.transaction;

import org.daijie.jdbc.datasource.DataSourceManage;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 当前事务信息
 * @author daijie
 * @since 2019/6/3
 */
public class TransactionInfo {

    private volatile int status = 0;

    private final AtomicLong atomicLong = new AtomicLong();

    private final Class<? extends Transaction> transactionClass;

    /**
     * 当前线程中的所事务集
     */
    private ConcurrentHashMap<String, Transaction> pool = new ConcurrentHashMap<>();

    public TransactionInfo(Class<? extends Transaction> transactionClass) {
        this.transactionClass = transactionClass;
    }

    /**
     * 获取事务管理类
     * @return Transaction 事务管理类
     */
    public Class<? extends Transaction> getTransactionClass() {
        return transactionClass;
    }

    /**
     * 设置事务状态
     * @param type 事务状态
     */
    public void setSuccessStatus(StatusType type) {
        if(type.getStatus() != status) {
            atomicLong.compareAndSet(0, type.getStatus());
        }
    }

    /**
     * 设置事务状态
     * @param type 事务状态
     */
    public void setFailStatus(StatusType type) {
        if(type.getStatus() != status) {
            atomicLong.compareAndSet(1, type.getStatus());
        }
    }

    /**
     * 当前线程添加事务
     * @param transaction 具体事务实现
     * @return 设置事物并返回
     */
    public Transaction setTransaction(Transaction transaction) {
        this.pool.put(DataSourceManage.getDataSourceName(), transaction);
        return transaction;
    }

    /**
     * 获取当前线程中的某个会话事务
     * @return transaction 具体事务实现
     */
    public Transaction getTransaction() {
        return this.pool.get(DataSourceManage.getDataSourceName());
    }

    /**
     * 判断当前会话事务是否已经加入
     * @return boolean 返回布尔值
     */
    public boolean isExistsTransaction() {
        return getTransaction() != null;
    }

    /**
     * 获取当前线程中的已创建的所有会话事务
     * @return Collection 多个具体事务实现
     */
    public Collection<Transaction> getTransactions() {
        return this.pool.values();
    }
}
