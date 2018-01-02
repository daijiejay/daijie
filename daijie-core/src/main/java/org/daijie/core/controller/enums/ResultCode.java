package org.daijie.core.controller.enums;

/**
 * 接口数据返回请求响应代码枚举
 * @author daijie_jay
 * @since 2018年1月1日
 */
public enum ResultCode {

	CODE_100("100", "数据校验失败"),

	CODE_101("101", "请求参数报文格式不对"),
	
	CODE_102("102", "参数错误"),
	
	CODE_103("103", "重复提交"),

	CODE_200("200", "请求成功"),

	CODE_300("300", "用户过期"),
	
	CODE_400("400", "无权限访问"),

	CODE_500("500", "服务器未知异常"),

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
