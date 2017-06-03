package org.daijie.shiro;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.daijie.shiro.authc.AuthorizationToken;
import org.daijie.shiro.authc.UserToken;

public class UserAuthorizingRealm extends AuthorizingRealm {

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
//		String currentUsername = (String) super.getAvailablePrincipal(principals);
		SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo(); 
		Object user = principals.fromRealm(getName()).iterator().next();
		if(user instanceof String){
			simpleAuthorInfo.addRole(null);
		}else{
			List<String> roles = ((UserToken) user).getRoles();
			if(roles != null){
				simpleAuthorInfo.addRoles(roles);
			}
			List<String> permissions = ((UserToken) user).getPermissions();
			if(permissions != null){
				simpleAuthorInfo.addStringPermissions(permissions);
			}
		}
		return simpleAuthorInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		AuthorizationToken authorizationToken = (AuthorizationToken) token;
		String username = (String)token.getPrincipal();
		SimpleAuthenticationInfo authcInfo = null;
        Session session = SecurityUtils.getSubject().getSession();
        if(authorizationToken.getUser() != null){
        	authcInfo = new SimpleAuthenticationInfo(
    				authorizationToken.getUser(), 
    				authorizationToken.getPassword(), 
    				ByteSource.Util.bytes(username), 
    				getName()
    				);
        	session.setAttribute(authorizationToken.getAuthcKey(), authorizationToken.getUser());
        }else{
        	authcInfo = new SimpleAuthenticationInfo(
        			authorizationToken.getUsername(), 
    				authorizationToken.getPassword(), 
    				ByteSource.Util.bytes(username), 
    				getName()
    				);
        	session.setAttribute(authorizationToken.getAuthcKey(), username);
        }
        if(authorizationToken.getSalt() != null){
        	authcInfo.setCredentialsSalt(ByteSource.Util.bytes(authorizationToken.getSalt()));
        }
		return authcInfo;
	}

}
