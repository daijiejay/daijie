package org.daijie.shiro.oauth2.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.StringUtils;


/**
 * oauth授权配置
 * 未认证时自动跳转登录页
 * @author daijie_jay
 * @since 2017年12月22日
 */
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ShiroOauth2Properties shiroOauth2Properties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	if (StringUtils.isEmpty(shiroOauth2Properties.getLoginPage())) {
    		http.formLogin()
	    		.and()
	    		.requestMatchers().anyRequest()
	    		.and()
	    		.authorizeRequests()
	    		.antMatchers("/oauth/*").permitAll();
    	} else {
        	http.formLogin().loginPage(shiroOauth2Properties.getLoginPage())
        		.and()
        		.requestMatchers().anyRequest()
        		.and()
        		.authorizeRequests()
        		.antMatchers("/oauth/*").permitAll();
    		
    	}
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
