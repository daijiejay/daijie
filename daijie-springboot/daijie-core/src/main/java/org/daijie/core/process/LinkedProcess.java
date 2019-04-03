package org.daijie.core.process;

/**
 * 线性结构流程类
 * @author daijie_jay
 * @since 2018年1月10日
 * @param <E> IEnumFactory
 */
public class LinkedProcess<E> {

	E element;
	
	LinkedProcess<E> next;
	
	LinkedProcess<E> previous;
	
	LinkedProcess(){}
	
	LinkedProcess(LinkedProcess<E> previous, E element, LinkedProcess<E> next){
		this.previous = previous;
		this.element = element;
        this.next = next;
	}
}
