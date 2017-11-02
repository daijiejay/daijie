package org.daijie.api.controller;

import org.daijie.api.cloud.UserCloud;
import org.daijie.core.controller.ApiController;
import org.daijie.core.result.ModelResult;
import org.daijie.mybatis.model.User;
import org.daijie.shiro.authc.UserToken;
import org.daijie.shiro.session.ShiroRedisSession.Redis;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends ApiController<UserCloud, Exception> {
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelResult<User> getUser(){
		UserToken userToken = (UserToken) Redis.getAttribute("user");
		User user = (User) userToken.getAuthc();
		return service.getUser(user.getUserId());
	}
}
