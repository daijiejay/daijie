package org.daijie.jdbc.transaction;

import javax.annotation.Nullable;
import java.sql.Connection;

/**
 * 事务超时定义，参考spring
 * @author daijie_jay
 * @since 2019年11月10日
 */
public interface TransactionDefinition {

    int PROPAGATION_REQUIRED = 0;

    int PROPAGATION_SUPPORTS = 1;

    int PROPAGATION_MANDATORY = 2;

    int PROPAGATION_REQUIRES_NEW = 3;

    int PROPAGATION_NOT_SUPPORTED = 4;

    int PROPAGATION_NEVER = 5;

    int PROPAGATION_NESTED = 6;

    int ISOLATION_DEFAULT = -1;

    int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;

    int ISOLATION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;

    int ISOLATION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;

    int ISOLATION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;

    int TIMEOUT_DEFAULT = -1;

    int getPropagationBehavior();

    int getIsolationLevel();

    int getTimeout();

    boolean isReadOnly();

    @Nullable
    String getName();

}
