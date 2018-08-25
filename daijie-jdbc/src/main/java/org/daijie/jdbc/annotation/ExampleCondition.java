package org.daijie.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动拼接条件配置
 * @author daijie_jay
 * @since 2018年7月19日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExampleCondition {
	
	String name();

	ConditionType type();
	
	SectionType sectionType();
	
	OrderType orderType();
	
	enum ConditionType {
		
		SECTION,
		
		ORDER;
	}
	
	enum SectionType {
		
		GREATER,
		
		GREATER_OR_EQUALT,
		
		LESS,
		
		LESS_OR_EQUALT;
	}
	
	enum OrderType {
		
		ASC,
		
		DESC,
	}
}

