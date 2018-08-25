package org.daijie.core.swagger.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SwaggerCache implements Serializable {
	
	private static final long serialVersionUID = -2958054701807677218L;
	private Map<String, String> data = new HashMap<>();
	
	public void set(String key, String value) {
		data.put(key, value);
	}
	
	public String getValue(String key) {
		return data.get(key);
	}
	
	public String getKey(String value) {
		for(String getKey: data.keySet()){
			if(data.get(getKey).equals(value)){
				return getKey;
			}
		}
		return null;
	}
}
