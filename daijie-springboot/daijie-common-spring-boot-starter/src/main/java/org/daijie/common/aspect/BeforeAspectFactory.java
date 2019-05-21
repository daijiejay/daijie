package org.daijie.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;

/**
 * 定义AOP工厂类，处理方法前具体实现
 * @author daijie_jay
 * @since 2018年3月9日
 */
public interface BeforeAspectFactory extends AspectFactory {

	/**
	 * 进入方法之前运行的方法
	 * @param joinPoint 获取方法
	 * @throws Exception 抛出异常
	 */
	@Before(value = "targets()")
	public void before(JoinPoint joinPoint) throws Exception;
}
