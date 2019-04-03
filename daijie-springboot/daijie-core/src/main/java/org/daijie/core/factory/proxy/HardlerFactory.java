package org.daijie.core.factory.proxy;

import java.lang.reflect.Method;

import org.daijie.core.factory.Factory;

/**
 * 处理方法中需要执行指定业务的方法工厂
 * @author daijie_jay
 * @since 2018年3月8日
 */
public interface HardlerFactory extends Factory {

	/**
	 * 执行代理对象方法
	 * @param method 代理对象方法
	 * @param args 代理对象方法参数集
	 * @return Object 返回对应代理对象方法返回值
	 * @throws Exception 抛出异常
	 */
	public Object hardle(Method method, Object[] args) throws Exception;
	
	/**
	 * 设置代理对象
	 * @param targetObject 代理对象
	 */
	public void setTargetObject(Object targetObject);
}
