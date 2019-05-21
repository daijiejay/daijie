package org.daijie.core.factory.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理对象中间类
 * @author daijie_jay
 * @since 2018年3月9日
 */
public abstract class AbstractHardleFactory implements HardlerFactory, InvocationHandler {

	private Object targetObject;
	
	public Object getTargetObject() {
		return targetObject;
	}

	@Override
	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}
	
	@Override
	public Object hardle(Method method, Object[] args) throws Exception{
		return method.invoke(getTargetObject(), args);
	}
}
