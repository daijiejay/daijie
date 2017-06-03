package org.daijie.web.service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.daijie.core.annotation.Access;
import org.daijie.core.controller.enums.AccessType;
import org.daijie.core.factory.specific.AspectFactory;
import org.daijie.core.httpResult.ApiResult;
import org.daijie.web.service.BasicService;

public abstract class AccessServiceAspect<MAPPER> extends BasicService<MAPPER> implements AspectFactory {

	@Override
	@Before("targets()")
	public void before(JoinPoint jp) throws Exception {
		validAccess(jp);
	}

	@Override
	@AfterReturning(returning = "result", pointcut = "targets()")
	public ApiResult after(Object result) throws Exception {
		// TODO Auto-generated method stub	
		return null;
	}

	public Access validAccess(JoinPoint jp) throws Exception {
		Access access = getAccess(jp);
		MySession.initSession();
		AccessType[] accessTypes = access.value();
		for (AccessType accessType : accessTypes) {
			if(accessType.equals(AccessType.NONE)){
				access = null;
			}else if(accessType.equals(AccessType.TOKEN)){
				validSession();
			}
		}
		return access;
	}

	public Access getAccess(JoinPoint jp) throws Exception{
		Access access = ((MethodSignature) jp.getSignature()).getMethod().getAnnotation(Access.class);
		if(access == null){
			access = jp.getTarget().getClass().getAnnotation(Access.class);
		}
		return access;
	}
	
	public void validSession() throws Exception{
		if(session == null){
			throw new Exception("Invalid token, cannot access resources.");
		}
	}
}
