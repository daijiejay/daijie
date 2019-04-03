package org.daijie.core.process.factory;

import org.daijie.core.factory.IEnumFactory;
import org.daijie.core.process.MapOrthogonalEnumProcess;
import org.daijie.core.process.Process;

/**
 * 图形十字链表结构枚举成员流程存储工厂
 * @author daijie_jay
 * @since 2018年1月15日
 * @param <E> Enum
 */
public interface MapOrthogonalEnumProcessFactory<E extends IEnumFactory<E>> extends IEnumFactory<E>, ProcesssFactory<E, MapOrthogonalEnumProcess<E>> {

	@Override
	default public MapOrthogonalEnumProcess<E> getEnumProcess(){
		MapOrthogonalEnumProcess<E> process = new MapOrthogonalEnumProcess<>(getEnumTypes().length);
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
