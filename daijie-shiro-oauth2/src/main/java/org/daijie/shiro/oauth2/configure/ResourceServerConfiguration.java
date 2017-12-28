package org.daijie.shiro.oauth2.configure;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private ShiroOauth2Properties shiroOauth2Properties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
    	if(StringUtils.isNotEmpty(shiroOauth2Properties.getMatchersRole())){
    		for (String definition : shiroOauth2Properties.getMatchersRole().split(",")){
    			if(definition.contains("=")){
					http.authorizeRequests()
			        .antMatchers(definition.split("=")[0]).hasRole(definition.split("=")[1])
			        .anyRequest().permitAll();
				}
    		}
    	}
        http.exceptionHandling()
        .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}
