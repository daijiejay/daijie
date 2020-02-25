package org.daijie.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 初始化swagger配置
 * 动态注册docket bean
 * @author daijie_jay
 * @since 2017年12月13日
 */
@Configuration
public class SwaggerConfiguration extends ApiInfoDocketFactory implements EnvironmentAware {
	
	protected Logger logger = LoggerFactory.getLogger(SwaggerConfiguration.class);

	private Environment environment;
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	@Override
	public void docket(List<SwaggerProperties> properties, BeanDefinitionRegistry registry){
		String[] groupNames = org.springframework.util.StringUtils.tokenizeToStringArray(environment.getProperty("swagger.groupNames"), 
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		if(groupNames == null || groupNames.length == 0){
			try {
				SwaggerProperties swaggerProperties = Binder.get(environment).bind("swagger", SwaggerProperties.class).get();
				properties.add(swaggerProperties);
			} catch (NoSuchElementException e) {
				logger.debug("没有读取到配置swagger属性，使用swagger官方默认配置！");
			}
		}else{
			for (String groupName : groupNames) {
				try {
					SwaggerProperties swaggerProperties = Binder.get(environment).bind("swagger."+groupName, SwaggerProperties.class).get();
					swaggerProperties.setGroupName(groupName);
					properties.add(swaggerProperties);
				} catch (NoSuchElementException e) {
					logger.error("没有读取到组<{}>配置swagger属性！", groupName, e);
					throw e;
				}
			}
		}
	}
}
