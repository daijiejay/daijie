package org.daijie.shiro.oauth2.configure;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * 访问角色权限配置
 * @author daijie_jay
 * @date 2017年12月22日
 */
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	super.configure(http);
    	http.authorizeRequests()
        .antMatchers("/user/**").hasRole("USER")
        .anyRequest().permitAll()
        .and().csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favor.ico");
    }
}
