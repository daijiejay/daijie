package org.daijie.mybatis.cloud;

import org.daijie.mybatis.annotation.IbatisAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@IbatisAutoConfiguration(basePackages = {"org.daijie.mybatis.mapper"})
@SpringBootApplication
@EnableDiscoveryClient
public class BootApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
