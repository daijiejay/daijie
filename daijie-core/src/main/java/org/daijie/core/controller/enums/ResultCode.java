package org.daijie.core.controller.enums;


public enum ResultCode {

	CODE_100("100", "数据校验失败"),

	CODE_101("101", "重复提交"),

	CODE_200("200", "客户端请求已成功"),

	CODE_300("300", "参数错误"),

	CODE_500("500", "服务器异常"),

	;

	private String value;

	private String description;

	private ResultCode(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}
}
