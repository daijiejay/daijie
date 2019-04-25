package org.daijie.shiro.oauth2.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.shiro.annotation.EnableShiro;
import org.daijie.shiro.oauth2.configure.JdbcAuthorizationServerConfigurer;
import org.daijie.shiro.oauth2.configure.ResourceServerConfiguration;
import org.daijie.shiro.oauth2.configure.ShiroAuthenticationConfigure;
import org.daijie.shiro.oauth2.configure.WebSecurityConfigurer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;

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
@RefreshScope
@Import({
	ShiroAuthenticationConfigure.class,
	WebSecurityConfigurer.class,
	JdbcAuthorizationServerConfigurer.class,
	ResourceServerConfiguration.class
})
public @interface EnableShiroOauth2SecurityServer {
}
