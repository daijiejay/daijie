package org.daijie.shiro.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.daijie.core.controller.ApiController;
import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.httpResult.ApiResult;
import org.daijie.mybatis.model.User;
import org.daijie.shiro.authc.AuthorizationToken;
import org.daijie.shiro.authc.UserToken;
import org.daijie.shiro.session.ShiroRedisSession.Redis;
import org.daijie.shiro.web.cloud.UserCloud;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description="用户登录")
public class LoginController extends ApiController<UserCloud, Exception> {

	@ApiOperation(notes = "登录", value = "登录")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object login(@RequestParam String username, @RequestParam String password){
		UserToken userToken = new UserToken();
		User user = new User();
		user.setId(1);
		user.setNickName("张三");
		userToken.setAuthc(user);
		AuthorizationToken token = new AuthorizationToken(username, password, null, "user", userToken);
		token.setRememberMe(true);  
	    Subject subject = SecurityUtils.getSubject();
	    subject.login(token);
	    subject.isAuthenticated();
		return Result.build("登录成功", ApiResult.SUCCESS, ResultCode.CODE_200);
	}
	
	@ApiOperation(notes = "获取登录用户", value = "获取登录用户")
	@RequestMapping(value = "/getUser", method = RequestMethod.POST)
	public Object getUser(){
		return Result.addData("user", Redis.getAttribute("user")).build();
	}
}
