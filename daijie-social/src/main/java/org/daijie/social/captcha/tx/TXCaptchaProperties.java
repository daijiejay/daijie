package org.daijie.social.captcha.tx;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 支付宝登录属性配置初始化
 * @author daijie_jay
 * @date 2017年11月28日
 */
@ConfigurationProperties("tx.captcha")
public class TXCaptchaProperties {
	
    private String url;
    
    private String secretId;
    
    private String secretKey;
    
    private String captchaType = "7";
    
    private String disturbLevel = "1";
    
    private String isHttps = "0";
    
    private String clientType = "4";
    
    private String businessId = "1";

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getCaptchaType() {
		return captchaType;
	}

	public void setCaptchaType(String captchaType) {
		this.captchaType = captchaType;
	}

	public String getDisturbLevel() {
		return disturbLevel;
	}

	public void setDisturbLevel(String disturbLevel) {
		this.disturbLevel = disturbLevel;
	}

	public String getIsHttps() {
		return isHttps;
	}

	public void setIsHttps(String isHttps) {
		this.isHttps = isHttps;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
}
