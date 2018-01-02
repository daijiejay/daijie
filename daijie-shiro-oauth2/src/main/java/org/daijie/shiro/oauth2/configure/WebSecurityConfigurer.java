package org.daijie.shiro.oauth2.configure;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * 访问角色权限配置
 * @author daijie_jay
 * @since 2017年12月22日
 */
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
			.authorizeRequests()
			.antMatchers("/api/user/**").hasRole("USER")
			.anyRequest().authenticated()
		.and()
			.formLogin()
		.and()
			.httpBasic();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favor.ico");
    }
}
