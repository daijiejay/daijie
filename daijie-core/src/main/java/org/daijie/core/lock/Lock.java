package org.daijie.core.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 分布式锁，作用于类和方法上的注解，优先方法的注解配置
 * @author daijie_jay
 * @since 2018年3月6日
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Lock {

	/**
	 * 执行访求的锁定时间
	 * 没有设置的情况下默认5秒的超时时间，在指定锁定时间内只能有一个线程运行
	 * @return 锁定时间
	 */
	int timeout() default 5000;
	
	/**
	 * 锁的唯一编号
	 * 可以不需要配置，根据唯一编号限制锁定时间内只能有一个线程运行
	 * @return 锁的唯一编号
	 */
	String lockId() default "";
	
	/**
	 * 锁的唯一名称
	 * 可以不需要配置，在对就的参数变量名中取值作为唯一编号，限制锁定时间内只能有一个线程运行
	 * @return 锁的唯一名称
	 */
	String argName() default "";
	
	/**
	 * 可以不需要配置，锁定时间内需要执行的类方法名称
	 * 比如：java.lang.Object.wait(java.lang.Long)
	 * @return 类方法名
	 */
	String timeOutMethodName() default "";

	/**
	 * 可以不需要配置，获取锁异常时需要执行的类方法名称
	 * 比如：java.lang.Object.wait(java.lang.Long)
	 * @return 类方法名
	 */
	String errorMethodName() default "";
}
