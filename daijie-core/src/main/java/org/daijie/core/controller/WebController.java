package org.daijie.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.daijie.core.annotation.ErrorMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 
 * @author daijie_jay
 * 程序出现运行时异常时默认跳转“/error”页面
 *
 */
public abstract class WebController implements ExceptionController<Exception>, ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	protected HttpServletRequest request;

	private String errorMappingPath = "error";
	
	@Override
	public Object exceptionHandler(Exception e) {
		return errorMappingPath;
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		String[] beanNames = context.getBeanNamesForType(Object.class);
		for (String beanName : beanNames) {
			ErrorMapping errorMapping = context.findAnnotationOnBean(beanName, ErrorMapping.class);
			if(errorMapping != null && context.getBean(beanName) instanceof WebController){
				if(!"".equals(errorMapping.path())){
					errorMappingPath = errorMapping.path();
				}
			}
		}
	}
}
