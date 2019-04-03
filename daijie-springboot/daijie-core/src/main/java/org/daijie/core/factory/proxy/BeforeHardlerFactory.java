package org.daijie.core.factory.proxy;

import java.lang.reflect.Method;

/**
 * 代理方法前执行具体实现
 * @author daijie_jay
 * @since 2018年3月9日
 */
public abstract class BeforeHardlerFactory extends AbstractHardleFactory implements BeforeFactory, ThrowingFactory {
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		try {
			before(proxy, method, args);
			result = hardle(method, args);
		} catch (Exception e) {
			throwing(proxy, method, args, e);
		}
		return result;
	}
}
