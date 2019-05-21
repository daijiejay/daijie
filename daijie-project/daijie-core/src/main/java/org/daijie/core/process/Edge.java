package org.daijie.core.process;

/**
 * 边结点
 * @author daijie_jay
 * @since 2018年1月15日
 */
public class Edge {
	
	/**
	 * 默认为主线流程
	 */
	Process process = Process.THROUGH;
	
	/**
	 * 来源节点下标
	 */
    int fromVertexIndex;
	
	/**
	 * 去向节点下标
	 */
    int toVertexIndex;
	
	/**
	 * 来源入边
	 */
    Edge fromVertexLink;
	
	/**
	 * 去向出边
	 */
    Edge toVertexLink;
	
	public Edge(Process process, int fromVertexIndex, int toVertexIndex, Edge fromVertexLink, Edge toVertexLink) {
		if(process != null){
			this.process = process;
		}
		this.fromVertexIndex = fromVertexIndex;
		this.toVertexIndex = toVertexIndex;
		this.fromVertexLink = fromVertexLink;
		this.toVertexLink = toVertexLink;
	}
}
