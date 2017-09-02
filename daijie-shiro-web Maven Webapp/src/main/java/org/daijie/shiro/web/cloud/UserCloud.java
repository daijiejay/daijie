package org.daijie.shiro.web.cloud;

import org.daijie.core.feign.FeignConfigure;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="${feign.mybatis-cloud}",configuration=FeignConfigure.class)
public interface UserCloud {
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public Object getUser();
}
