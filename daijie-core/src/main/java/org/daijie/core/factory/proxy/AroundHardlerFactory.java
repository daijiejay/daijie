package org.daijie.core.factory.proxy;

import java.lang.reflect.Method;

/**
 * 代理对象方法前后具体实现
 * @author daijie_jay
 * @since 2018年3月9日
 */
public abstract class AroundHardlerFactory extends AbstractHardleFactory implements BeforeFactory, AfterFactory {

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		before(proxy, method, args);
		Object result = method.invoke(getTargetObject(), args);
		after(proxy, method, args);
		return result;
	}
}
