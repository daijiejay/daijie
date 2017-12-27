package org.daijie.shiro.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.shiro.security.configure.SecurityZuulFilterBean;
import org.springframework.context.annotation.Import;

/**
 * 开启过滤相关权限，暂时用不到
 * @author daijie_jay
 * @date 2017年12月27日
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SecurityZuulFilterBean.class)
public @interface EnableSecurityZuulFilter {

}
