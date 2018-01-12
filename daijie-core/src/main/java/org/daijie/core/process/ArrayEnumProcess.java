package org.daijie.core.process;

import java.io.Serializable;

import org.daijie.core.factory.IEnumFactory;

@SuppressWarnings("unchecked")
public class ArrayEnumProcess<E extends IEnumFactory<E>> implements Serializable {

	private static final long serialVersionUID = -4355337954752651386L;
	
	private static final Object[] DEFAULT_ELEMENTS = {};
	
	private EnumProcess<E>[] elements;
	
    private int size;
    
	public ArrayEnumProcess(){
    	elements = (EnumProcess<E>[]) DEFAULT_ELEMENTS;
    }
	
	void insert(E element) {
		final EnumProcess<E> newProcess = new EnumProcess<>(element);
		elements[size++] = newProcess;
	}
	
	public boolean add(E element) {
        insert(element);
        return true;
    }

	public int size() {
        return size;
    }
	
	public boolean isEmpty() {
        return size == 0;
    }
}
