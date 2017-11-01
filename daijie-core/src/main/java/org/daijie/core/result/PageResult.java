package org.daijie.core.result;

import java.util.List;

/**
 * 分页列表封装
 * @author daijie_jay
 * @date 2017年11月1日
 * @param <T>
 */
public class PageResult<T> {
	private List<T> rows;
	private Long total;

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public static <T> PageResult<T> of(List<T> rows,Long total){
		PageResult<T> pr = new PageResult<T>();
		pr.setRows(rows);
		pr.setTotal(total);
		return pr;
	}


}
