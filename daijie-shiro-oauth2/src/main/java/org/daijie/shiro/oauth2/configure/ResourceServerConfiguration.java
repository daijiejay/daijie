package org.daijie.shiro.oauth2.configure;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * 资源访问配置
 * 读取shiro路径配置
 * @author daijie_jay
 * @since 2018年6月7日
 */
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "default";
	
	@Autowired
	private ShiroOauth2Properties shiroOauth2Properties;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
    	if(StringUtils.isNotEmpty(shiroOauth2Properties.getMatchersRole())){
    		for (String definition : shiroOauth2Properties.getMatchersRole().split(",")){
    			if(definition.contains("=")){
					http.authorizeRequests()
				        .antMatchers(definition.split("=")[0]).hasAnyRole(definition.split("=")[1])
				        .anyRequest().authenticated();
				}
    		}
    	}
        http.exceptionHandling()
        	.accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}
