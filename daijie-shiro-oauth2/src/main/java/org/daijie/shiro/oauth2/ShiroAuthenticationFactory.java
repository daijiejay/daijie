package org.daijie.shiro.oauth2;

import org.daijie.core.factory.RegisterBeanFactory;
import org.daijie.shiro.oauth2.excption.ShiroOauth2MatchException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

/**
 * 动态注册bean实现类
 * 自定义实现了org.daijie.shiro.security.oauth2.AuthenticationMatch类
 * 配置shiro.auth2.match.className
 * 由于动态注册bean，实现类里如果有@Autowired类似注入的注解，注入的bean需要先实例
 * @author daijie_jay
 * @date 2017年12月27日
 */
public class ShiroAuthenticationFactory implements RegisterBeanFactory, EnvironmentAware {

	private Environment environment;
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata,
			BeanDefinitionRegistry registry) {
		RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "shiro.auth2.match.");
		String className = propertyResolver.getProperty("className");
		if(StringUtils.isEmpty(className)){
			className = "org.daijie.shiro.security.oauth2.RequestAuthenticationMatch";
		}
		try {
			Class<?> clz = Class.forName(className);
			if(clz.newInstance() instanceof AuthenticationMatch){
				BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ShiroAuthenticationManager.class);
				builder.addPropertyValue("authenticationMatch", clz.newInstance());
				registerBean("authenticationManager", builder, registry);
			} else {
				throw new ShiroOauth2MatchException(clz + "不是" + AuthenticationMatch.class + "子类");
			}
		} catch (Exception e) {
			throw new ShiroOauth2MatchException("配置有误的shiro.auth2.match.className", e);
		}
	}

}
