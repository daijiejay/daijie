package org.daijie.lock;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.daijie.common.aspect.BeforeAspectFactory;
import org.daijie.lock.Exception.LockException;
import org.daijie.lock.Exception.LockTimeOutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

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
		logger.debug("执行方法{}之前，正在尝试获取锁！", method.toGenericString());
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
		tryLock(lockId, lock);
	}

	@Override
	public Object throwing(JoinPoint joinPoint, Exception exception) {
		return null;
	}

	/**
	 * 获取锁
	 * @param lockId 锁业务ID
	 * @param lock @Lock注解
	 */
	private void tryLock(String lockId, Lock lock){
		LockTool.execute(lockId, lock.timeout(), new Callback() {
			@Override
			public Object onTimeout() throws InterruptedException {
				String timeOutMethodName = lock.timeOutMethodName();
				if(timeOutMethodName != null){
					new Runnable() {
						@Override
						public void run() {
							execute(timeOutMethodName);
						}
					}.run();
				}
				throw new LockTimeOutException("获取锁超时，当前设置超时时间为" + lock.timeout() + "ms");
			}
			@Override
			public Object onGetLock() throws InterruptedException {
				return true;
			}
			@Override
			public Object onError(Exception exception){
				String errorMethodName = lock.errorMethodName();
				if(errorMethodName != null){
					new Runnable() {
						@Override
						public void run() {
							execute(errorMethodName);
						}
					}.run();
				}
				throw new LockException("获取锁异常", exception);
			}
		});
	}

	/**
	 * 获取锁异常时执行的方法
	 * @param serviceName 方法名
	 */
	private void execute(String serviceName){
		String str1 = serviceName.substring(0, serviceName.indexOf("("));
		String className = str1.substring(0, str1.lastIndexOf("."));
		String methodName = str1.substring(str1.lastIndexOf(".")+1);
		String args = serviceName.substring(serviceName.lastIndexOf("(")+1, serviceName.indexOf(")"));
		try {
			Class<?> clz = Class.forName(className);
			if(StringUtils.isEmpty(args)){
				Method method = clz.getDeclaredMethod(methodName);
				method.invoke(clz.newInstance());
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
				method.invoke(clz.newInstance(), argObjects);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
