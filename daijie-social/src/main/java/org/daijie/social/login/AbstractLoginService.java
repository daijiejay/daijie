package org.daijie.social.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * 三方登录服务接入
 * @author daijie_jay
 * @since 2017年11月29日
 */
public abstract class AbstractLoginService<T extends LoginProperties> implements LoginService {
	
	protected static final String REDIRECT = "redirect:";

	@Autowired
	protected RestTemplate restTemplate;
	
	@Autowired
	protected T properties;
	
	public String getRedirectUrl() {
		return REDIRECT + properties.getRedirectUri();
	}
	
	public String getErrorUrl() {
		return REDIRECT + properties.getErrorUri();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getProperties(){
		return properties;
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
