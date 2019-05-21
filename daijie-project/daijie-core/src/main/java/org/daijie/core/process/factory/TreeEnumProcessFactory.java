package org.daijie.core.process.factory;

import org.daijie.core.factory.IEnumFactory;
import org.daijie.core.process.Process;
import org.daijie.core.process.TreeEnumProcess;

/**
 * 树结构枚举成员流程存储工厂
 * @author daijie_jay
 * @since 2018年1月11日
 * @param <E> Enum
 */
public interface TreeEnumProcessFactory<E extends IEnumFactory<E>> extends IEnumFactory<E>, ProcesssFactory<E, TreeEnumProcess<E>> {
	
	@Override
	default public TreeEnumProcess<E> getEnumProcess(){
		TreeEnumProcess<E> process = new TreeEnumProcess<>();
		process.add(getEnumTypes());
		return process;
	}

	@Override 
    default E nextProcess(Process process){
		return getEnumProcess().next(getEnumType(), process);
    }
      
	@Override  
    default E preProcess(Process process){
		return getEnumProcess().pre(getEnumType(), process);
    }
}
