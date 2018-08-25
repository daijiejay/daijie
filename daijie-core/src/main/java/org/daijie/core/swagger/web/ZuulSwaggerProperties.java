package org.daijie.core.swagger.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 集中式swagger文档配置
 * @author daijie_jay
 * @since 2018年8月25日
 */
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
		
		/**
		 * 拦截路径
		 */
		private String path;

		/**
		 * 微服务名称
		 */
		private String serviceId;

		/**
		 * 微服务别名，用于显示
		 */
		private String serviceName;

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

		public String getServiceName() {
			return serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
}
