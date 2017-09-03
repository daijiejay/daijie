package org.daijie.shiro.security.cloud.middleware;

import org.daijie.shiro.feign.ShiroInterceptorConfigure;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="${feign.api}",configuration=ShiroInterceptorConfigure.class)
public interface ApiCloud {

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Object excute();
}
