package org.daijie.social.login.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.social.SocialBeanAutoConfiguration;
import org.daijie.social.login.config.QQLoginBeanAutoConfiguration;
import org.daijie.social.login.qq.QQLoginTool;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SocialBeanAutoConfiguration.class, QQLoginBeanAutoConfiguration.class, QQLoginTool.class})
public @interface EnableQQLogin {

}
