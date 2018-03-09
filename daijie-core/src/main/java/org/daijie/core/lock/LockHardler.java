package org.daijie.core.lock;

import java.lang.reflect.Method;

import org.daijie.core.factory.proxy.BeforeHardlerFactory;
import org.daijie.core.result.ApiResult;
import org.daijie.core.result.ModelResult;
import org.daijie.core.result.factory.ModelResultInitialFactory.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 代理指定方法加锁
 * @author daijie_jay
 * @since 2018年3月9日
 */
public class LockHardler extends BeforeHardlerFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(LockHardler.class);

	@Override
	public void before(Object proxy, Method method, Object[] args) {
		logger.debug("进入分布式锁，执行方法{}", method.toGenericString());
		Class<?>[] parameterNames = method.getParameterTypes();
		Lock lock = method.getAnnotation(Lock.class);
		if(lock == null){
			lock = proxy.getClass().getAnnotation(Lock.class);
		}
		String lockId = lock.lockId();
		if(StringUtils.isEmpty(lockId)){
			if(!StringUtils.isEmpty(lock.argName())){
				for (int i = 0; i < args.length; i++) {
					if(parameterNames[i].getName().contains(lock.argName())){
						lockId = (String) args[i];
					}
				}
			}else{
				lockId = method.getName();
			}
		}
		ModelResult<Object> locking = tryLock(lockId, lock);
		if(!locking.isSuccess()){
			throw new RuntimeException();
		}
	}

	@Override
	public Object throwing(Object proxy, Method method, Object[] args, Exception exception) {
		Class<?> returnType = method.getReturnType();
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
