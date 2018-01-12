package org.daijie.core.process;


/**
 * 枚举流程类
 * @author daijie_jay
 * @since 2018年1月11日
 * @param <E> Enum
 */
public class EnumProcess<E> {
	
	/**
	 * 当前枚举成员元素
	 */
	E element;
	
	/**
	 * 默认为主线流程
	 */
	Process process = Process.THROUGH;
	
	/**
	 * 下一个默认元素节点
	 */
	EnumProcess<E> defaultNext;
	
	/**
	 * 上一个默认元素节点
	 */
	EnumProcess<E> defaultPrevious;
	
	/**
	 * 当前元素节点下一个多个流程元素分支
	 */
	EnumProcess<E>[] nextProcesses;
	
	/**
	 * 当前元素节点上一个多个流程元素分支
	 */
	EnumProcess<E>[] previousProcesses;
	
	@SuppressWarnings("unchecked")
	EnumProcess(E element){
		this.previousProcesses = (EnumProcess<E>[]) new Object[]{};
		this.element = element;
        this.nextProcesses = (EnumProcess<E>[]) new Object[]{};
	}

	@SuppressWarnings("unchecked")
	public EnumProcess(EnumProcess<E> defaultPrevious, E element, EnumProcess<E> defaultNext, EnumProcess<E>[] previousProcesses, EnumProcess<E>[] nextProcesses){
		this.defaultNext = defaultNext;
		this.defaultPrevious = defaultPrevious;
		this.previousProcesses = previousProcesses == null ? (EnumProcess<E>[]) new Object[]{} : previousProcesses;
		this.element = element;
        this.nextProcesses = nextProcesses == null ? (EnumProcess<E>[]) new Object[]{} : nextProcesses;
	}
}
