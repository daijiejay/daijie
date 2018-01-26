package org.daijie.core.process.factory;

import org.daijie.core.process.Process;
import org.daijie.core.factory.IEnumFactory;
import org.daijie.core.process.LinkedEnumProcess;

/**
 * 线性链表形式存储枚举成员
 * @author daijie_jay
 * @since 2018年1月10日
 * @param <E> IEnumFactory
 */
public interface LinkedEnumProcessFactory<E extends IEnumFactory<E>> extends IEnumFactory<E>, ProcesssFactory<E, LinkedEnumProcess<E>> {
	
	@Override
	default LinkedEnumProcess<E> getEnumProcess(){
		final LinkedEnumProcess<E> list = new LinkedEnumProcess<E>();
		for (int i = 0; i < getEnumTypes().length; i++) {
			list.add(getEnumTypes()[i]);
		}
		return list;
	}

	@Override
    default public E nextProcess(Process process){
		return getEnumProcess().next(getEnumType(), process);
    }
      
	@Override  
    default public E preProcess(Process process){
		return getEnumProcess().pre(getEnumType(), process);
    }
}
