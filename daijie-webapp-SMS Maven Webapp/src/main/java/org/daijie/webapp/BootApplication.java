package org.daijie.webapp;

import org.daijie.shiro.annotation.ShiroAutoConfiguration;
import org.daijie.web.annotation.SMSConfigurtion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SMSConfigurtion
@ShiroAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
public class BootApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
