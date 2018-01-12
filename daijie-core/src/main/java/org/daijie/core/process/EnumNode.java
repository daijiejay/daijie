package org.daijie.core.process;

/**
 * 枚举节点类
 * @author daijie_jay
 * @since 2018年1月10日
 * @param <E> IEnumFactory
 */
public class EnumNode<E> {

	E element;
	
	EnumNode<E> next;
	
	EnumNode<E> previous;
	
	EnumNode(){}
	
	EnumNode(EnumNode<E> previous, E element, EnumNode<E> next){
		this.previous = previous;
		this.element = element;
        this.next = next;
	}
}
