package org.daijie.core.httpResult;

import java.io.Serializable;

import org.daijie.core.controller.enums.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 返回实体封装，对modelResult进行代理设置
 * @author daijie
 * @date 2017年10月27日
 * @param <E>
 */
public class ModelResultWrapper<E> implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Logger logger = LoggerFactory.getLogger(ModelResultWrapper.class);

	private ModelResult<E> modelResult = new ModelResult<E>();

	public ModelResultWrapper() {

	}

	public ModelResultWrapper(boolean success) {
		modelResult.setSuccess(success);
	}
	
	public ModelResultWrapper(boolean success, ResultCode code) {
		modelResult.setSuccess(success);
		modelResult.setCode(code.getValue());
		modelResult.setMsg(code.getDescription());
	}
	
	public ModelResultWrapper(boolean success, ResultCode code, String msg) {
		modelResult.setSuccess(success);
		modelResult.setCode(code.getValue());
		modelResult.setMsg(msg);
	}

	public ModelResultWrapper<E> setSuccess(boolean success) {
		modelResult.setSuccess(success);
		return this;
	}

	public ModelResultWrapper<E> setMsg(String msg) {
		modelResult.setMsg(msg);
		return this;
	}
	
	public ModelResultWrapper<E> setCode(ResultCode code) {
		modelResult.setCode(code.getValue());
		return this;
	}

	public ModelResultWrapper<E> setData(E value) {
		modelResult.setData(value);
		return this;
	}

	public ModelResult<E> build() {
		return modelResult;
	}
}
