package org.daijie.core.factory.proxy;

import java.lang.reflect.Method;

/**
 * 代理对象方法后执行
 * @author daijie_jay
 * @since 2018年3月9日
 */
public interface AfterFactory {

	/**
	 * 代理对象方法后执行的方法
	 * @param proxy 代理对象
	 * @param method 代理对象方法
	 * @param args 参数集
	 * @return Object 对应代理对象方法返回值
	 */
	public Object after(Object proxy, Method method, Object[] args);
}
