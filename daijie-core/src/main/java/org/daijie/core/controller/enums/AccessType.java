package org.daijie.core.controller.enums;

/**
 * 
 * @author daijie
 * 权限枚举
 * none 任何角色可以 访问
 * token 需要有服务器任何的令牌可以访问
 * login 需要在登录状态下可以访问
 *
 */
public enum AccessType {
	
	NONE,

	TOKEN,
	
	LOGIN
}
