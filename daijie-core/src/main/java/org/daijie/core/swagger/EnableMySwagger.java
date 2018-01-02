package org.daijie.core.swagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 重写EnableSwagger注解，改成可配置的文档生成
 * @author daijie_jay
 * @since 2017年12月13日
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE })
@Documented
@Import({SwaggerConfiguration.class})
@EnableSwagger2
public @interface EnableMySwagger {

}
