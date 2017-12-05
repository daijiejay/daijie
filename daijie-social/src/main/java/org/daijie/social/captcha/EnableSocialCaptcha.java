package org.daijie.social.captcha;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImportAutoConfiguration({SocialCaptchaBeanAutoConfiguration.class, SocialCaptchaTool.class})
public @interface EnableSocialCaptcha {

}
