package org.daijie.social.login.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.social.login.ali.AliLoginTool;
import org.daijie.social.login.config.AliLoginBeanAutoConfiguration;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AliLoginBeanAutoConfiguration.class, AliLoginTool.class})
public @interface EnableAliLogin {

}
