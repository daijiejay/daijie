package org.daijie.social.login.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.daijie.social.SocialBeanAutoConfiguration;
import org.daijie.social.login.baidu.BaiduLoginTool;
import org.daijie.social.login.config.BaiduLoginBeanAutoConfiguration;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SocialBeanAutoConfiguration.class, BaiduLoginBeanAutoConfiguration.class, BaiduLoginTool.class})
public @interface EnableBaiduLogin {

}
