package org.daijie.shiro.oauth2.configure;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * oauth授权配置
 * 未认证时自动跳转登录页
 * @author daijie_jay
 * @since 2017年12月22日
 */
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.formLogin()
//    	http.formLogin().loginPage("")
    		.and()
    		.requestMatchers().anyRequest()
        	.and()
            .authorizeRequests()
            .antMatchers("/oauth/*").permitAll();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/favor.ico", 
//        		"/swagger-ui.html",
//        		"/webjars/*",
//        		"/swagger-resources/*",
//        		"/*");
//    }
    
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(null).passwordEncoder(new BCryptPasswordEncoder());
//	}
}
