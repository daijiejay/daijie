package org.daijie.core.process;

/**
 * 控制流程流转的状态枚举类
 * @author daijie_jay
 * @since 2018年1月12日
 */
public enum Process {

	/**
	 * 通过，意义为流转到主线流程的下一个流程
	 */
	THROUGH,
	
	/**
	 * 不通过，意义为流程流转到直接结束或流转到流程的下一个分支流程
	 */
	NOT_THROUGH,
	
	/**
	 * 拒绝，意义为流转到流程的下一个分支流程
	 */
	REJECT,
	
	/**
	 * 退回，意义为流转到主线流程的上一个流程
	 */
	RETURN,
}
