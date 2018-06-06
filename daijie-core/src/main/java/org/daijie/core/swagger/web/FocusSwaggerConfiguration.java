package org.daijie.core.swagger.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import(FocusSwaggerController.class)
@EnableConfigurationProperties(ZuulSwaggerProperties.class)
public class FocusSwaggerConfiguration {

	@Bean
	public FocusSwaggerFilter focusSwaggerFilter() {
		return new FocusSwaggerFilter();
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
