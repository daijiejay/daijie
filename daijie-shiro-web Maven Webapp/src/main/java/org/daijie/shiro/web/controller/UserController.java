package org.daijie.shiro.web.controller;

import org.daijie.core.controller.ApiController;
import org.daijie.shiro.web.cloud.UserCloud;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends ApiController<UserCloud, Exception> {
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public Object getUser(){
		return service.getUser();
	}
}
