package org.daijie.core.factory.proxy;

import java.lang.reflect.Method;

/**
 * 代理方法后执行具体实现
 * @author daijie_jay
 * @since 2018年3月9日
 */
public abstract class AfterHardlerFactory extends AbstractHardleFactory implements AfterFactory {
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = method.invoke(getTargetObject(), args);
		after(proxy, method, args);
		return result;
	}
}
