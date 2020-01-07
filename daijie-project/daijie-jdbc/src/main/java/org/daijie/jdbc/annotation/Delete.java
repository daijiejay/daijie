package org.daijie.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 自定义方法声明方法是delete操作
 * @author daijie_jay
 * @since 2018年06月22日
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Delete {

    /**
     * SQL语句
     * @return SQL语句
     */
    String value();
}
