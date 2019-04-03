package org.daijie.core.swagger;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiSelector;

/**
 * 对重写swagger中的Docket添加set方法，用于实例Docket bean
 * @author daijie_jay
 * @since 2018年1月2日
 */
public class PackRewirteDocket extends RewriteDocket {
	
	public PackRewirteDocket(DocumentationType documentationType) {
		super(documentationType);
	}

	public void setApiInfo(ApiInfo apiInfo){
		this.apiInfo(apiInfo);
	}
	
	public void setGroupName(String groupName){
		this.groupName(groupName);
	}
	
	public void setApiSelector(ApiSelector apiSelector){
		this.selector(apiSelector);
	}
}
