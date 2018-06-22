package org.daijie.jdbc.mybatis.example;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.util.StringUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * mybatis条件构建器
 * @author daijie_jay
 * @since 2018年6月21日
 */
public interface ExampleConditions extends Serializable {

	/**
	 * 构建条件
	 * @param className 对就表的实体类（是被扫描的实体）
	 * @return Example
	 */
	default public Example exampleBuild(Class<?> className) {
		Example example = new Example(className);
		Criteria criteria = example.createCriteria();
		Class<? extends ExampleConditions> conditionsClassName = this.getClass();
		Field[] fields = conditionsClassName.getDeclaredFields();
		try {
			for (Field field : fields) {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), conditionsClassName);
				Method readMethod = propertyDescriptor.getReadMethod();
				Object invoke = readMethod.invoke(this);
				if (invoke == null || (invoke instanceof String && ((String)invoke).equals(""))) {
					continue;
				}
				String conditionName = null;
				Column column = field.getAnnotation(Column.class);
				if (column != null && !StringUtils.isEmpty(column.name())) {
					conditionName = column.name();
				}
				if (field.getType() == Date.class) {
					if (field.getName().endsWith("Start")) {
						conditionName = conditionName == null ? field.getName().replace("Start", "") : conditionName;
						criteria.andLessThanOrEqualTo(conditionName, invoke);
					} else if (field.getName().endsWith("End")) {
						conditionName = conditionName == null ? field.getName().replace("End", "") : conditionName;
						criteria.andGreaterThanOrEqualTo(conditionName, invoke);
					}
				} else {
					conditionName = conditionName == null ? field.getName() : conditionName;
					criteria.andEqualTo(field.getName(), invoke);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return example;
	}
}
