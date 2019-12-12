package org.daijie.jdbc.transaction;

/**
 * 事务注解，用于类或方法上声明需要事务管理，参考spring
 * @author daijie_jay
 * @since 2019年11月10日
 */
public @interface Transactional {

    String value() default "";

    String transactionManager() default "";

    Propagation propagation() default Propagation.REQUIRED;

    Isolation isolation() default Isolation.DEFAULT;

    int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    boolean readOnly() default false;

    Class<? extends Throwable>[] rollbackFor() default {};

    String[] rollbackForClassName() default {};

    Class<? extends Throwable>[] noRollbackFor() default {};

    String[] noRollbackForClassName() default {};
}
