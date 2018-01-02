package org.daijie.mybatis.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.daijie.mybatis.annotation.ReadOnlyConnection;
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
public class ReadOnlyConnectionInterceptor implements Ordered {

    public static final Logger logger = LoggerFactory.getLogger(ReadOnlyConnectionInterceptor.class);

    @Around("@annotation(readOnlyConnection)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, ReadOnlyConnection readOnlyConnection) throws Throwable {
        try {
            logger.info("set database connection to read only");
            DbContextHolder.setDbType(DbContextHolder.DbType.SLAVE);
            Object result = proceedingJoinPoint.proceed();
            return result;
        }finally {
            DbContextHolder.clearDbType();
            logger.info("restore database connection");
        }
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
