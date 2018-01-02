package org.daijie.core.swagger;

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.xiaoleilu.hutool.bean.BeanUtil;

/**
 * 初始化swagger配置
 * 动态注册docket bean
 * @author daijie_jay
 * @since 2017年12月13日
 */
public class SwaggerConfiguration extends ApiInfoDocketFactory implements EnvironmentAware {

	private Environment environment;
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	@Override
	public void docket(List<SwaggerProperties> properties, BeanDefinitionRegistry registry){
		RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "swagger.");
		String[] groupNames = org.springframework.util.StringUtils.tokenizeToStringArray(propertyResolver.getProperty("groupNames"), 
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		if(groupNames == null || groupNames.length == 0){
			SwaggerProperties swaggerProperties = BeanUtil.mapToBean(propertyResolver.getSubProperties(""), 
					SwaggerProperties.class, true);
			swaggerProperties.setGroupName("swaggerDocment");
			properties.add(swaggerProperties);
		}else{
			for (String groupName : groupNames) {
				SwaggerProperties swaggerProperties = BeanUtil.mapToBean(propertyResolver.getSubProperties(groupName + "."), 
						SwaggerProperties.class, true);
				swaggerProperties.setGroupName(groupName);
				properties.add(swaggerProperties);
			}
		}
	}
}
