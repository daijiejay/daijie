package org.daijie.shiro.security.cloud;

import org.daijie.core.result.ModelResult;
import org.daijie.mybatis.model.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="${feign.shiro-api}")
public interface UserCloud {

	@RequestMapping(value = "/user/username/{userName}", method = RequestMethod.GET)
	public ModelResult<User> getUser(@PathVariable(name = "userName") String userName);
	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ModelResult<Boolean> addUser(@RequestBody User user);
}
