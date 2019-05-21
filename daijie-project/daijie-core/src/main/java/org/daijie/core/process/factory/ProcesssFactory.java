package org.daijie.core.process.factory;

import org.daijie.core.factory.Factory;
import org.daijie.core.process.Process;

import java.io.Serializable;


/**
 * 流程处理工厂
 * @author daijie_jay
 * @since 2018年1月10日
 */
public interface ProcesssFactory<E, T extends Serializable> extends Factory {

	/**
	 * 获取委派人
	 * @return String 委派人
	 */
	public String getAssignee();
	
	/**
	 * 获取流程说明
	 * @return String 流程说明
	 */
	public String getMsg();
	
	/**
	 * 获取流程元素集合类
	 * @return Serializable
	 */
	public T getEnumProcess();
	
	/** 
     * 下一个流程
     * @param process 当前流程到下一个流程流转的条件
     * @return IEnumFactory
     */  
    public E nextProcess(Process process);
    
    /** 
     * 上一个流程
     * @param process 上一个流程到当前流程流转的条件
     * @return IEnumFactory
     */  
    public E preProcess(Process process);

}
