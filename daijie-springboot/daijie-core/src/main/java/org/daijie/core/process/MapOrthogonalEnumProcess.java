package org.daijie.core.process;

import java.lang.reflect.Array;

import org.daijie.core.factory.IEnumFactory;

/**
 * 图形十字链表算法实现的流程存储集合类
 * @author daijie_jay
 * @since 2018年1月15日
 * @param <E> Enum
 */
@SuppressWarnings("unchecked")
public class MapOrthogonalEnumProcess<E extends IEnumFactory<E>> implements ProcessHandle<E> {

	private static final long serialVersionUID = -3676220248726358481L;
	
	private Vertex<E>[] vertexs;
	
	private int vertexSize;
	
	private int edgeSize;
	
	public MapOrthogonalEnumProcess(int index) {
		vertexs = (Vertex<E>[]) Array.newInstance(Vertex.class, index);
	}
	
	/**
	 * 获取元素指定的下标
	 * 如果不存在该元素会自动创建
	 * @param element 流程元素
	 * @return int
	 */
	private int check(E element) {
		for (int i = 0; i < vertexSize; i++) {
			if(element.equals(vertexs[i].element)){
				return i;
			}
		}
		final Vertex<E> newVertex = new Vertex<E>(element);
		vertexs[vertexSize++] = newVertex;
		return vertexSize - 1;
	}

	/**
	 * 关连流程元素节点并设置流转条件
	 * @param from 来源节点元素
	 * @param to 去向节点元素
	 * @param processEnum 流程节点流转条件
	 */
	private void link(E from, E to, Process processEnum) {
		int indexFrom = check(from);
		int indexTo = check(to);
		
		Edge edgeOut = vertexs[indexFrom].firstOut;
		Edge edgeIn = vertexs[indexTo].firstIn;
		final Edge edge = new Edge(processEnum, indexFrom, indexTo, edgeIn, edgeOut);
		vertexs[indexFrom].firstOut = edge;
        vertexs[indexTo].firstIn = edge;
        edgeSize++;
	}
	
	/**
	 * 获取流程元素的下一节点流程元素
	 * @param element 流程元素
	 * @return Enum
	 */
	private E nextNode(E element) {
		return nextNode(element, Process.THROUGH);
	}

	/**
	 * 获取流程元素的下一节点流程元素
	 * @param element 流程元素
	 * @param processEnum 流程节点流转条件
	 * @return Enum
	 */
	private E nextNode(E element, Process processEnum) {
		int index = check(element);
		Edge edge = vertexs[index].firstOut;
		while (edge != null) {
			if(edge.process.equals(processEnum)){
				return vertexs[edge.toVertexIndex].element;
			}
			edge = edge.toVertexLink;
		}
		return null;
	}
	
	/**
	 * 获取流程元素的上一节点流程元素
	 * @param element 流程元素
	 * @return Enum
	 */
	private E preNode(E element) {
		int index = check(element);
		Edge edge = vertexs[index].firstIn;
		if (edge != null) {
			return vertexs[edge.fromVertexIndex].element;
		}
		return null;
	}

	/**
	 * 获取流程元素的上一节点流程元素
	 * @param element 流程元素
	 * @param processEnum 流程节点流转条件
	 * @return Enum
	 */
	private E preNode(E element, Process processEnum) {
		int index = check(element);
		Edge edge = vertexs[index].firstIn;
		while (edge != null) {
			if(edge.process.equals(processEnum)){
				return vertexs[edge.fromVertexIndex].element;
			}
			edge = edge.fromVertexLink;
		}
		return null;
	}
	
	@Override
	public boolean add(E element) {
		if(vertexSize == 0){
			check(element);
		}else{
			if(vertexSize < 2){
				link(vertexs[vertexSize - 1].element, element, null);
			}else{
				for (int i = vertexSize - 1; i >= 0; i--) {
					Edge edge = vertexs[i].firstIn;
					while (edge != null) {
						if(edge.process.equals(Process.THROUGH)){
							link(vertexs[edge.toVertexIndex].element, element, null);
							return true;
						}
						edge = edge.fromVertexLink;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean add(E[] elements) {
		for (int i = 0; i < elements.length; i++) {
			add(elements[i]);
		}
		return true;
	}
	
	/**
	 * 添加流程元素节点
	 * @param from 来源元素
	 * @param to 去向元素
	 * @param processEnum 流程节点流转条件
	 * @return boolean
	 */
	public boolean add(E from, E to, Process processEnum) {
		link(from, to, processEnum);
		return true;
	}
	
	/**
	 * 获取流程线数
	 * @return int
	 */
	public int line() {
		return edgeSize;
	}
	
	@Override
	public int size() {
        return vertexSize;
    }
	
	@Override
	public boolean isEmpty() {
        return vertexSize == 0;
    }
	
	public E next(E element) {
		return nextNode(element);
	}
	
	@Override
	public E next(E element, Process processEnum) {
		return nextNode(element, processEnum);
	}
	
	public E pre(E element) {
		return preNode(element);
	}
	
	@Override
	public E pre(E element, Process processEnum) {
		return preNode(element, processEnum);
	}
}
