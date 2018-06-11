package org.daijie.core.swagger.web;

import org.daijie.core.feign.RestTemplateConfigure;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({FocusSwaggerController.class, RestTemplateConfigure.class})
@EnableConfigurationProperties(ZuulSwaggerProperties.class)
public class FocusSwaggerConfiguration {

	@Bean
	public FocusSwaggerFilter focusSwaggerFilter() {
		return new FocusSwaggerFilter();
	}
}
