package org.daijie.web.bean;

import org.daijie.web.session.ShiroRedisSession;
import org.springframework.context.annotation.Bean;

public class ShiroRedisSessionBean {
	
	@Bean
	public ShiroRedisSession initShiroRedisSession(){
		return new ShiroRedisSession();
	}
}
