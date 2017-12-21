package org.daijie.core.swagger;

import java.util.List;

import org.daijie.core.factory.InitialFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import springfox.documentation.service.ApiInfo;

public interface DocketFactory extends InitialFactory {

	void docket(List<SwaggerProperties> properties, BeanDefinitionRegistry registry);
	
	ApiInfo apiInfo(SwaggerProperties swaggerProperties);
	
	public void registerBean(String beanName, BeanDefinitionBuilder builder, BeanDefinitionRegistry registry);
}
