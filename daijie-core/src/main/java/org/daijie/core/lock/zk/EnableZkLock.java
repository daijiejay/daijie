package org.daijie.core.lock.zk;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.core.lock.LockTool;
import org.springframework.context.annotation.Import;

/**
 * 启用zookeeper分布式锁
 * @author daijie_jay
 * @since 2018年1月2日
 */
@Target({ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME)  
@Documented 
@Import({ZKLockAutoConfiguration.class, LockTool.class})
public @interface EnableZkLock {

}
