package org.daijie.jdbc.mybatis.example;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.daijie.core.result.Page;
import org.daijie.core.result.PageResult;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.common.Mapper;

/**
 * 分页条件查询工具类
 * 需要结合返回的实体对象继承此对象
 * 
 * @author daijie_jay
 * @since 2018年8月10日
 * @param <E> 
 * 		与mapper对象的实体类型
 * @param <R> 
 * 		返回分页实体对象
 */
@SuppressWarnings("serial")
public abstract class ExampleExecutePage<E, R> extends Page implements ExampleConditions{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageResult<R> executePage(Mapper<E> mapper) {
		Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		if (this.getPageSize() > 0) {
			PageHelper.startPage(this.getPageNumber(), this.getPageSize());
		}
		List<E> entitys = mapper.selectByExample(this.exampleBuild((Class) types[0]));
		PageInfo<E> pageInfo = new PageInfo<>(entitys);
		return new PageResult(pageInfo.getList(), pageInfo.getTotal(), (Class) types[1]);
	}
}
