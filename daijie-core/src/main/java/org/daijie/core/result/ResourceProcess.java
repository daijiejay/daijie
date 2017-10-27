package org.daijie.core.result;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.ContextLoader;

/**
 * 
 * @author daijie
 * 需要配合AOP使用，调用api接口后对返回结果进行处理
 *
 */
public class ResourceProcess {
	
	static ApiResultAuthorizationWrapper apiResultAuthorizationWrapper;

	static{
		if(apiResultAuthorizationWrapper == null){
			apiResultAuthorizationWrapper =  (ApiResultAuthorizationWrapper) ContextLoader.getCurrentWebApplicationContext().getBean("apiResultAuthorizationWrapper");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void getResource(ApiResult apiResult, HttpServletRequest request) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String result = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1);
		if(apiResultAuthorizationWrapper == null){
			return;
		}
		for (ApiResultAuthorization apiResultAuthorization : apiResultAuthorizationWrapper.getApiResultAuthorizations()) {
			if(apiResultAuthorization.getApiNames().contains(result)){
				List<Object> resultDatas = (List<Object>) apiResult.getData().get(result);
				boolean multiple = true;
				if(resultDatas == null){
					multiple = false;
					resultDatas = new ArrayList<Object>();
					resultDatas.add(apiResult.getData().get(result));
				}
				for (Object resultData : resultDatas) {
					Map<String, Object> data = new HashMap<String, Object>();
					for (String beanData : apiResultAuthorization.getBeanDatas()) {
						Class clz = Class.forName(apiResultAuthorization.getResourceClassName());
						Object obj = resultData;
						Method get = null;
						String[] names = beanData.split("\\.");
						if(names.length > 1){
							for (int i = 0; i < names.length - 1; i++) {
								get = clz.getMethod(generateGetter(names[i]), Object.class);
								obj = get.invoke(obj, Object.class);
								clz = obj.getClass();
							}
							get = clz.getMethod(generateGetter(names[names.length-1]), Object.class);
						}else{
							get = clz.getMethod(generateGetter(names[0]), Object.class);
						}
						if(get.invoke(obj, Object.class) instanceof Date){
							data.put(names[names.length-1], ((Date)get.invoke(obj, Object.class)).getTime());
						}else{
							data.put(names[names.length-1], get.invoke(obj, Object.class));
						}
					}
					list.add(data);
				}
				if(multiple){
					apiResult.getData().put(result, list);
				}else{
					apiResult.getData().put(result, list.get(0));
				}
				break;
			}
		}
	}
	
	public static String generateGetter(String method){
		return "get" + method.replaceFirst(method.substring(0, 1),method.substring(0, 1).toUpperCase());
	}
}
