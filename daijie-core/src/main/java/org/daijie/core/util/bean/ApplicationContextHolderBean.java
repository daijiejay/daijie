package org.daijie.core.util.bean;

import org.daijie.core.util.ApplicationContextHolder;
import org.springframework.context.annotation.Bean;

/**
 * spring容器bean工具类
 * @author daijie_jay
 * @since 2018年1月2日
 */
public class ApplicationContextHolderBean {

	@Bean
	public ApplicationContextHolder initApplicationContextHolder(){
		return new ApplicationContextHolder();
	}
}
