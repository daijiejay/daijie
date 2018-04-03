package org.daijie.shiro.filter.bean;

import org.daijie.shiro.filter.ShiroRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class ShiroRequestFilterBean {

	@Bean
    public FilterRegistrationBean<ShiroRequestFilter> ParametersFilterRegistration() {
        FilterRegistrationBean<ShiroRequestFilter> registration = new FilterRegistrationBean<ShiroRequestFilter>();
        registration.setFilter(new ShiroRequestFilter());
        return registration;
    }
}
