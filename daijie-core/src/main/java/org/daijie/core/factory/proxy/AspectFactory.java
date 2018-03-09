package org.daijie.core.factory.proxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.daijie.core.factory.InitialFactory;

/**
 * 定义AOP工厂类
 * @author daijie_jay
 * @since 2018年1月1日
 */
public interface AspectFactory extends InitialFactory {

	/**
	 * 进入方法需要定义的表达式
	 */
	public void targets();
	
	/**
	 * 进入方法后出现运行异常时运行的方法
	 * @param exception 方法处理时捕获的异常
	 * @return 对应拦截方法的返回参数
	 */
	@AfterThrowing(value = "targets()", throwing = "exception")
	public Object throwing(JoinPoint joinPoint, Exception exception);
}
