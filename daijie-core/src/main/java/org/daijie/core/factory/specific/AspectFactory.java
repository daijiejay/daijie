package org.daijie.core.factory.specific;

import org.aspectj.lang.JoinPoint;
import org.daijie.core.httpResult.ApiResult;

public interface AspectFactory {

	public void targets();
	
	public void before(JoinPoint jp) throws Exception;
	
	public ApiResult after(Object result) throws Exception;
}
