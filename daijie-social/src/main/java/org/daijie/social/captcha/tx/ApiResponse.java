package org.daijie.social.captcha.tx;

import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("rawtypes")
public class ApiResponse {
    private Map<?, ?> header;
    private Object body;

	public ApiResponse() {
        this.header = new TreeMap();
        this.body = "";
    }

    public ApiResponse(Map header, String body) {
        this.header = header;
        this.body = body;
    }

    public void setHeader(Map header) {
        this.header = header;
    }

    public Map getHeader() {
        return this.header;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Object getBody() {
        return this.body;
    }
}
