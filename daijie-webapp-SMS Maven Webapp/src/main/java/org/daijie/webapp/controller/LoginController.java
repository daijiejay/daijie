package org.daijie.webapp.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.daijie.api.model.User;
import org.daijie.core.controller.ApiController;
import org.daijie.shiro.authc.AuthorizationToken;
import org.daijie.shiro.authc.UserToken;
import org.daijie.webapp.cloud.UserCloud;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController extends ApiController<UserCloud, Exception> {
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object login(){
		UserToken userToken = new UserToken();
		User user = new User();
		user.setId(1);
		user.setNickName("张三");
		userToken.setAuthc(user);
		AuthorizationToken token = new AuthorizationToken("daijie", "123456", null, "user", userToken);
		token.setRememberMe(true);  
	    Subject subject = SecurityUtils.getSubject();
	    subject.login(token);
	    subject.isAuthenticated();
		return service.getUser();
	}
}
