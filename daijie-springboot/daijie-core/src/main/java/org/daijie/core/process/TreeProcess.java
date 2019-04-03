package org.daijie.core.process;

import java.lang.reflect.Array;


/**
 * 树结构流程类
 * @author daijie_jay
 * @since 2018年1月11日
 * @param <E> Enum
 */
public class TreeProcess<E> {
	
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
	TreeProcess<E> defaultNext;
	
	/**
	 * 上一个默认元素节点
	 */
	TreeProcess<E> defaultPrevious;
	
	/**
	 * 当前元素节点下一个多个流程元素分支
	 */
	TreeProcess<E>[] nextProcesses;
	
	/**
	 * 当前元素节点上一个多个流程元素分支
	 */
	TreeProcess<E>[] previousProcesses;
	
	@SuppressWarnings("unchecked")
	TreeProcess(E element){
		this.previousProcesses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, 0);
		this.element = element;
        this.nextProcesses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, 0);
	}

	@SuppressWarnings("unchecked")
	public TreeProcess(TreeProcess<E> defaultPrevious, E element, TreeProcess<E> defaultNext, TreeProcess<E>[] previousProcesses, TreeProcess<E>[] nextProcesses){
		this.defaultNext = defaultNext;
		this.defaultPrevious = defaultPrevious;
		this.previousProcesses = previousProcesses == null ? (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, 0) : previousProcesses;
		this.element = element;
        this.nextProcesses = nextProcesses == null ? (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, 0) : nextProcesses;
	}
}
