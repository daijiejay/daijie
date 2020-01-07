package org.daijie.jdbc.annotation;

import java.lang.annotation.*;
/**
 * 自定义方法声明方法是insert操作
 * @author daijie_jay
 * @since 2018年06月22日
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Insert {

    /**
     * 是否忽略null值字段的插入
     * @return
     */
    boolean isSelective() default false;

    /**
     * SQL语句
     * @return SQL语句
     */
    String value();
}
