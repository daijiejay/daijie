package org.daijie.shiro.feign;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.daijie.core.util.SerializeUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

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
				Subject subject = SecurityUtils.getSubject();
				Session session = subject.getSession();
				byte[] serialize = SerializeUtil.serialize(session.getAttribute("user"));
				template.header("reqest-anth", serialize.toString());
				template.method(session.getAttribute("redirect_method").toString());
				template.insert(0, session.getAttribute("redirect_uri").toString());
			}
		};
	}
}
