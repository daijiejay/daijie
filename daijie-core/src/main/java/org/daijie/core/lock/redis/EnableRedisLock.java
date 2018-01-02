package org.daijie.core.lock.redis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.core.lock.LockTool;
import org.springframework.context.annotation.Import;

/**
 * 启用redis分布式锁
 * @author daijie_jay
 * @since 2018年1月2日
 */
@Target({ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME)  
@Documented 
@Import({RedisLockAutoConfiguration.class, LockTool.class})
public @interface EnableRedisLock {

}
