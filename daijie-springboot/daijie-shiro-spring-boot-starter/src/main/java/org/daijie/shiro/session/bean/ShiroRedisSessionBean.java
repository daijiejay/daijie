package org.daijie.shiro.session.bean;

import org.daijie.shiro.ShiroSecurity;
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
	public ShiroRedisSession initShiroRedisSession(ShiroSecurity shiroSecurity){
		return new ShiroRedisSession(shiroSecurity.getCookieName(), shiroSecurity.getSessionTimeOut());
	}
	
	@Bean
	public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy(){
	    FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<DelegatingFilterProxy>();
	    DelegatingFilterProxy proxy = new DelegatingFilterProxy();
	    proxy.setTargetFilterLifecycle(true);
	    proxy.setTargetBeanName("shiroFilter");
	    filterRegistrationBean.setFilter(proxy);
	    return filterRegistrationBean;
	}
}
