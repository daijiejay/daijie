package org.daijie.shiro.session.bean;

import org.daijie.shiro.session.ShiroRedisSession;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * shiro redis管理类bean实例
 * @author daijie
 * @since 2017年6月22日
 */
public class ShiroRedisSessionBean {
	
	@Bean
	public ShiroRedisSession initShiroRedisSession(){
		return new ShiroRedisSession();
	}
	
	@Bean
	public FilterRegistrationBean delegatingFilterProxy(){
	    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	    DelegatingFilterProxy proxy = new DelegatingFilterProxy();
	    proxy.setTargetFilterLifecycle(true);
	    proxy.setTargetBeanName("shiroFilter");
	    filterRegistrationBean.setFilter(proxy);
	    return filterRegistrationBean;
	}
}
