package org.daijie.jdbc.transaction;

import org.daijie.jdbc.datasource.DataSourceManage;

/**
 * 事务管理
 * @author daijie
 * @since 2019/6/3
 */
public class TransactionManage {

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
            if (TransactionManage.holder.get().isExistsTransaction()) {
                return TransactionManage.holder.get().getTransaction();
            }
            return TransactionManage.holder.get().setTransaction(getTransaction());
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
        if (TransactionManage.holder.get() == null) {
            TransactionManage.holder.set(new TransactionInfo());
        }
        return TransactionManage.holder.get();
    }

    /**
     * 更新事务状态
     * @param type 事务状态
     */
    protected static void changeTransactionInfo(StatusType type) {
        TransactionManage.holder.get().setSuccessStatus(type);
    }

    /**
     * 清除当前线程事务信息
     */
    protected static void removeTransactionInfo() {
        if (TransactionManage.holder.get() != null) {
            TransactionManage.holder.remove();
        }
    }

    /**
     * 是否开发事务
     * @return 返回布尔值
     */
    public static boolean isTransaction() {
        return TransactionManage.holder.get() != null;
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
