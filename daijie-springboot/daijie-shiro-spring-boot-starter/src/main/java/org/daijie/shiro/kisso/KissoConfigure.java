package org.daijie.shiro.kisso;

import com.baomidou.kisso.web.interceptor.SSOSpringInterceptor;
import org.daijie.shiro.ShiroSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * kisso配置
 * 去掉kisso默认所有请求拦截
 * @author daijie_jay
 * @since 2017年11月17日
 */
@Configuration
public class KissoConfigure implements WebMvcConfigurer {

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SSOSpringInterceptor()).excludePathPatterns("/**");
    }
}
