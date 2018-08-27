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
	private int pageNumber;
	
	@ApiModelProperty(name = "pageSize", value = "每页结果数")
	private int pageSize;

	@ApiModelProperty(name = "order", value = "排序字段名")
	private String order;

	@ApiModelProperty(name = "sort", value = "排序方式：ASC/DESC")
	private String sort;

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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}
