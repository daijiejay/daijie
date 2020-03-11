package org.daijie.shiro.oauth2.annotation;

import org.daijie.common.FeignConfigure;
import org.daijie.shiro.annotation.EnableShiro;
import org.daijie.shiro.oauth2.configure.JdbcAuthorizationServerConfigurer;
import org.daijie.shiro.oauth2.configure.ResourceServerConfiguration;
import org.daijie.shiro.oauth2.configure.ShiroAuthenticationConfigure;
import org.daijie.shiro.oauth2.configure.WebSecurityConfigurer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * spring加载类注解
 * 集成了shiro+oauth2+zuul
 * @author daijie_jay
 * @since 2017年12月27日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableShiro
@EnableZuulProxy
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
@RefreshScope
@Import({
	ShiroAuthenticationConfigure.class,
	WebSecurityConfigurer.class,
	JdbcAuthorizationServerConfigurer.class,
	ResourceServerConfiguration.class,
	FeignConfigure.class
})
public @interface EnableShiroOauth2SecurityServer {
}
