package org.daijie.core.process.factory;

import org.daijie.core.factory.IEnumFactory;
import org.daijie.core.process.LinkedEnumProcess;
import org.daijie.core.process.Process;

/**
 * 双向链表枚举成员流程存储工厂
 * @author daijie_jay
 * @since 2018年1月11日
 * @param <E> Enum
 */
public interface LinkedEnumProcessFactory<E extends IEnumFactory<E>> extends IEnumFactory<E>, ProcesssFactory<E> {
	
	/**
	 * 初始化流程枚举集合
	 * @return LinkedEnumProcess
	 */
	default public void initEnumProcess(){
	}
	
	default public LinkedEnumProcess<E> getLinkedEnumProcess(){
		LinkedEnumProcess<E> process = new LinkedEnumProcess<>();
		process.add(getEnumTypes());
		return process;
	}

    default E nextProcess(Process process){
		return getLinkedEnumProcess().next(getEnumType(), process);
    }
	
	@Override
	default E nextProcess(){
		return getLinkedEnumProcess().next(getEnumType());
	}
      
	@Override  
    default E preProcess(){
		return getLinkedEnumProcess().pre(getEnumType());
    }
}
