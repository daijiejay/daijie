package org.daijie.core.factory;

/**
 * 枚举实例工厂
 * @author daijie_jay
 * @since 2018年1月1日
 */
public interface IEnumFactory {

	/**
	 * 获取枚举属性名称
	 * @return 枚举属性名称
	 */
	default public String name(){
		return this.name();
	}
}
