package org.daijie.jpa.cloud;

import org.daijie.core.swagger.EnableMySwagger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@EntityScan(basePackages={"org.daijie.mybatis.model"})
@EnableMySwagger
@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
public class BootApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
