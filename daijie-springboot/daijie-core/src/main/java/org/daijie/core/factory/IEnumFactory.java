package org.daijie.core.factory;

/**
 * 枚举实例工厂
 * @author daijie_jay
 * @since 2018年1月1日
 */
public interface IEnumFactory<E> {
    
	/**
	 * 获取枚举类
	 * @return Enum 枚举类
	 */
    public E getEnumType();
    
    /**
     * 获取枚举成员数组
     * @return Enum[] 枚举成员数组
     */
    public E[] getEnumTypes();

	/**
	 * 获取枚举属性名称
	 * @return 枚举属性名称
	 */
	default public String name(){
		return this.name();
	}
}
