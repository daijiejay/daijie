package org.daijie.core.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class ParametersFilterBean {

	@Bean
    public FilterRegistrationBean ParametersFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ParametersFilter());
        return registration;
    }
}
