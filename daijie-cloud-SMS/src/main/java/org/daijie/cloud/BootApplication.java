package org.daijie.cloud;

import org.daijie.mybatis.annotation.IbatisAutoConfiguration;
import org.daijie.shiro.annotation.ShiroAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@IbatisAutoConfiguration(basePackages = {"org.daijie.api.mapper", "org.daijie.api.SMS.mapper"})
@ShiroAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
public class BootApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
