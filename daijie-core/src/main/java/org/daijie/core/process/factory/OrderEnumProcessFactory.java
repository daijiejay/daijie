package org.daijie.core.process.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.daijie.core.factory.IEnumFactory;

/**
 * 有序流程枚举工厂
 * 对流程枚举排序，查询操作
 * @author daijie_jay
 * @since 2018年1月9日
 * @param <E> ProcessEnumFactory
 */
public interface OrderEnumProcessFactory<E extends OrderEnumProcessFactory<E>> extends IEnumFactory<E>, ProcesssFactory<E> {
	
	/**
	 * 获取流程状态码
	 * @return Integer 流程状态码
	 */
	public Integer getStatus();
    
	/**
	 * 根据流程状态码获取枚举
	 * @param <E> 枚举类
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
    
    /**
     * 枚举排序
	 * @param <E> 枚举类
     * @return List<Enum> 枚举成员数组
     */
    default public List<E> getSortEnumList(){
    	List<E> list = new ArrayList<E>();
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
	
    default public E nextProcess(boolean complete){
    	if(complete){
    		return nextProcess();
    	}
    	return getSortEnumList().get(getEnumTypes().length - 1);
    }
	
    @Override
    default public E nextProcess(){
    	List<E> list = getSortEnumList();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getStatus() == getStatus()){
				if(list.size() > i+1){
					return list.get(i+1);
				}
				break;
			}
		}
		return null;
    }
      
    @Override 
    default public E preProcess(){
    	List<E> list = getSortEnumList();
    	for (int i = 0; i < list.size(); i++) {
    		if(list.get(i).getStatus() == getStatus()){
    			if(list.size() > 0){
    				return list.get(i-1);
    			}
    			break;
    		}
    	}
    	return null;
    }
}
