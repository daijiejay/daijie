package org.daijie.workflow.activiti;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * 开启activiti
 * @author daijie_jay
 * @since 2018年5月31日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ActivitiAutoConfigure.class)
public @interface EnableActiviti {
}
