package org.daijie.core.factory.proxy;

import java.lang.reflect.Proxy;

/**
 * 代理对象工具类
 * @author daijie_jay
 * @since 2018年3月9日
 */
public class ProxyFactory {

	/**
	 * 获取代理对象
	 * @param targetObject 代理对象的具体实现类
	 * @param factory 代理对象中间类
	 * @return Object 代理对象
	 */
	public static Object getProxy(Object targetObject, AbstractHardleFactory factory){
		factory.setTargetObject(targetObject);
		return Proxy.newProxyInstance(targetObject.getClass()
				.getClassLoader(), targetObject.getClass()
				.getInterfaces(), factory);
	}
}
