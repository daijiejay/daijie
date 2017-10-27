package org.daijie.core.result;

import java.util.ArrayList;
import java.util.List;

public class ApiResultAuthorizationWrapper {

	private List<ApiResultAuthorization> apiResultAuthorizations = new ArrayList<ApiResultAuthorization>();

	public List<ApiResultAuthorization> getApiResultAuthorizations() {
		return apiResultAuthorizations;
	}

	public void setApiResultAuthorizations(
			List<ApiResultAuthorization> apiResultAuthorizations) {
		this.apiResultAuthorizations = apiResultAuthorizations;
	}
	
	
}
