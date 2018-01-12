package org.daijie.jdbc.interceptor;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.daijie.core.result.factory.AspectFactory;
import org.daijie.jdbc.DbContextHolder;
import org.daijie.jdbc.annotation.SelectDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * aop拦截初始化数据源
 * @author daijie_jay
 * @since 2017年11月20日
 */
@Aspect
@Component
public class SelectDataSourceInterceptor implements Ordered, AspectFactory {

    public static final Logger logger = LoggerFactory.getLogger(SelectDataSourceInterceptor.class);

    @Override
    @Around("@within(org.daijie.jdbc.annotation.SelectDataSource)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            logger.info("set database connection to read only");
            Signature signature = proceedingJoinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method targetMethod = methodSignature.getMethod();
            Class<?> targetClass = methodSignature.getDeclaringType();
            if (targetMethod.isAnnotationPresent(SelectDataSource.class)) {
            	SelectDataSource selectDataSource = targetMethod.getAnnotation(SelectDataSource.class);
            	DbContextHolder.setDataSourceName(selectDataSource.name());
            }else if(targetClass.isAnnotationPresent(SelectDataSource.class)){
            	SelectDataSource selectDataSource = (SelectDataSource) targetClass.getAnnotation(SelectDataSource.class);
            	DbContextHolder.setDataSourceName(selectDataSource.name());
            }
            Object result = proceedingJoinPoint.proceed();
            return result;
        }finally {
            DbContextHolder.clearDataSourceName();
            logger.info("restore database connection");
        }
    }


    @Override
    public int getOrder() {
        return 0;
    }


	@Override
	public void targets() {
	}


	@Override
	public void before(JoinPoint jp) throws Exception {
	}


	@Override
	public Object after(Object result) throws Exception {
		return null;
	}
}
