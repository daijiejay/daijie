package org.daijie.swagger.result;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页列表封装
 * 不建议直接使用这个返回，嵌套到org.daijie.core.result.ModelResult使用
 * @author daijie_jay
 * @since 2017年11月1日
 * @param <T> 列表实体类型
 */
public class PageResult<T> {
	
	@ApiModelProperty(name = "rows", value = "数据集")
	private List<T> rows = new ArrayList<T>();;
	
	@ApiModelProperty(name = "total", value = "总条数")
	private Long total;
	
	public PageResult() {}

	public PageResult(List<T> rows, Long total) {
		super();
		this.total = total;
		this.rows = rows;
	}
	
	public <E> PageResult(List<E> rows, Long total, Class<T> className) {
		super();
		this.total = total;
		setRows(rows, className);
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
	public <E> void setRows(List<E> rows, Class<T> className) {
		if (rows.size() > 0) {
			List<T> list = Lists.transform(rows, 
	    		new Function<E, T>() {
					@Override
					public T apply(E input) {
						T entity = null;
						try {
							entity = className.newInstance();
						} catch (Exception e) {
							e.printStackTrace();
						}
						BeanUtil.copyProperties(input, entity, CopyOptions.create().setIgnoreError(true));
						return entity;
					}
		        });
			this.rows = list;
		}
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public static <T> PageResult<T> of(List<T> rows, Long total){
		PageResult<T> pr = new PageResult<T>();
		pr.setRows(rows);
		pr.setTotal(total);
		return pr;
	}


}
