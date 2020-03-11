package org.daijie.common;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign相关配置
 * @author daijie
 * @since 2017年9月2日
 */
@Configuration
public class FeignConfigure {

	/**
	 * 重写RequestInterceptor，实现客服端请求服务到微服务请求头一致
	 * @return RequestInterceptor
	 */
	@Bean
	public RequestInterceptor headerInterceptor() {
		return new HeaderInterceptor();
	}
}
