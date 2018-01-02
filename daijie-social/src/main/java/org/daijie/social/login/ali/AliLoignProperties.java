package org.daijie.social.login.ali;

import org.daijie.social.login.LoginProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 支付宝登录属性配置初始化
 * @author daijie_jay
 * @since 2017年11月28日
 */
@ConfigurationProperties("ali.login")
public class AliLoignProperties extends LoginProperties {
	
    protected String publicKey;

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
}
