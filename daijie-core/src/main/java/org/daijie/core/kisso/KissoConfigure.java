package org.daijie.core.kisso;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.baomidou.kisso.web.interceptor.SSOSpringInterceptor;

/**
 * kisso配置
 * @author daijie_jay
 * @date 2017年11月17日
 */
@ControllerAdvice
@Configuration
public class KissoConfigure extends WebMvcConfigurerAdapter {

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SSOSpringInterceptor()).addPathPatterns("/**").excludePathPatterns("/login");
    }
}
