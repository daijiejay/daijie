package org.daijie.shiro.security.annotation;

import org.daijie.shiro.security.configure.SecurityZuulFilterBean;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启过滤相关权限，暂时用不到
 * @author daijie_jay
 * @since 2017年12月27日
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SecurityZuulFilterBean.class)
public @interface EnableSecurityZuulFilter {

}
