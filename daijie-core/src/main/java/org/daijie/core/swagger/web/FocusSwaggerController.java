package org.daijie.core.swagger.web;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.daijie.core.swagger.web.ZuulSwaggerProperties.ZuulRoute;
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

@SuppressWarnings("unchecked")
@RestController
public class FocusSwaggerController {
	
	protected Logger logger = LoggerFactory.getLogger(FocusSwaggerController.class);

	public static final String SWAGGER_RESOURCES_URL = "/swagger-resources";
	public static final String SWAGGER_RESOURCES_UI_URL = "/swagger-resources/configuration/ui";
	public static final String DEFAULT_URL = "/focus/api-docs";
	public static final String RESOURCES_URL = "/focus-resources";
	public static final String PARAM = "?group=";
	private static final String HAL_MEDIA_TYPE = "application/hal+json";
	private static final String SPLIT = ".";

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
						SwaggerResource swaggerResource = new SwaggerResource();
						if (data.get("location").contains(PARAM)) {
							swaggerResource.setLocation(data.get("location").replace(PARAM, PARAM + next.getValue().getServiceId() + SPLIT));
						} else {
							swaggerResource.setLocation(data.get("location") + PARAM + next.getValue().getServiceId() + SPLIT + data.get("name"));
						}
						if (data.get("url").contains(PARAM)) {
							swaggerResource.setLocation(data.get("url").replace(PARAM, PARAM + next.getValue().getServiceId() + SPLIT));
						} else {
							swaggerResource.setLocation(data.get("url") + PARAM + next.getValue().getServiceId() + SPLIT + data.get("name"));
						}
						swaggerResource.setSwaggerVersion(data.get("swaggerVersion"));
						swaggerResource.setName(next.getValue().getServiceId() + SPLIT + data.get("name"));
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
	
	public Object swaggerResourcesUI(String serviceId) {
		return restTemplate.getForObject("http://"+ serviceId + SWAGGER_RESOURCES_UI_URL, Object.class);
	}
}
