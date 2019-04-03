package org.daijie.core.swagger;

import java.util.List;

import org.daijie.core.factory.Factory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import springfox.documentation.service.ApiInfo;

/**
 * swagger注册相关bean要执行的方法工厂
 * @author daijie_jay
 * @since 2018年1月2日
 */
public interface DocketFactory extends Factory {

	/**
	 * 注册多个docket bean
	 * 通过List中的上限注册个数
	 * @param properties 多个配置
	 * @param registry 注册bean的工具
	 */
	void docket(List<SwaggerProperties> properties, BeanDefinitionRegistry registry);
	
	/**
	 * 通过配置实例api文档信息内容
	 * @param swaggerProperties 配置
	 * @return ApiInfo
	 */
	ApiInfo apiInfo(SwaggerProperties swaggerProperties);
}
