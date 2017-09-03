package org.daijie.shiro.security.controller;

import org.daijie.shiro.security.cloud.middleware.ApiCloud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "")
public class MiddlewareController {
	
	@Autowired
	private ApiCloud api;

	@RequestMapping(value = "api", method = RequestMethod.GET)
	public Object excute(){
		return api.excute();
	}
}
