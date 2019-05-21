package org.daijie.shiro.annotation;

import org.daijie.shiro.configure.ShiroConfigure;
import org.daijie.shiro.kisso.KissoConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 基于spring+shiro框架的集成
 * 注解集成了spring boot，在任何被扫描类加上此注册即可使用
 * @author daijie
 * @since 2017年6月22日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({
	ShiroConfigure.class,
	KissoConfigure.class
	})
public @interface EnableShiro {
}
