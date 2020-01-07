package org.daijie.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 声明参数名
 * 如果没有声明，将会默认获取方法参数名，这种获取方式在很多情况下不能够真正获取，包括jdk1.8中新加的Parameter对象也无法获取到
 * @author daijie
 * @since 2019/5/23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {

    /**
     * 声明参数名
     * @return 参数名
     */
    String value();
}
