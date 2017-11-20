package org.daijie.mybatis.cloud;

import org.daijie.mybatis.annotation.ClusterIbatisAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@ClusterIbatisAutoConfiguration(basePackages = {"org.daijie.mybatis.mapper"})
@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
public class BootApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
