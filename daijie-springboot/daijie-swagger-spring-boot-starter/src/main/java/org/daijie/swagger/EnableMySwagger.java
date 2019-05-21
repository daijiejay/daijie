package org.daijie.swagger;

import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.*;

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
