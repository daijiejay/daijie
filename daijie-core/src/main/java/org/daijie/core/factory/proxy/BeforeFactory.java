package org.daijie.core.factory.proxy;

import java.lang.reflect.Method;

/**
 * 代理对象方法前
 * @author daijie_jay
 * @since 2018年3月9日
 */
public interface BeforeFactory {

	/**
	 * 代理对象方法前执行的方法
	 * @param proxy 代理对象
	 * @param method 代理对象方法
	 * @param args 参数集
	 */
	public void before(Object proxy, Method method, Object[] args);
}
