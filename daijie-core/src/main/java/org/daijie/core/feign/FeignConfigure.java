package org.daijie.core.feign;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Feign相关配置
 * @author daijie
 * @date 2017年9月2日
 */
@Configuration
public class FeignConfigure {

	/**
	 * 重写RequestInterceptor，实现客服端请求服务到微服务请求头一致
	 * @return
	 */
	@Bean
	public RequestInterceptor headerInterceptor() {
		return new RequestInterceptor() {
			@Override
			public void apply(RequestTemplate requestTemplate) {
				ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				HttpServletRequest request = attributes.getRequest();
				Enumeration<String> headerNames = request.getHeaderNames();
				if (headerNames != null) {
					while (headerNames.hasMoreElements()) {
						String name = headerNames.nextElement();
						String values = request.getHeader(name);
						requestTemplate.header(name, values);
					}
				}
			}
		};
	}
}
