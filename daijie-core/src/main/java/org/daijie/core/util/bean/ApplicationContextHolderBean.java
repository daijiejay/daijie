package org.daijie.core.util.bean;

import org.daijie.core.util.ApplicationContextHolder;
import org.springframework.context.annotation.Bean;

public class ApplicationContextHolderBean {

	@Bean
	public ApplicationContextHolder initApplicationContextHolder(){
		return new ApplicationContextHolder();
	}
}
