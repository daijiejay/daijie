package org.daijie.shiro.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.shiro.configure.ShiroConfigure;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.core.annotation.AliasFor;

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

	@AliasFor(annotation = EnableShiro.class, attribute = "value")
	Class<?>[] value() default {ShiroConfigure.class};
}
