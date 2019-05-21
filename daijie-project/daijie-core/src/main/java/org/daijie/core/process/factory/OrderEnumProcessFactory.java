package org.daijie.core.process.factory;

import org.daijie.core.factory.IEnumFactory;
import org.daijie.core.process.OrderEnumProcess;
import org.daijie.core.process.Process;

import java.util.Arrays;

/**
 * 有序流程枚举工厂
 * 对流程枚举排序，查询操作
 * @author daijie_jay
 * @since 2018年1月9日
 * @param <E> ProcessEnumFactory
 */
public interface OrderEnumProcessFactory<E extends OrderEnumProcessFactory<E>> extends IEnumFactory<E>, ProcesssFactory<E, OrderEnumProcess<E>> {
	
	/**
	 * 获取流程状态码
	 * @return Integer 流程状态码
	 */
	public Integer getStatus();
    
	/**
	 * 根据流程状态码获取枚举
	 * @param status 流程状态码
	 * @return Enum 枚举类
	 */
    default public E getEnum(Integer status){
    	for (E enumObject : getEnumTypes()) {  
            if (enumObject.getStatus() == status) {  
                return enumObject;  
            }  
        }  
    	return null;
    }
    
    @Override
    default public OrderEnumProcess<E> getEnumProcess(){
    	OrderEnumProcess<E> list = new OrderEnumProcess<E>();
		Integer[] array = new Integer[getEnumTypes().length];
		for (int i = 0; i < getEnumTypes().length; i++) {
			array[i] = getEnumTypes()[i].getStatus();
		}
		Arrays.sort(array);
		for (int i = 0; i < array.length; i++) {
			list.add(getEnum(array[i]));
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
