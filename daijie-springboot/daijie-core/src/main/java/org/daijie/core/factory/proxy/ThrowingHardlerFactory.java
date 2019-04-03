package org.daijie.core.factory.proxy;

import java.lang.reflect.Method;

/**
 * 代理对象运行时异常具体实现
 * @author daijie_jay
 * @since 2018年3月9日
 */
public abstract class ThrowingHardlerFactory extends AbstractHardleFactory implements ThrowingFactory {
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		try {
			result = method.invoke(getTargetObject(), args);
		} catch (Exception e) {
			throwing(proxy, method, args, e);
		}
		return result;
	}
}
