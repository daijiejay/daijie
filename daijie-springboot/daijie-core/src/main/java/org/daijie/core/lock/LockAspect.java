package org.daijie.core.lock;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.daijie.core.factory.proxy.BeforeAspectFactory;
import org.daijie.core.result.ApiResult;
import org.daijie.core.result.ModelResult;
import org.daijie.core.result.factory.ModelResultInitialFactory.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 分布式锁注解aop处理
 * 通过org.daijie.core.lock.Lock注解配置在类和方法使用，可以配置锁的超时时间、业务编号
 * @author daijie_jay
 * @since 2018年3月7日
 */
@Aspect
@Component
public class LockAspect implements BeforeAspectFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(LockAspect.class);
	
	@Override
	@Pointcut("@annotation(Lock)") 
	public void targets(){}

	@Override
	public void before(JoinPoint joinPoint) throws Exception {
		MethodSignature methodSignature = ((MethodSignature) joinPoint.getSignature());
		Method method = methodSignature.getMethod();
		logger.debug("进入分布式锁，执行方法{}", method.toGenericString());
		String[] parameterNames = methodSignature.getParameterNames();
		Lock lock = method.getAnnotation(Lock.class);
		if(lock == null){
			lock = joinPoint.getTarget().getClass().getAnnotation(Lock.class);
		}
		String lockId = lock.lockId();
		if(StringUtils.isEmpty(lockId)){
			if(!StringUtils.isEmpty(lock.argName())){
				Object[] args = joinPoint.getArgs();
				for (int i = 0; i < args.length; i++) {
					if(parameterNames[i].contains(lock.argName())){
						lockId = (String) args[i];
					}
				}
			}else{
				lockId = method.getName();
			}
		}
		ModelResult<Object> locking = tryLock(lockId, lock);
		if(!locking.isSuccess()){
			throw new Exception();
		}
	}

	@Override
	public Object throwing(JoinPoint joinPoint, Exception exception) {
		MethodSignature methodSignature = ((MethodSignature) joinPoint.getSignature());
		Class<?> returnType = methodSignature.getReturnType();
		try {
			if(returnType.newInstance() instanceof ModelResult<?>){
				return Result.build(exception.getMessage(), ApiResult.ERROR);
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private ModelResult<Object> tryLock(String lockId, Lock lock){
		return (ModelResult<Object>) LockTool.execute(lockId, lock.timeout(), new Callback() {
			@Override
			public ModelResult<Object> onTimeout() throws InterruptedException {
				String timeOutMethodName = lock.timeOutMethodName();
				if(timeOutMethodName != null){
					return execute(timeOutMethodName);
				}
				return Result.build(ApiResult.ERROR);
			}
			@Override
			public ModelResult<Object> onGetLock() throws InterruptedException {
				return Result.build();
			}
			@Override
			public ModelResult<Object> onError(Exception exception){
				String errorMethodName = lock.errorMethodName();
				if(errorMethodName != null){
					return execute(errorMethodName);
				}
		    	return Result.build(ApiResult.ERROR);
		    }
		});
	}
	
	private ModelResult<Object> execute(String serviceName){
		String str1 = serviceName.substring(0, serviceName.indexOf("("));
		String className = str1.substring(0, str1.lastIndexOf("."));
		String methodName = str1.substring(str1.lastIndexOf(".")+1);
		String args = serviceName.substring(serviceName.lastIndexOf("(")+1, serviceName.indexOf(")"));
		try {
			Object result = null;
			Class<?> clz = Class.forName(className);
			if(StringUtils.isEmpty(args)){
				Method method = clz.getDeclaredMethod(methodName);
				result = method.invoke(clz.newInstance());
			}else{
				String[] argNames = args.split(",");
				Object[] argObjects = new Object[argNames.length];
				Class<?>[] argClasses = new Class[argNames.length];
				for (int i = 0; i < argClasses.length; i++) {
					if(argNames[i].trim().length() > 0){
						argClasses[i] = Class.forName(argNames[i].trim());
						argObjects[i] = argClasses[i].newInstance();
					}
				}
				Method method = clz.getDeclaredMethod(methodName, argClasses);
				result = method.invoke(clz.newInstance(), argObjects);
			}
			return Result.build(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
