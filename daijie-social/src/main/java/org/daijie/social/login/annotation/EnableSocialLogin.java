package org.daijie.social.login.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.core.util.bean.ApplicationContextHolder;
import org.daijie.social.SocialBeanAutoConfiguration;
import org.daijie.social.login.config.SocialLoginBeanAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration({SocialBeanAutoConfiguration.class, SocialLoginBeanAutoConfiguration.class})
@Import({ApplicationContextHolder.class})
public @interface EnableSocialLogin {

}
