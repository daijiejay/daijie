package org.daijie.core.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * 开启Controller异常处理
 * @author daijie_jay
 * @since 2018年3月22日
 */
@Target({ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME)  
@Documented 
@Import({ControllerExceptionHandlerResolver.class})
public @interface EnableExceptionHandler {

}
