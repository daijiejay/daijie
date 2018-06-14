package org.daijie.core.result;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分页对象
 * @author daijie_jay
 * @since 2018年6月14日
 */
public class Page {

	@ApiModelProperty(name = "pageNumber", value = "页码")
	private int pageNumber;
	
	@ApiModelProperty(name = "pageSize", value = "每页结果数")
	private int pageSize;

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
