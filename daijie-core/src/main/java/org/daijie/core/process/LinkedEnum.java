package org.daijie.core.process;

import java.io.Serializable;

public class LinkedEnum<E> implements Serializable {
	
	private static final long serialVersionUID = -9012969703888523924L;
	
	protected transient int modCount = 0;

	transient int size = 0;
	
	transient EnumNode<E> first;
	
	transient EnumNode<E> last;

	private void linkFirst(E element) {
        final EnumNode<E> firstNode = first;
        final EnumNode<E> newNode = new EnumNode<>(null, element, firstNode);
        first = newNode;
        if (firstNode == null){
            last = newNode;
        }else{
        	firstNode.previous = newNode;
        }
        size++;
        modCount++;
    }
	
	void linkLast(E element) {
        final EnumNode<E> lastNode = last;
        final EnumNode<E> newNode = new EnumNode<>(lastNode, element, null);
        last = newNode;
        if (lastNode == null){
            first = newNode;
        }else{
        	lastNode.next = newNode;
        }
        size++;
        modCount++;
    }
	
	void linkBefore(E element, EnumNode<E> node) {
        final EnumNode<E> previous = node.previous;
        final EnumNode<E> newNode = new EnumNode<>(previous, element, node);
        node.previous = newNode;
        if (previous == null){
            first = newNode;
        }else{
        	previous.next = newNode;
        }
        size++;
        modCount++;
    }
	
	public void addFirst(E element) {
        linkFirst(element);
    }
	
	public void addLast(E element) {
        linkLast(element);
    }
	
	public void add(int index, E element) {
        if (index == size)
            linkLast(element);
        else
            linkBefore(element, processNode(index));
    }
	
	public int size() {
        return size;
    }
	
	E unlink(EnumNode<E> node) {
        final E element = node.element;
        final EnumNode<E> next = node.next;
        final EnumNode<E> previous = node.previous;
        if (previous == null) {
            first = next;
        } else {
        	previous.next = next;
        	node.previous = null;
        }
        if (next == null) {
            last = previous;
        } else {
            next.previous = previous;
            node.next = null;
        }
        node.element = null;
        size--;
        modCount++;
        return element;
    }
	
	public boolean remove(E element) {
        for (EnumNode<E> node = first; node != null; node = node.next) {
            if ((element == null && node == null) || element.equals(node.element)) {
                unlink(node);
                return true;
            }
        }
        return false;
    }
	
	public E get(int index) {
        return processNode(index).element;
    }
	
	public E getNext(int index) {
		return processNode(index).next.element;
	}
	
	public E getPre(int index) {
		return processNode(index).previous.element;
	}
	
	public EnumNode<E> getNode(int index) {
		return processNode(index);
	}
	
	EnumNode<E> processNode(int index) {
        if (index < (size >> 1)) {
        	EnumNode<E> node = first;
            for (int i = 0; i < index; i++){
            	node = node.next;
            }
            return node;
        } else {
        	EnumNode<E> node = last;
            for (int i = size - 1; i > index; i--){
            	node = node.previous;
            }
            return node;
        }
    }
	
	public EnumNode<E> next(EnumNode<E> node) {
		return node.next;
	}
	
	public EnumNode<E> pre(EnumNode<E> node) {
		return node.previous;
	}
}
