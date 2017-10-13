package org.daijie.shiro.filter.bean;

import org.daijie.shiro.filter.ShiroRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class ShiroRequestFilterBean {

	@Bean
    public FilterRegistrationBean ParametersFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ShiroRequestFilter());
        return registration;
    }
}
