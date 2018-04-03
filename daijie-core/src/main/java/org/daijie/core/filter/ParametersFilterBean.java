package org.daijie.core.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 请求头处理类bean注册
 * @author daijie_jay
 * @since 2018年1月2日
 */
public class ParametersFilterBean {

	@Bean
    public FilterRegistrationBean<ParametersFilter> ParametersFilterRegistration() {
        FilterRegistrationBean<ParametersFilter> registration = new FilterRegistrationBean<ParametersFilter>();
        registration.setFilter(new ParametersFilter());
        return registration;
    }
}
