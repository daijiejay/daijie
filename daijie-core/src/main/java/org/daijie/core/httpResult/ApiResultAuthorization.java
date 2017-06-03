package org.daijie.core.httpResult;

import java.util.ArrayList;
import java.util.List;

public class ApiResultAuthorization {
	
	private String resourceClassName;
	
	private String apiNames = "";
	
	private List<String> beanDatas = new ArrayList<String>();

	public String getResourceClassName() {
		return resourceClassName;
	}

	public void setResourceClassName(String resourceClassName) {
		this.resourceClassName = resourceClassName;
	}

	public String getApiNames() {
		return apiNames;
	}

	public void setApiNames(String apiNames) {
		this.apiNames = apiNames;
	}

	public List<String> getBeanDatas() {
		return beanDatas;
	}

	public void setBeanDatas(List<String> beanDatas) {
		this.beanDatas = beanDatas;
	}

}
