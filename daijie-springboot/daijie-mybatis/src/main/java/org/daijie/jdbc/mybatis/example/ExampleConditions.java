package org.daijie.jdbc.mybatis.example;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.daijie.jdbc.annotation.ExampleCondition;
import org.daijie.jdbc.annotation.ExampleCondition.ConditionType;
import org.daijie.jdbc.annotation.ExampleCondition.OrderType;
import org.daijie.jdbc.annotation.ExampleCondition.SectionType;
import org.springframework.util.StringUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.entity.Example.OrderBy;

/**
 * mybatis条件构建器
 * 
 * @author daijie_jay
 * @since 2018年6月21日
 */
public interface ExampleConditions extends Serializable {

	/**
	 * 构建条件
	 * 
	 * @param className
	 *            对就表的实体类（是被扫描的实体）
	 * @return Example
	 */
	default public Example exampleBuild(Class<?> className) {
		Example example = new Example(className);
		Criteria criteria = example.createCriteria();
		Class<? extends ExampleConditions> conditionsClassName = this.getClass();
		Field[] fields = conditionsClassName.getDeclaredFields();
		String order = null;
		String sort = null;
		try {
			for (Field field : fields) {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), conditionsClassName);
				Method readMethod = propertyDescriptor.getReadMethod();
				Object invoke = readMethod.invoke(this);
				if (invoke == null || (invoke instanceof String && ((String) invoke).equals(""))) {
					continue;
				}
				String conditionName = null;
				ConditionType type = null;
				SectionType sectionType = null;
				OrderType orderType = null;
				ExampleCondition condition = field.getAnnotation(ExampleCondition.class);
				if (condition != null && !StringUtils.isEmpty(condition.name())) {
					conditionName = condition.name();
					type = condition.type();
					sectionType = condition.sectionType();
					orderType = condition.orderType();
					
				}
				if (type != null && type == ConditionType.SECTION && sectionType == null) {
					throw new ExampleConditionException(field.getName() + "字段必须指定SectionType");
				}
				String fieldName = conditionName == null ? field.getName() : conditionName;
				if (type == null && className.getDeclaredField(field.getName()) != null) {
					criteria.andEqualTo(fieldName, invoke);
				} else if (type == ConditionType.SECTION) {
					if (sectionType == SectionType.GREATER) {
						criteria.andGreaterThan(fieldName, invoke);
					} else if (sectionType == SectionType.GREATER_OR_EQUALT) {
						criteria.andGreaterThanOrEqualTo(fieldName, invoke);
					} else if (sectionType == SectionType.LESS) {
						criteria.andLessThan(fieldName, invoke);
					} else if (sectionType == SectionType.LESS_OR_EQUALT) {
						criteria.andLessThanOrEqualTo(fieldName, invoke);
					}
				} else if (type == ConditionType.ORDER) {
					if (orderType == OrderType.ASC) {
						example.orderBy(fieldName).asc();
					} else if (orderType == OrderType.DESC) {
						example.orderBy(fieldName).desc();
					}
				}
			}
			extendConditions(criteria);
			if (org.daijie.core.result.Page.class == conditionsClassName.getSuperclass()) {
				order = (String) conditionsClassName.getMethod("getOrder").invoke(this);
				sort = (String) conditionsClassName.getMethod("getSort").invoke(this);
			}
			if (!StringUtils.isEmpty(order) && !StringUtils.isEmpty(sort)) {
				OrderBy orderBy = example.orderBy(sort);
				if (order.toUpperCase().equals("ASC")) {
					orderBy.asc();
				} else {
					orderBy.desc();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return example;
	}
	
	/**
	 * 扩展条件
	 * 
	 * @param criteria mybatis添加条件的Criteria
	 */
	default public void extendConditions(Criteria criteria) {
		
	}
}
