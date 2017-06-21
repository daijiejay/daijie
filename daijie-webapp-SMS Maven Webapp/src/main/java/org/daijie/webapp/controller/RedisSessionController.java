package org.daijie.webapp.controller;

import org.daijie.core.factory.specific.ApiResultInitialFactory.Result;
import org.daijie.web.session.ShiroRedisSession.Redis;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisSessionController {
	
	@RequestMapping(value = "/setRedis", method = RequestMethod.GET)
	public Object setRedis(String key, String value){
		Redis.set(key, value);
		return Result.build();
	}
	
	@RequestMapping(value = "/getRedis", method = RequestMethod.GET)
	public Object getRedis(String key){
		return Result.addData(key, Redis.get(key)).build();
	}
}
