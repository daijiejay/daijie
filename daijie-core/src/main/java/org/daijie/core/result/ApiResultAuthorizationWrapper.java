package org.daijie.core.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色权限处理返回数据工具包
 * @author daijie_jay
 * @since 2018年1月2日
 */
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
