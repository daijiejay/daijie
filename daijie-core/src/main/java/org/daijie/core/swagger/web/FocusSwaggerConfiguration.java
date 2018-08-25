package org.daijie.core.swagger.web;

import org.daijie.core.feign.RestTemplateConfigure;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 集中式swagger文档初始化配置
 * @author daijie_jay
 * @since 2018年8月25日
 */
@Configuration
@Import({FocusSwaggerController.class, RestTemplateConfigure.class})
@EnableConfigurationProperties(ZuulSwaggerProperties.class)
public class FocusSwaggerConfiguration {

	@Bean
	public FocusSwaggerFilter focusSwaggerFilter() {
		return new FocusSwaggerFilter();
	}
}
