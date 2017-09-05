package org.daijie.shiro.security;

import org.daijie.core.annotation.ParametersFilter;
import org.daijie.shiro.annotation.ShiroConfigurtion;
import org.daijie.shiro.configure.ShiroConfigure;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@ShiroConfigurtion(ShiroConfigure.class)
@ParametersFilter
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
public class BootApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
