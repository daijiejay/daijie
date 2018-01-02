package org.daijie.shiro.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.shiro.annotation.EnableShiro;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * spring加载类注解
 * 此注解只是在@EnableShiro的基础上集成了zuul
 * 利用了shiro的权限拦截机制，过滤之后由zuul路由设置跳转到对应的接口服务
 * @author daijie_jay
 * @since 2017年12月27日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableShiro
@EnableSecurityZuulFilter
@EnableZuulProxy
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
@RefreshScope
public @interface EnableShiroSecurityServer {
}
