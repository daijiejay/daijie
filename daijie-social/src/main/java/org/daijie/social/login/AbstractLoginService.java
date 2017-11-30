package org.daijie.social.login;

import javax.annotation.Resource;

import org.springframework.web.client.RestTemplate;

/**
 * 三方登录服务接入
 * @author daijie_jay
 * @date 2017年11月29日
 */
public abstract class AbstractLoginService<T extends LoginProperties> implements LoginService {
	
	protected static final String REDIRECT = "redirect:";

	@Resource
	protected RestTemplate restTemplate;
	
	@Resource
	protected T properties;
	
	public String getRedirectUrl() {
		return REDIRECT + properties.getRedirectUri();
	}
	
	public String getErrorUrl() {
		return REDIRECT + properties.getErrorUri();
	}

	@Override
	public String loadQrcode() {
		return this.loadAuthPage(null);
	}

	@Override	
	public String loadAuthPage() {
		return this.loadAuthPage(null);
	}
}
