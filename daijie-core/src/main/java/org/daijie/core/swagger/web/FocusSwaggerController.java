package org.daijie.core.swagger.web;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.daijie.core.result.factory.ModelResultInitialFactory.Result;
import org.daijie.core.swagger.web.ZuulSwaggerProperties.ZuulRoute;
import org.daijie.core.util.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.spring.web.PropertySourcedMapping;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger2.web.Swagger2Controller;

/**
 * 集中式swagger文档资源调用入口
 * @author daijie_jay
 * @since 2018年8月25日
 */
@SuppressWarnings({ "unchecked", "serial" })
@RestController
public class FocusSwaggerController implements Serializable {
	
	protected Logger logger = LoggerFactory.getLogger(FocusSwaggerController.class);

	public static final String SWAGGER_RESOURCES_URL = "/swagger-resources";
	public static final String SWAGGER_RESOURCES_UI_URL = "/swagger-resources/configuration/ui";
	public static final String DEFAULT_URL = "/focus/api-docs";
	public static final String RESOURCES_URL = "/focus-resources";
	public static final String API_DEFAULT_URL = "/api-focus/api-docs";
	public static final String API_RESOURCES_URL = "/api-focus-resources";
	public static final String PARAM = "?group=";
	private static final String HAL_MEDIA_TYPE = "application/hal+json";
	private static final String SPLIT = ".";
	
	private static Map<String, SwaggerCache> swaggerCaches = new HashMap<>();

	private final RestTemplate restTemplate;

	private final ZuulSwaggerProperties zuulSwaggerProperties;

	@Autowired
	public FocusSwaggerController(
			RestTemplate restTemplate,
			ZuulSwaggerProperties zuulSwaggerProperties) {
		this.restTemplate = restTemplate;
		this.zuulSwaggerProperties = zuulSwaggerProperties;
	}

	@RequestMapping(
			value = DEFAULT_URL,
			method = RequestMethod.GET,
			produces = { APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE })
	@PropertySourcedMapping(
			value = "${springfox.documentation.swagger.v2.path}",
			propertyKey = "springfox.documentation.swagger.v2.path")
	public Object getDocumentation(
		      @RequestParam(value = "group", required = false) String swaggerGroup,
		      HttpServletRequest servletRequest) {
		if (!swaggerGroup.contains(SPLIT)) {
			return null;
		}
		String[] split = swaggerGroup.split("\\" + SPLIT);
		if (swaggerCaches.get(split[0]) != null) {
			split[1] = swaggerCaches.get(split[0]).getValue(split[1]);
		}
		Map<String, Object> documentation = null;
		try {
			int request = 0;
			//允许重试3次
			while (request < 3) {
				try {
					documentation = restTemplate.getForObject("http://"+ split[0] + Swagger2Controller.DEFAULT_URL + PARAM + split[1], Map.class);
					request = 3;
				} catch (Exception e) {
					request++;
				}
			}
			if (!zuulSwaggerProperties.getRoutes().isEmpty()) {
				Iterator<Entry<String, ZuulRoute>> iterator = zuulSwaggerProperties.getRoutes().entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, ZuulRoute> next = iterator.next();
					if (split[0].equals(next.getValue().getServiceId())) {
						documentation.put("basePath", "/" + next.getKey());
						documentation.put("host", servletRequest.getServerName() + ":" +servletRequest.getServerPort());
					}
				}
			}
		} catch (Exception e) {
			logger.error("API服务名{}加载文档失败！", swaggerGroup);
			logger.error("", e);
		}
		return documentation;
	}
	
	@RequestMapping(
		      value = RESOURCES_URL,
		      method = RequestMethod.GET,
		      produces = { APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE })
	public Object swaggerResources() {
		Iterator<Entry<String, ZuulRoute>> iterator = zuulSwaggerProperties.getRoutes().entrySet().iterator();
		List<SwaggerResource> list = new ArrayList<SwaggerResource>();
		while (iterator.hasNext()) {
			Entry<String, ZuulRoute> next = iterator.next();
			try {
				List<Map<String, String>> datas = null;
				//初始化资源
				swaggerResourcesUI(next.getValue().getServiceId());
				int request = 0;
				//允许重试3次
				while (request < 3) {
					try {
						datas = restTemplate.getForObject("http://"+ next.getValue().getServiceId() + SWAGGER_RESOURCES_URL, List.class);
						request = 3;
					} catch (Exception e) {
						request++;
					}
				}
				if (datas != null) {
					datas.forEach(data -> {
						String name = null;
						SwaggerResource swaggerResource = new SwaggerResource();
						if (swaggerCaches == null) {
							swaggerCaches = new HashMap<>();
						}
						if (swaggerCaches.get(next.getValue().getServiceId()) == null) {
							name = IdWorker.getDayId();
							SwaggerCache swaggerCache = new SwaggerCache();
							swaggerCache.set(name, data.get("name"));
							swaggerCaches.put(next.getValue().getServiceId(), swaggerCache);
						} else {
							SwaggerCache swaggerCache = swaggerCaches.get(next.getValue().getServiceId());
							name = swaggerCache.getKey(data.get("name"));
							if (name == null) {
								name = IdWorker.getDayId();
								swaggerCache.set(name, data.get("name"));
							}
						}
						
						String location = null;
						if (data.get("location").contains(PARAM)) {
							location = data.get("location").replace(PARAM, PARAM + next.getValue().getServiceId() + SPLIT);
						} else {
							location = data.get("location") + PARAM + next.getValue().getServiceId() + SPLIT + name;
						}
						if (data.get("url").contains(PARAM)) {
							location = data.get("url").replace(PARAM, PARAM + next.getValue().getServiceId() + SPLIT);
						} else {
							location = data.get("url") + PARAM + next.getValue().getServiceId() + SPLIT + name;
						}
						location = location.replace(location.substring(location.lastIndexOf(".")+1), name);
						swaggerResource.setLocation(location);
						swaggerResource.setSwaggerVersion(data.get("swaggerVersion"));
						swaggerResource.setName(next.getValue().getServiceName() + SPLIT + data.get("name"));
						list.add(swaggerResource);
					});
				}
			} catch (Exception e) {
				logger.error("API服务名{}加载资源失败！", next.getValue().getServiceId());
				logger.error("", e);
			}
		}
		return list;
	}
	
	private Object swaggerResourcesUI(String serviceId) {
		return restTemplate.getForObject("http://"+ serviceId + SWAGGER_RESOURCES_UI_URL, Object.class);
	}

	@RequestMapping(
		      value = API_RESOURCES_URL,
		      method = RequestMethod.GET)
	public Object swaggerResourcesApi() {
		return Result.build(swaggerResources());
	}

	@RequestMapping(
			value = API_DEFAULT_URL,
			method = RequestMethod.GET)
	public Object getDocumentationApi(
		      @RequestParam(value = "group", required = false) String swaggerGroup,
		      HttpServletRequest servletRequest) {
		return Result.build(getDocumentation(swaggerGroup, servletRequest));
	}
}
