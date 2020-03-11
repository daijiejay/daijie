package org.daijie.common.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.daijie.common.result.JSONType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * 将请求参数转换为form-data
 * 支持application/json和restful传参的转换
 * @author daijie
 * @since 2017年6月13日
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private static Logger logger = LoggerFactory.getLogger(BodyReaderHttpServletRequestWrapper.class);

	private final byte[] body;
	
	private final AntPathMatcher matcher = new AntPathMatcher();
	
	private final UrlPathHelper pathHelper = new UrlPathHelper();

	private Map<Object, Object> params = new HashMap<Object, Object>();

	public BodyReaderHttpServletRequestWrapper(HttpServletRequest request,
                                               AbstractHandlerMethodMapping<RequestMappingInfo> objHandlerMethodMapping) throws IOException {
		super(request);
		this.params.putAll(request.getParameterMap());
		String bodyString = SpringHttpUtil.getBodyString();
		if (StringUtils.isEmpty(bodyString) || JSONType.getJSONType(bodyString).equals(JSONType.JSON_TYPE_ERROR)) {
			this.body = new byte[0];
			Collection<RequestMappingInfo> mappings = objHandlerMethodMapping.getHandlerMethods().keySet();
			mappings.forEach(mapping -> {
				RequestMappingInfo requestMappingInfo = mapping.getMatchingCondition(request);
				if (requestMappingInfo != null) {
					String pattern = requestMappingInfo.getPatternsCondition().getPatterns().iterator().next();
					if (pattern.contains("{") && pattern.contains("}")) {
						Map<String, String> result = this.matcher.extractUriTemplateVariables(pattern, this.pathHelper.getLookupPathForRequest(request));
						Iterator<Entry<String, String>> iterator = result.entrySet().iterator();
						while (iterator.hasNext()) {
							Entry<String, String> next = iterator.next();
							if (!(next.getValue().contains("{") && next.getValue().contains("}"))) {
								this.params.put(next.getKey(), next.getValue());
							}
						}
					}
				}
			});
		} else {
			if (request.getMethod().equals(HttpMethod.GET.name())) {
				this.body = new byte[0];
			} else {
				this.body = bodyString.getBytes(Charset.forName("UTF-8"));
			}
			if (request.getContentType().contains("application/json")) {
				ObjectMapper mapper = new ObjectMapper();
				if(JSONType.getJSONType(bodyString).equals(JSONType.JSON_TYPE_OBJECT)){
					this.params.putAll(mapper.readValue(bodyString, Map.class));
				}
			}
		}
		logger.info("params = {}", this.params);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		final ByteArrayInputStream bais = new ByteArrayInputStream(body);

		return new ServletInputStream() {

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public int read() throws IOException {
				// TODO Auto-generated method stub
				// return 0;
				return bais.read();
			}
		};
	}

	@Override
	public String getHeader(String name) {
		return super.getHeader(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return super.getHeaderNames();
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return super.getHeaders(name);
	}

	@Override
	public Map getParameterMap() {
		return params;
	}

	@Override
	public Enumeration getParameterNames() {
		Vector<Object> l = new Vector<Object>(params.keySet());
		return l.elements();
	}

	@Override
	public String[] getParameterValues(String name) {
		Object v = params.get(name);
		if (v == null) {
			return null;
		} else if (v instanceof String[]) {
			return (String[]) v;
		} else if (v instanceof String) {
			return new String[] { (String) v };
		} else if (v instanceof Collection) {
			Object[] array = ((Collection) v).toArray();
			String[] strs = new String[array.length];
			for (int i = 0; i < strs.length; i++) {
				if (array[i] == null) {
					strs[i] = null;
				} else if (array[i] instanceof Map || array[i] instanceof Collection) {
					logger.info("param请求参数{}数组{}只允许是基本数据类型", name, i);
					strs[i] = null;
				} else {
					strs[i] = array[i].toString();
				}
			}
			return strs;
		} else if (v instanceof Map) {
			logger.info("param请求参数属性{}只允许是基本数据类型", name);
			return new String[] {};
		} else {
			return new String[] {v.toString()};
		}
	}

	@Override
	public String getParameter(String name) {
		Object v = params.get(name);
		if (v == null) {
			return null;
		} else if (v instanceof String[]) {
			String[] strArr = (String[]) v;
			if (strArr.length > 0) {
				return strArr[0];
			} else {
				return null;
			}
		} else if (v instanceof String) {
			return (String) v;
		} else {
			return v.toString();
		}
	}

	public String[] generatorObject(Map map) {
		String[] array = new String[map.size()];
		Iterator iterator = map.keySet().iterator();
		int index = 0;
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof Map) {
				generatorObject((Map) next);
			} else {
				array[index] = next.toString();
			}
			index ++;
		}
		return array;
	}
}