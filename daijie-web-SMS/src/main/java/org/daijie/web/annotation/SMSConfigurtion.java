package org.daijie.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.core.annotation.ParametersFilter;
import org.daijie.core.util.bean.ApplicationContextHolderBean;
import org.daijie.web.bean.ShiroRedisSessionBean;
import org.springframework.context.annotation.Import;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ParametersFilter
@Import({ShiroRedisSessionBean.class, ApplicationContextHolderBean.class})
public @interface SMSConfigurtion {

}
