package org.daijie.core.swagger.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("zuul")
public class ZuulSwaggerProperties {
	
	private Map<String, ZuulRoute> routes = new LinkedHashMap<>();

	public Map<String, ZuulRoute> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<String, ZuulRoute> routes) {
		this.routes = routes;
	}

	public static class ZuulRoute {
		
		private String id;
		
		private String path;

		private String serviceId;

		private String url;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getServiceId() {
			return serviceId;
		}

		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
}
