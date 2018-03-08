package org.daijie.shiro.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.daijie.core.annotation.Access;
import org.daijie.core.controller.enums.AccessType;
import org.daijie.core.result.ApiResult;
import org.daijie.core.result.factory.AspectFactory;
import org.daijie.shiro.session.ShiroRedisSession.Redis;

/**
 * 基于aop实现访问权限
 * @author daijie
 * @since 2017年6月22日
 */
public abstract class AccessServiceAspect implements AspectFactory {
	
	@Pointcut("@annotation(org.springframework.web.bind.annotation.RestController)") 
	public void targets(){}

	@Override
	@Before("targets()")
	public void before(JoinPoint jp) throws Exception {
		validAccess(jp);
	}

	@Override
	@AfterReturning(returning = "result", pointcut = "targets()")
	public ApiResult after(JoinPoint joinPoint, Object result) throws Exception {
		// TODO Auto-generated method stub	
		return null;
	}

	public Access validAccess(JoinPoint jp) throws Exception {
		Access access = getAccess(jp);
		Redis.initSession();
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
		if(Redis.getSession() == null){
			throw new Exception("Invalid token, cannot access resources.");
		}
	}
}
