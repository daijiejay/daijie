package org.daijie.shiro.oauth2.configure;

import org.daijie.shiro.oauth2.AuthenticationMatch;
import org.daijie.shiro.oauth2.RequestAuthenticationMatch;
import org.daijie.shiro.oauth2.ShiroAuthenticationManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * shiro oauth2权限初始化bean
 * @author daijie_jay
 * @since 2017年12月27日
 */
@EnableConfigurationProperties(ShiroOauth2Properties.class)
@Configuration
public class ShiroAuthenticationConfigure {

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationMatch authenticationMatch){
		ShiroAuthenticationManager authenticationManager = new ShiroAuthenticationManager();
		authenticationManager.setAuthenticationMatch(authenticationMatch);
		return authenticationManager;
	}

	@Bean
	public AuthenticationMatch authenticationMatch(){
		return new RequestAuthenticationMatch();
	}
}
