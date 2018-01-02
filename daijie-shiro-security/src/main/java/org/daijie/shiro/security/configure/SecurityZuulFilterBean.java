package org.daijie.shiro.security.configure;

import org.daijie.shiro.security.filter.SecurityZuulFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 初始化shiro zuul加关bean
 * @author daijie_jay
 * @since 2017年12月27日
 */
public class SecurityZuulFilterBean {

	@Bean
	public SecurityZuulFilter securityZuulFilter(){
		return new SecurityZuulFilter();
	}

	@Bean
	@Order(Integer.MAX_VALUE)
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
