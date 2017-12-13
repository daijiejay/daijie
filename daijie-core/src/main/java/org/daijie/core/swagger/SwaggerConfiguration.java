package org.daijie.core.swagger;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 初始化swagger配置
 * @author daijie_jay
 * @date 2017年12月13日
 */
@Configuration
@EnableConfigurationProperties({SwaggerProperties.class})
@EnableSwagger2
public class SwaggerConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Docket createRestApi(SwaggerProperties swaggerProperties) {
		return new Docket(DocumentationType.SWAGGER_2)
		.apiInfo(apiInfo(swaggerProperties))
		.select()
		.apis(RequestHandlerSelectors.basePackage(StringUtils.isNotEmpty(swaggerProperties.getBasePackage())?swaggerProperties.getBasePackage():"org.daijie"))
		.paths(PathSelectors.any())
		.build();
	}
	@SuppressWarnings("deprecation")
	private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
		return new ApiInfoBuilder()
		.title(swaggerProperties.getTitle())
		.description(swaggerProperties.getDescription())
		.termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
		.contact(swaggerProperties.getContact())
		.version(swaggerProperties.getVersion())
		.build();
	}
}
