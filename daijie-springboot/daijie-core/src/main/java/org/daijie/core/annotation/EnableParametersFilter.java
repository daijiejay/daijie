package org.daijie.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.core.request.RequestConfigure;
import org.daijie.core.filter.ParametersFilterBean;
import org.springframework.context.annotation.Import;

/**
 * 引用这个注解之后，将启动请求body转换成parame形式
 * @author daijie_jay
 * @since 2018年1月1日
 */
@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
	ParametersFilterBean.class, 
	RequestConfigure.class})
public @interface EnableParametersFilter {

}
