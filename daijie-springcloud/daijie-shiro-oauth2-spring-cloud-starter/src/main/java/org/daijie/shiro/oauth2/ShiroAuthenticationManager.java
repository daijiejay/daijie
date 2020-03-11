package org.daijie.shiro.oauth2;

import org.daijie.shiro.authc.Auth;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * oauth2用户授权认证的实现类
 * 此类需要通过shiro登录后，得到shiro session中的角色权限
 * @author daijie_jay
 * @since 2017年12月27日
 */
public class ShiroAuthenticationManager implements AuthenticationManager, AuthenticationMatchFactory {
	
	private AuthenticationMatch authenticationMatch;

	@Override
	public void setAuthenticationMatch(AuthenticationMatch authenticationMatch) {
		this.authenticationMatch = authenticationMatch;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		Boolean match = authenticationMatch.match(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
		if(! match){
			throw new UsernameNotFoundException("用户或密码错误");
		}
		String[] authorityList = new String[]{};
		if(!Auth.getPermissions().isEmpty()){
			authorityList = Auth.getPermissions().toArray(authorityList);
			for (int i = 0; i < authorityList.length; i++) {
				authorityList[i] = "ROLE_" + authorityList[i];
			}
		}
		return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), 
				AuthorityUtils.createAuthorityList(authorityList));
	}

}
