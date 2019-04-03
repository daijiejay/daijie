package org.daijie.core.request;

import org.daijie.core.request.Interceptor.HeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

/**
 * Feign相关配置
 * @author daijie
 * @since 2017年9月2日
 */
@Configuration
public class RequestConfigure {

	/**
	 * 重写RequestInterceptor，实现客服端请求服务到微服务请求头一致
	 * @return RequestInterceptor
	 */
	@Bean
	public RequestInterceptor headerInterceptor() {
		return new HeaderInterceptor();
	}
}
