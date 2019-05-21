package org.daijie.lock.redis;

import org.daijie.lock.LockAspect;
import org.daijie.lock.LockTool;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用redis分布式锁
 * @author daijie_jay
 * @since 2018年1月2日
 */
@Target({ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME)  
@Documented 
@Import({
		RedisLockAutoConfiguration.class,
		LockAspect.class,
		LockTool.class})
public @interface EnableRedisLock {

}
