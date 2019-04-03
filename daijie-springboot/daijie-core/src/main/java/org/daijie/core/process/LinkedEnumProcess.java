package org.daijie.core.process;

/**
 * 双向链表算法实现的流程存储集合类
 * @author daijie_jay
 * @since 2018年1月16日
 * @param <E> Enum
 */
public class LinkedEnumProcess<E> implements ProcessHandle<E> {
	
	private static final long serialVersionUID = -9012969703888523924L;
	
	protected transient int modCount = 0;

	transient int size = 0;
	
	transient LinkedProcess<E> first;
	
	transient LinkedProcess<E> last;

	/**
	 * 在链表的首个元素前插入元素
	 * @param element 流程元素
	 */
	private void linkFirst(E element) {
        final LinkedProcess<E> firstNode = first;
        final LinkedProcess<E> newNode = new LinkedProcess<>(null, element, firstNode);
        first = newNode;
        if (firstNode == null){
            last = newNode;
        }else{
        	firstNode.previous = newNode;
        }
        size++;
        modCount++;
    }
	
	/**
	 * 在链表的未尾元素后添加元素
	 * @param element 流程元素
	 */
	private void linkLast(E element) {
        final LinkedProcess<E> lastNode = last;
        final LinkedProcess<E> newNode = new LinkedProcess<>(lastNode, element, null);
        last = newNode;
        if (lastNode == null){
            first = newNode;
        }else{
        	lastNode.next = newNode;
        }
        size++;
        modCount++;
    }
	
	/**
	 * 在指定元素后插入流程元素类
	 * @param element 流程元素
	 * @param process 流程元素类
	 */
	private void linkBefore(E element, LinkedProcess<E> process) {
        final LinkedProcess<E> previous = process.previous;
        final LinkedProcess<E> newNode = new LinkedProcess<>(previous, element, process);
        process.previous = newNode;
        if (previous == null){
            first = newNode;
        }else{
        	previous.next = newNode;
        }
        size++;
        modCount++;
    }
	
	/**
	 * 删除某个流程元素类，并返回该流程元素
	 * @param process
	 * @return Enum
	 */
	private E unlink(LinkedProcess<E> process) {
        final E element = process.element;
        final LinkedProcess<E> next = process.next;
        final LinkedProcess<E> previous = process.previous;
        if (previous == null) {
            first = next;
        } else {
        	previous.next = next;
        	process.previous = null;
        }
        if (next == null) {
            last = previous;
        } else {
            next.previous = previous;
            process.next = null;
        }
        process.element = null;
        size--;
        modCount++;
        return element;
    }
	
	/**
	 * 根据元素下标值获取对应流程元素类
	 * @param index 元素下标值
	 * @return LinkedProcess
	 */
	private LinkedProcess<E> processNode(int index) {
        if (index < (size >> 1)) {
        	LinkedProcess<E> process = first;
            for (int i = 0; i < index; i++){
            	process = process.next;
            }
            return process;
        } else {
        	LinkedProcess<E> process = last;
            for (int i = size - 1; i > index; i--){
            	process = process.previous;
            }
            return process;
        }
    }
	
	/**
	 * 在链表的首个元素前插入元素
	 * @param element 流程元素
	 * @return boolean
	 */
	public boolean addFirst(E element) {
        linkFirst(element);
		return true;
    }
	
	/**
	 * 在链表的未尾元素后添加元素
	 * @param element 流程元素
	 * @return boolean
	 */
	public boolean addLast(E element) {
        linkLast(element);
		return true;
    }
	
	@Override
	public boolean add(E element) {
		linkLast(element);
		return true;
	}

	@Override
	public boolean add(E[] elements) {
		for (E element : elements) {
			linkLast(element);
		}
		return true;
	}
	
	/**
	 * 指定下标值添加流程元素，下标值以上的往后移
	 * @param index 元素下标值
	 * @param element 流程元素
	 * @return boolean
	 */
	public boolean add(int index, E element) {
        if (index == size){
            linkLast(element);
        }else{
            linkBefore(element, processNode(index));
        }
        return true;
    }
	
	@Override
	public int size() {
        return size;
    }
	
	@Override
	public boolean isEmpty() {
        return size == 0;
    }
	
	/**
	 * 删除流程元素
	 * @param element 流程元素
	 * @return boolean
	 */
	public boolean remove(E element) {
        for (LinkedProcess<E> node = first; node != null; node = node.next) {
            if ((element == null && node == null) || element.equals(node.element)) {
                unlink(node);
                return true;
            }
        }
        return false;
    }
	
	/**
	 * 根据指定下标值获取当前元素
	 * @param index 下标值
	 * @return Enum
	 */
	public E get(int index) {
        return processNode(index).element;
    }
	
	/**
	 * 根据指定下标值获取下一个元素
	 * @param index 下标值
	 * @return Enum
	 */
	public E getNext(int index) {
		return processNode(index).next.element;
	}
	
	/**
	 * 根据指定下标值获取上一个元素
	 * @param index 下标值
	 * @return Enum
	 */
	public E getPre(int index) {
		return processNode(index).previous.element;
	}
	
	public E next(E element) {
		return next(element, Process.THROUGH);
	}

	@Override
	public E next(E element, Process processEnum) {
		if(processEnum == Process.NOT_THROUGH){
			return processNode(size).element;
		}
		for (LinkedProcess<E> node = first; node != null; node = node.next) {
            if ((element == null && node == null) || element.equals(node.element)) {
            	return node.next.element;
            }
        }
		return null;
	}
	
	public E pre(E element) {
		return pre(element, Process.THROUGH);
	}

	@Override
	public E pre(E element, Process processEnum) {
		for (LinkedProcess<E> node = first; node != null; node = node.next) {
            if ((element == null && node == null) || element.equals(node.element)) {
            	return node.previous.element;
            }
        }
		return null;
	}
}
