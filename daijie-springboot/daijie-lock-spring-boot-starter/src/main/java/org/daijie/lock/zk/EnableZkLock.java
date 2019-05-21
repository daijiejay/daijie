package org.daijie.lock.zk;

import org.daijie.lock.LockAspect;
import org.daijie.lock.LockTool;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用zookeeper分布式锁
 * @author daijie_jay
 * @since 2018年1月2日
 */
@Target({ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME)  
@Documented 
@Import({
		ZKLockAutoConfiguration.class,
		LockAspect.class,
		LockTool.class})
@EnableAspectJAutoProxy
public @interface EnableZkLock {

}
