package org.daijie.core.process;

import java.io.Serializable;

public interface ProcessHandle<E> extends Serializable {
	
	/**
	 * 添加枚举元素
	 * @param element 枚举元素
	 * @return boolean
	 */
	public boolean add(E element);
	
	/**
	 * 添加多个枚举元素
	 * @param elements[] 枚举元素数组
	 * @return boolean
	 */
	public boolean add(E[] elements);
	
	/**
	 * 获取流程元素大小
	 * @return int
	 */
	public int size();
	
	/**
	 * 流程元素长度是否为0
	 * @return boolean
	 */
	public boolean isEmpty();
	
	/**
	 * 获取下一个流程元素
	 * @param element 流程元素
	 * @param processEnum 流程节点流转条件
	 * @return Enum
	 */
	public E next(E element, Process processEnum);
	
	/**
	 * 获取上一个流程元素
	 * @param element 流程元素
	 * @param processEnum 流程节点流转条件
	 * @return Enum
	 */
	public E pre(E element, Process processEnum);
}
