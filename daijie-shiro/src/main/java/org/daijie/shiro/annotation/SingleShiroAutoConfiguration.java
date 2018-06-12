package org.daijie.shiro.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.core.util.bean.ApplicationContextHolderBean;
import org.daijie.shiro.configure.SingleShiroConfigure;
import org.daijie.shiro.session.bean.ShiroRedisSessionBean;
import org.springframework.context.annotation.Import;

/**
 * 此注解配置shiro
 * 注解集成了spring boot，在任何被扫描类加上此注册即可使用
 * 1.1.0版本后已弃用，org.daijie.shiro.configure.ShiroConfigure支持单机和集群配置
 * @author daijie
 * @since 2017年6月22日
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
@Import({SingleShiroConfigure.class, ShiroRedisSessionBean.class, ApplicationContextHolderBean.class})
public @interface SingleShiroAutoConfiguration {

}
