package org.daijie.jdbc.result;

import java.sql.SQLException;

/**
 * 查询结果对象映射异常
 * @author daijie
 * @since 2019/6/24
 */
public class SqlResultException extends SQLException {

    public SqlResultException(String massage) {
        super(massage);
    }

    public SqlResultException(String massage, Throwable cause) {
        super(massage, cause);
    }
}
