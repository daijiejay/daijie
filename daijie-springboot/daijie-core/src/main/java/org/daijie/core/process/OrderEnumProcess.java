package org.daijie.core.process;

import java.lang.reflect.Array;

import org.daijie.core.factory.IEnumFactory;

@SuppressWarnings("unchecked")
public class OrderEnumProcess<E extends IEnumFactory<E>> implements ProcessHandle<E> {

	private static final long serialVersionUID = -4355337954752651386L;
	
	private E[] elements;
	
    private int size;
	
	private void insert(E element) {
		final E[] datas = elements;
		elements = (E[]) Array.newInstance(element.getClass(), ++size);
		if(size > 1){
			System.arraycopy(datas, 0, elements, 0, datas.length);
		}
		elements[size - 1] = element;
	}
	
	@Override
	public boolean add(E element) {
        insert(element);
        return true;
    }

	@Override
	public boolean add(E[] elements) {
		for (E element : elements) {
			insert(element);
		}
		return false;
	}

	@Override
	public int size() {
        return size;
    }
	
	@Override
	public boolean isEmpty() {
        return size == 0;
    }

	public E next(E element) {
		for (int i = 0; i < elements.length; i++) {
			if(elements[i].equals(element)){
				return elements[i+1];
			}
		}
		return null;
	}

	@Override
	public E next(E element, Process processEnum) {
		if(processEnum == Process.THROUGH){
			return next(element);
		}
		return elements[elements.length - 1];
	}

	public E pre(E element) {
		for (int i = 0; i < elements.length; i++) {
			if(elements[i].equals(element)){
				return elements[i-1];
			}
		}
		return null;
	}

	@Override
	public E pre(E element, Process processEnum) {
		return pre(element);
	}
}
