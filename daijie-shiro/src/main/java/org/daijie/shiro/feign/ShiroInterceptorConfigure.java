package org.daijie.shiro.feign;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.daijie.shiro.authc.ShiroConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 集成shiro对请求头处理
 * @author daijie_jay
 * @since 2018年1月2日
 */
@Configuration
public class ShiroInterceptorConfigure {

	@Bean
	public RequestInterceptor shiroInterceptor() {
		return new RequestInterceptor() {
			
			@Override
			public void apply(RequestTemplate template) {
				ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				HttpServletRequest request = attributes.getRequest();
				Enumeration<String> headerNames = request.getHeaderNames();
				if (headerNames != null) {
					while (headerNames.hasMoreElements()) {
						String name = headerNames.nextElement();
						String values = request.getHeader(name);
						template.header(name, values);
					}
				}
				template.method(request.getHeader(ShiroConstants.REDIRECT_METHOD));
				template.insert(0, request.getHeader(ShiroConstants.REDIRECT_URI));
			}
		};
	}
}
