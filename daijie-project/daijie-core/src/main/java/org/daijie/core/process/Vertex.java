package org.daijie.core.process;

/**
 * 流程节点类
 * @author daijie_jay
 * @since 2018年1月15日
 * @param <E> Enum
 */
public class Vertex<E> {

	/**
	 * 枚举成员元素
	 */
	E element;
	
	/**
	 * 入边
	 */
    Edge firstIn;
    
    /**
     * 出边
     */
    Edge firstOut;

    public Vertex(E element) {
        this.element = element;
    }
}
