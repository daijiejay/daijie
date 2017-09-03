package org.daijie.shiro.security.cloud;

import org.daijie.shiro.feign.ShiroInterceptorConfigure;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="${feign.mybatis-cloud}",configuration=ShiroInterceptorConfigure.class)
public interface UserCloud {
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public Object getUser();
}
