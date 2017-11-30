package org.daijie.social.login.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.social.login.config.SinaLoginBeanAutoConfiguration;
import org.daijie.social.login.sina.SinaLoginTool;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SinaLoginBeanAutoConfiguration.class, SinaLoginTool.class})
public @interface EnableSinaLogin {

}
