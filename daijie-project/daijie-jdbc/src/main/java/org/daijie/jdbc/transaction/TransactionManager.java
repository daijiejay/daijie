package org.daijie.jdbc.transaction;

import org.daijie.jdbc.datasource.DataSourceManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事务管理
 * @author daijie
 * @since 2019/6/3
 */
public class TransactionManager {

    private static final Logger log = LoggerFactory.getLogger(TransactionManager.class);

    /**
     * 当前持有事务信息
     */
    private static ThreadLocal<TransactionInfo> holder = new ThreadLocal<>();

    /**
     * 如果线程没有开启事务管理，则创建新的事务
     * 如果线程已开启事务管理，先从事务池中获取事务，没有则创建新的事务
     * @return 获取具体事务类
     */
    public static Transaction createTransaction() {
        if (isTransaction()) {
            if (TransactionManager.holder.get().isExistsTransaction()) {
                log.debug("获取事务：此次数据源连接已列入当前事务连接池中，直接获取连接");
                return TransactionManager.holder.get().getTransaction();
            }
            log.debug("获取事务：列入一个新的数据源连接到当前事务连接池中，并获取连接");
            return TransactionManager.holder.get().setTransaction(getTransaction());
        }
        return getTransaction();
    }

    /**
     * 根据会话连接中定义的指定数据源创建新的事务
     * @return Transaction 具体事务类
     */
    private static Transaction getTransaction() {
        return new DataSourceTransaction(DataSourceManage.getDataSource());
    }

    /**
     * 开启事务，创建事务信息
     * @return 事务信息
     */
    protected static TransactionInfo createTransactionInfo() {
        if (TransactionManager.holder.get() == null) {
            TransactionManager.holder.set(new TransactionInfo());
        }
        return TransactionManager.holder.get();
    }

    /**
     * 更新事务状态
     * @param type 事务状态
     */
    protected static void changeTransactionInfo(StatusType type) {
        TransactionManager.holder.get().setSuccessStatus(type);
    }

    /**
     * 清除当前线程事务信息
     */
    protected static void removeTransactionInfo() {
        if (TransactionManager.holder.get() != null) {
            TransactionManager.holder.remove();
        }
    }

    /**
     * 是否开发事务
     * @return 返回布尔值
     */
    public static boolean isTransaction() {
        return TransactionManager.holder.get() != null;
    }

    /**
     * 注册事务管理对象
     * @param target 事务管理对象Class
     * @param <T> 事务管理对象
     * @return 事务管理对象
     */
    public static <T> T  registerTransactionManageTarget(Class<?> target) {
        if (!target.isInterface() && target.getInterfaces().length > 0) {
            JDKTransactionInvocationHandler jdkTransactionInvocationHandler = new JDKTransactionInvocationHandler();
            return (T) jdkTransactionInvocationHandler.newProxyInstance(target);
        } else {
            CGLibTransactionInterceptor cgLibTransactionInterceptor = new CGLibTransactionInterceptor();
            return (T) cgLibTransactionInterceptor.newProxyInstance(target);
        }
    }
}
