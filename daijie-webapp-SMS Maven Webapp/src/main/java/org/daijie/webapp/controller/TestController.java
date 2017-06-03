package org.daijie.webapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(){
		return "欢迎你！";
	}
}
