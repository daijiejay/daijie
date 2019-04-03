package org.daijie.core.factory.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

/**
 * 定义AOP工厂类，处理方法前后具体实现
 * @author daijie_jay
 * @since 2018年3月9日
 */
public interface AroundAspectFactory extends AspectFactory {

	/**
	 * 进入方法前后运行的方法
	 * @param proceedingJoinPoint 获取方法
	 * @return 对应拦截方法的返回参数
	 * @throws Exception 抛出异常
	 */
	@Around(value = "targets()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;
}
