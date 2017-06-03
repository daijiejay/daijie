package org.daijie.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.core.controller.enums.AccessType;

/**
 * 用于设置调用controller的角色权限
 * @author daijie
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME)  
@Documented 
public @interface Access {

	AccessType[] value();
	
	String[] role() default "";
}
