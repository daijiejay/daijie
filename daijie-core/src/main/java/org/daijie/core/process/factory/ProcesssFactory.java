package org.daijie.core.process.factory;


/**
 * 流程处理工厂
 * @author daijie_jay
 * @since 2018年1月10日
 */
public interface ProcesssFactory<E> {

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
     * 下一个流程
     * @return IEnumFactory
     */  
    public E nextProcess();
    
    /** 
     * 上一个流程
     * @return IEnumFactory
     */  
    public E preProcess();

}
