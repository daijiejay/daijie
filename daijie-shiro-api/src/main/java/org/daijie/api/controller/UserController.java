package org.daijie.api.controller;

import org.daijie.api.cloud.UserCloud;
import org.daijie.core.controller.ApiController;
import org.daijie.core.result.ModelResult;
import org.daijie.mybatis.model.User;
import org.daijie.shiro.authc.Auth;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends ApiController<UserCloud, Exception> {
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelResult<User> getUser(){
		User user = (User) Auth.getAuthc("user");
		return service.getUser(user.getUserId());
	}

	@RequestMapping(value = "/user/username/{userName}", method = RequestMethod.GET)
	public ModelResult<User> getUser(@PathVariable(name = "userName") String userName){
		return service.getUser(userName);
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ModelResult<Boolean> addUser(@RequestBody User user){
		return service.addUser(user);
	}
}
