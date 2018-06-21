package org.daijie.core.filter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 请求头处理类bean注册
 * @author daijie_jay
 * @since 2018年1月2日
 */
@EnableConfigurationProperties(HttpRequestProperties.class)
public class ParametersFilterBean {

	@Bean
    public FilterRegistrationBean<ParametersFilter> ParametersFilterRegistration(HttpRequestProperties requestProperties) {
        FilterRegistrationBean<ParametersFilter> registration = new FilterRegistrationBean<ParametersFilter>();
        registration.setFilter(new ParametersFilter(requestProperties));
        return registration;
    }
}
