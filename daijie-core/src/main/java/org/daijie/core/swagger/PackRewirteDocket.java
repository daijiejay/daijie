package org.daijie.core.swagger;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiSelector;

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
