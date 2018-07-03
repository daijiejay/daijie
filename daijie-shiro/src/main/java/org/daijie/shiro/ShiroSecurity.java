package org.daijie.shiro;

import org.daijie.core.kisso.KissoSecurity;
import org.daijie.shiro.configure.ShiroProperties;

public class ShiroSecurity implements KissoSecurity {
	
	private ShiroProperties shiroProperties;
	
	public ShiroSecurity(ShiroProperties shiroProperties) {
		this.shiroProperties = shiroProperties;
	}

	@Override
	public String getCookieName() {
		return shiroProperties.getKissoEnable() ? shiroProperties.getCookieName() : shiroProperties.getSessionid();
	}

	@Override
	public boolean isKissoEnable() {
		return shiroProperties.getKissoEnable();
	}

}
