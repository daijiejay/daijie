package org.daijie.webapp.cloud;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("${feign.daijie-cloud}")
public interface UserCloud {
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public Object getUser();
}
