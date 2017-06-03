package org.daijie.cloud.controller;

import org.daijie.cloud.service.UserService;
import org.daijie.core.controller.ApiController;
import org.daijie.core.httpResult.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController extends ApiController<UserService, Exception> {

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ApiResult home(){
		return Result.build("欢迎", true);
	}
}
