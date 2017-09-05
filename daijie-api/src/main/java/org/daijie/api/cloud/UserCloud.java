package org.daijie.api.cloud;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="${feign.mybatis-cloud}")
public interface UserCloud {
	
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public Object getUser(@RequestParam(name = "userId") Integer userId);
}
