package org.daijie.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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

@Api(description="用户管理")
@RestController
public class UserController extends ApiController<UserCloud, Exception> {
	
	@ApiOperation(notes = "获取当前登录用户信息", value = "获取当前登录用户信息")
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelResult<User> getUser(){
		User user = (User) Auth.getAuthc("user");
		return service.getUser(user.getUserId());
	}

	@ApiOperation(notes = "根据用户名获取用户信息", value = "根据用户名获取用户信息")
	@RequestMapping(value = "/user/username/{userName}", method = RequestMethod.GET)
	public ModelResult<User> getUser(@PathVariable(name = "userName") String userName){
		return service.getUser(userName);
	}

	@ApiOperation(notes = "添加用户", value = "添加用户")
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ModelResult<Boolean> addUser(@RequestBody User user){
		return service.addUser(user);
	}
}
