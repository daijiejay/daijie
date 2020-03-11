package org.daijie.shiro.security.annotation;

import org.daijie.shiro.annotation.EnableShiro;
import org.daijie.shiro.security.swagger.FocusSwaggerConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

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
@Import(FocusSwaggerConfiguration.class)
public @interface EnableShiroSecurityServer {
}
