package org.daijie.core.process.factory;

import org.daijie.core.factory.IEnumFactory;
import org.daijie.core.process.LinkedEnum;

/**
 * 链表形式存储枚举成员
 * @author daijie_jay
 * @since 2018年1月10日
 * @param <E> IEnumFactory
 */
public interface LinkedEnumFactory<E extends IEnumFactory<E>> extends IEnumFactory<E>, ProcesssFactory<E> {
	
	default LinkedEnum<E> linkedEnumList(){
		final LinkedEnum<E> list = new LinkedEnum<E>();
		for (int i = 0; i < getEnumTypes().length; i++) {
			list.addLast(getEnumTypes()[i]);
		}
		return list;
	}

	@Override
    default public E nextProcess(){
    	LinkedEnum<E> list = linkedEnumList();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).equals(getEnumType())){
				return list.getNext(i);
			}
		}
		return null;
    }
      
	@Override  
    default public E preProcess(){
    	LinkedEnum<E> list = linkedEnumList();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).equals(getEnumType())){
				return list.getPre(i);
			}
		}
		return null;
    }
}
