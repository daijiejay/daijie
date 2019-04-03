package org.daijie.core.request.Interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 实现客服端请求服务到微服务请求头一致
 * @author daijie_jay
 * @since 2017年9月3日
 */
public class HeaderInterceptor implements RequestInterceptor {

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
	}

}
