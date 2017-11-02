package org.daijie.api.cloud;

import org.daijie.core.result.ModelResult;
import org.daijie.mybatis.model.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="${feign.mybatis-cloud}")
public interface UserCloud {
	
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public ModelResult<User> getUser(@RequestParam(name = "userId") Integer userId);
}
