package org.daijie.shiro.security;

import org.daijie.core.annotation.EnableParametersFilter;
import org.daijie.shiro.annotation.EnableSecurityZuulFilter;
import org.daijie.shiro.annotation.EnableShiro;
import org.daijie.shiro.configure.ShiroConfigure;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableShiro(ShiroConfigure.class)
@EnableSecurityZuulFilter
@EnableParametersFilter
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
@RefreshScope
public class BootApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
