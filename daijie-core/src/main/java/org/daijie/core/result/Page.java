package org.daijie.core.result;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分页对象
 * @author daijie_jay
 * @since 2018年6月14日
 */
public abstract class Page implements Serializable {

	private static final long serialVersionUID = 6550659921425449000L;

	@ApiModelProperty(name = "pageNumber", value = "页码")
	private int pageNumber = 1;
	
	@ApiModelProperty(name = "pageSize", value = "每页结果数")
	private int pageSize = 20;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
