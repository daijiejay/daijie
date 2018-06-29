package org.daijie.core.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.daijie.core.controller.enums.JSONType;
import org.daijie.core.util.http.HttpConversationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.UrlPathHelper;

import com.fasterxml.jackson.databind.ObjectMapper;

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

	private Map<String, Object> params = new HashMap<String, Object>();

	public BodyReaderHttpServletRequestWrapper(HttpServletRequest request, 
			AbstractHandlerMethodMapping<RequestMappingInfo> objHandlerMethodMapping) throws IOException {
		super(request);
		this.params.putAll(request.getParameterMap());
		String bodyString = HttpConversationUtil.getBodyString();
		if (StringUtils.isEmpty(bodyString)) {
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
			this.body = bodyString.getBytes(Charset.forName("UTF-8"));
			if (request.getContentType().contains("application/json")) {
				String param = new String(this.body, Charset.forName("UTF-8"));
				ObjectMapper mapper = new ObjectMapper();
				if(JSONType.getJSONType(param).equals(JSONType.JSON_TYPE_OBJECT)){
					this.params.putAll(mapper.readValue(param, Map.class));
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
		} else {
			return new String[] { v.toString() };
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

}