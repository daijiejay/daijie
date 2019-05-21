package org.daijie.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger属性配置
 * @author daijie_jay
 * @since 2017年12月13日
 */
@ConfigurationProperties("swagger")
public class SwaggerProperties {
	
	/**
	 * 组名
	 */
	private String groupName;

	/**
	 * 标题
	 */
	private String basePackage;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 地址
	 */
	private String termsOfServiceUrl;

	/**
	 * 联系方式
	 */
	private String contact;

	/**
	 * 版本
	 */
	private String version;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTermsOfServiceUrl() {
		return termsOfServiceUrl;
	}

	public void setTermsOfServiceUrl(String termsOfServiceUrl) {
		this.termsOfServiceUrl = termsOfServiceUrl;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
