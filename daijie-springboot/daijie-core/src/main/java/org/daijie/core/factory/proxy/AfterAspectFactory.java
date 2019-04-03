package org.daijie.core.factory.proxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;

/**
 * 定义AOP工厂类，处理方法后具体实现
 * @author daijie_jay
 * @since 2018年3月9日
 */
public interface AfterAspectFactory extends AspectFactory {

	/**
	 * 进入方法之后运行的方法
	 * @param joinPoint 获取方法
	 * @param result 拦截方法的返回参数
	 * @return 对应拦截方法的返回参数
	 * @throws Exception 抛出异常
	 */
	@After(value = "targets()")
	public Object after(JoinPoint joinPoint, Object result) throws Exception;
}
