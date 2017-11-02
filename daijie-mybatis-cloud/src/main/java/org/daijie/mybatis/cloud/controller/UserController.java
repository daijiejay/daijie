package org.daijie.mybatis.cloud.controller;

import org.daijie.core.controller.ApiController;
import org.daijie.core.factory.specific.ModelResultInitialFactory.Result;
import org.daijie.core.result.ModelResult;
import org.daijie.mybatis.cloud.service.UserService;
import org.daijie.mybatis.model.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends ApiController<UserService, Exception> {
	
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public ModelResult<User> getUser(@PathVariable(name = "userId") Integer userId){
		return Result.build(service.getUser(userId));
	}
	
	@RequestMapping(value = "/user/username/{userName}", method = RequestMethod.GET)
	public ModelResult<User> getUser(@PathVariable(name = "userName") String userName){
		return Result.build(service.getUserByUserName(userName));
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ModelResult<Boolean> addUser(@RequestBody User user){
		return Result.build(service.addUser(user));
	}
}
