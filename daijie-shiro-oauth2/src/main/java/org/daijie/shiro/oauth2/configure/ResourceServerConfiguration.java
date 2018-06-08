package org.daijie.shiro.oauth2.configure;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

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
    	if(!shiroOauth2Properties.getMatcher().isEmpty()){
    		Iterator<Entry<String, List<String>>> iterator = shiroOauth2Properties.getMatcher().entrySet().iterator();
    		while (iterator.hasNext()) {
    			Entry<String, List<String>> next = iterator.next();
    			String matcher = next.getKey().charAt(0) != '_' ? 
    					("/" + next.getKey()).replaceAll("_", "/") : next.getKey().replaceAll("_", "/");
    			http.authorizeRequests()
			        .antMatchers(matcher)
			        .hasAnyRole(next.getValue().toArray(new String[next.getValue().size()]))
			        .anyRequest()
			        .authenticated();
    		}
    	}
        http.exceptionHandling()
        	.accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}
