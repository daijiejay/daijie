package org.daijie.core.process;

import java.io.Serializable;

import org.daijie.core.factory.IEnumFactory;

/**
 * 双向链表算法实现的流程存储集合类
 * @author daijie_jay
 * @since 2018年1月11日
 * @param <E> Enum
 */
@SuppressWarnings("unchecked")
public class LinkedEnumProcess<E extends IEnumFactory<E>> implements Serializable {
	
	private static final long serialVersionUID = -6144753183618957937L;

	protected transient int modCount;

	transient int size;
	
	transient EnumProcess<E> firstProcess;
	
	transient EnumProcess<E> lastProcess;

	/**
	 * 在开头添加枚举元素
	 * @param element 枚举元素
	 */
	void linkFirst(E element) {
        final EnumProcess<E> newFirstProcess = firstProcess;
        EnumProcess<E>[] newFirstProcesses = null;
        if(newFirstProcess != null){
        	newFirstProcesses = (EnumProcess<E>[]) new Object[]{newFirstProcess};
        }
        final EnumProcess<E> newProcess = new EnumProcess<>(null, element, newFirstProcess, null, newFirstProcesses);
        firstProcess = newProcess;
        if (newFirstProcess == null){
        	lastProcess = newProcess;
        }else{
        	newFirstProcess.defaultPrevious = newProcess;
        }
        size++;
        modCount++;
    }

	/**
	 * 在末尾添加枚举元素
	 * @param element 枚举元素
	 */	
	void linkLast(E element) {
        final EnumProcess<E> newLastProcess = lastProcess;
        EnumProcess<E>[] newLastProcesses = null;
        if(newLastProcess != null){
        	newLastProcesses = (EnumProcess<E>[]) new Object[]{newLastProcess};
        }
        final EnumProcess<E> newProcess = new EnumProcess<>(newLastProcess, element, null, newLastProcesses, null);
        lastProcess = newProcess;
        if (newLastProcess == null){
        	firstProcess = newProcess;
        }else{
        	newLastProcess.defaultNext = newProcess;
        }
        size++;
        modCount++;
    }

	/**
	 * 在枚举类之前添加枚举元素
	 * @param element 枚举元素
	 * @param process 要插入此流程之前的枚举类，即element->process
	 */
	void linkBefore(E element, EnumProcess<E> process) {
        final EnumProcess<E> defaultPrevious = process.defaultPrevious;
        final EnumProcess<E>[] previousProcesses = process.previousProcesses;
        final EnumProcess<E>[] newLastProcesses = (EnumProcess<E>[]) new Object[]{process};
        final EnumProcess<E> newProcess = new EnumProcess<>(defaultPrevious, element, process, previousProcesses, newLastProcesses);
        process.defaultPrevious = newProcess;
        if (defaultPrevious == null){
        	firstProcess = newProcess;
        }else{
        	defaultPrevious.defaultNext = newProcess;
        }
        size++;
        modCount++;
    }

	/**
	 * 在枚举类之后添加枚举元素
	 * @param element 枚举元素
	 * @param process 要插入此流程之后的枚举类，即process->element
	 */
	void linkAfter(E element, EnumProcess<E> process) {
		final EnumProcess<E> defaultNext= process.defaultNext;
		final EnumProcess<E>[] nextProcesses = process.nextProcesses;
		final EnumProcess<E>[] newPreviosProcesses = (EnumProcess<E>[]) new Object[]{process};
		final EnumProcess<E> newProcess = new EnumProcess<>(process, element, defaultNext, newPreviosProcesses, nextProcesses);
		process.defaultNext = newProcess;
		if (defaultNext == null){
			lastProcess = newProcess;
		}else{
			defaultNext.defaultPrevious = newProcess;
		}
		size++;
		modCount++;
	}

	/**
	 * 在枚举类之后添加流程分支枚举元素并关连顺序
	 * @param beginProcess 要插入此流程之后的枚举类，即beginProcess->element
	 * @param element 枚举元素
	 */
	void linkBranch(EnumProcess<E> beginProcess, E element) {
		EnumProcess<E>[] nextProcesses = beginProcess.nextProcesses;
		final EnumProcess<E>[] newPreviosProcesses = (EnumProcess<E>[]) new Object[]{beginProcess};
		final EnumProcess<E> newProcess = new EnumProcess<>(beginProcess, element, null, newPreviosProcesses, null);
		nextProcesses[nextProcesses.length + 1] = newProcess;
		modCount++;
	}
	
	/**
	 * 在枚举类之后添加流程分支枚举元素并关连顺序
	 * @param beginProcess 要插入此流程之后的枚举类，即beginProcess->element
	 * @param element 枚举元素
	 * @param processEnum 设置流程节点流转条件
	 */
	void linkBranch(EnumProcess<E> beginProcess, E element, Process processEnum) {
		EnumProcess<E>[] nextProcesses = beginProcess.nextProcesses;
		final EnumProcess<E>[] newPreviosProcesses = (EnumProcess<E>[]) new Object[]{beginProcess};
		final EnumProcess<E> newProcess = new EnumProcess<>(beginProcess, element, null, newPreviosProcesses, null);
		newProcess.process = processEnum;
		nextProcesses[nextProcesses.length + 1] = newProcess;
		modCount++;
	}
	
	/**
	 * 设置枚举类的关联顺序
	 * @param beginProcess 要设置此流程之后的枚举类，即beginProcess->process
	 * @param process 要设置的枚举类
	 */
	void setLinkBranch(EnumProcess<E> beginProcess, EnumProcess<E> process) {
		beginProcess.nextProcesses[beginProcess.nextProcesses.length] = process;
		if(beginProcess.defaultNext == null){
			beginProcess.defaultNext = process;
		}
		process.previousProcesses[process.previousProcesses.length] = beginProcess;
		if(process.defaultPrevious == null){
			process.defaultPrevious = beginProcess;
		}
	}

	/**
	 * 在枚举类之后添加流程分支枚举元素并关连顺序
	 * @param beginProcess 要插入此流程之后的枚举类，即beginProcess->element
	 * @param element 枚举元素
	 * @param endProcess 给该插入的枚举元素加上下一个流程的枚举类，即element->endProcess
	 */
	void linkBranch(EnumProcess<E> beginProcess, E element, EnumProcess<E> endProcess) {
		EnumProcess<E>[] nextProcesses = beginProcess.nextProcesses;
		final EnumProcess<E>[] newPreviosProcesses = (EnumProcess<E>[]) new Object[]{beginProcess};
        final EnumProcess<E>[] newLastProcesses = (EnumProcess<E>[]) new Object[]{endProcess};
		final EnumProcess<E> newProcess = new EnumProcess<>(beginProcess, element, endProcess, newPreviosProcesses, newLastProcesses);
		nextProcesses[nextProcesses.length + 1] = newProcess;
		modCount++;
	}
	
	/**
	 * 在枚举类之后添加流程分支枚举元素并关连顺序
	 * @param beginProcess 要插入此流程之后的枚举类，即beginProcess->element
	 * @param element 枚举元素
	 * @param endProcess 给该插入的枚举元素加上下一个流程的枚举类，即element->endProcess
	 * @param processEnum 设置流程节点流转条件
	 */
	void linkBranch(EnumProcess<E> beginProcess, E element, EnumProcess<E> endProcess, Process processEnum) {
		EnumProcess<E>[] nextProcesses = beginProcess.nextProcesses;
		final EnumProcess<E>[] newPreviosProcesses = (EnumProcess<E>[]) new Object[]{beginProcess};
		final EnumProcess<E>[] newLastProcesses = (EnumProcess<E>[]) new Object[]{endProcess};
		final EnumProcess<E> newProcess = new EnumProcess<>(beginProcess, element, endProcess, newPreviosProcesses, newLastProcesses);
		newProcess.process = processEnum;
		nextProcesses[nextProcesses.length + 1] = newProcess;
		modCount++;
	}
	
	/**
	 * 设置枚举类的关联顺序
	 * @param beginProcess 要设置此流程之后的枚举类，即beginProcess->process
	 * @param process 要设置的枚举类
	 * @param endProcess 要设置此流程之前的枚举类，即process->endProcess
	 */
	void setLinkBranch(EnumProcess<E> beginProcess, EnumProcess<E> process, EnumProcess<E> endProcess) {
		setLinkBranch(beginProcess, process);
		setLinkBranch(process, endProcess);
	}
	
	/**
	 * 给下一个流程设置默认分支流程
	 * @param beginProcess 要设置此流程之后的枚举类，即beginProcess->element
	 * @param element 枚举元素
	 * @return boolean
	 */
	boolean linkDefaultBranch(EnumProcess<E> beginProcess, E element) {
		EnumProcess<E>[] nextProcesses = beginProcess.nextProcesses;
		for (EnumProcess<E> process : nextProcesses) {
			if(process.element.equals(element)){
				process.process = Process.THROUGH;
				beginProcess.defaultNext = process;
				return true;
			}
		}
		return false;
	}
	
	E[] array = (E[]) new Object[]{};
	
	/**
	 * 根据枚举元素查找枚举类
	 * 以树型结构算法实现
	 * array记录已查找过的枚举类，防止重复查找或进入死循环
	 * @param process 树结构的根节点枚举类
	 * @param element 枚举元素
	 * @return EnumProcess
	 */
	EnumProcess<E> recursionNode(EnumProcess<E> process, E element) {
		if(process != null){
			for (E unique : array) {
				if(unique== element){
					return null;
				}
			}
			if(process.element.equals(element)){
				return process;
			}
			array[array.length] = process.element;
			for (EnumProcess<E> enumProcess : process.nextProcesses) {
				return recursionNode(enumProcess, element);
			}
		}
		return null;
	}

	/**
	 * 在元素之后添加流程分支枚举元素
	 * @param beginElement 要插入此流程之后的流程元素，即beginElement->element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @return boolean
	 */
	public boolean addBranch(E beginElement, E element) {
		final EnumProcess<E> process = recursionNode(firstProcess, beginElement);
		if(process != null && process.element.equals(beginElement)){
			linkBranch(process, element);
			return true;
		}
		return false;
	}
	
	/**
	 * 在元素之后添加流程分支枚举元素
	 * @param beginElement 要插入此流程之后的流程元素，即beginElement->element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param processEnum 设置流程节点流转条件
	 * @return boolean
	 */
	public boolean addBranch(E beginElement, E element, Process processEnum) {
		final EnumProcess<E> process = recursionNode(firstProcess, beginElement);
		if(process != null && process.element.equals(beginElement)){
			linkBranch(process, element, processEnum);
			return true;
		}
		return false;
	}
	
	/**
	 * 设置枚举元素并关连顺序，如果枚举元素不存在则添加
	 * @param beginElement 要设置此流程之后的流程元素，即beginElement->element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @return boolean
	 */
	public boolean setBranch(E beginElement, E element) {
		final EnumProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final EnumProcess<E> process = recursionNode(firstProcess, element);
		if(beginProcess != null && beginProcess.element.equals(beginElement)){
			if(process == null){
				linkBranch(beginProcess, element);
			}else{
				setLinkBranch(beginProcess, process);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 设置枚举元素并关连顺序，如果枚举元素不存在则添加
	 * @param beginElement 要设置此流程之后的流程元素，即beginElement->element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param processEnum 设置流程节点流转条件
	 * @return boolean
	 */
	public boolean setBranch(E beginElement, E element, Process processEnum) {
		final EnumProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final EnumProcess<E> process = recursionNode(firstProcess, element);
		if(beginProcess != null && beginProcess.element.equals(beginElement)){
			if(process == null){
				linkBranch(beginProcess, element, processEnum);
			}else{
				process.process = processEnum;
				setLinkBranch(beginProcess, process);
			}
			return true;
		}
		return false;
	}

	/**
	 * 在元素之后添加流程分支枚举元素
	 * @param beginElement 要插入此流程之后的流程元素，即beginElement->element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param endElement 给该入的枚举元素加上下一个流程的流程元素，即element->endElement，必须是已添加的枚举元素
	 * @return boolean
	 */
	public boolean addBranch(E beginElement, E element, E endElement) {
		final EnumProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final EnumProcess<E> endProcess = recursionNode(firstProcess, endElement);
		if(beginProcess != null && beginProcess.element.equals(beginElement)
				&& endProcess != null && endProcess.element.equals(endElement)){
			linkBranch(beginProcess, element, endProcess);
			return true;
		}
		return false;
	}
	
	/**
	 * 在元素之后添加流程分支枚举元素
	 * @param beginElement 要插入此流程之后的流程元素，即beginElement->element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param endElement 给该入的枚举元素加上下一个流程的流程元素，即element->endElement，必须是已添加的枚举元素
	 * @param processEnum 设置流程节点流转条件
	 * @return boolean
	 */
	public boolean addBranch(E beginElement, E element, E endElement, Process processEnum) {
		final EnumProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final EnumProcess<E> endProcess = recursionNode(firstProcess, endElement);
		if(beginProcess != null && beginProcess.element.equals(beginElement)
				&& endProcess != null && endProcess.element.equals(endElement)){
			linkBranch(beginProcess, element, endProcess, processEnum);
			return true;
		}
		return false;
	}
	
	/**
	 * 设置枚举元素并关连顺序，如果枚举元素不存在则添加
	 * @param beginElement 要设置此流程之后的流程元素，即beginElement->element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param endElement 给该设置的枚举元素加上下一个流程的流程元素，即element->endElement，必须是已添加的枚举元素
	 * @return boolean
	 */
	public boolean setBranch(E beginElement, E element, E endElement) {
		final EnumProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final EnumProcess<E> process = recursionNode(firstProcess, element);
		final EnumProcess<E> endProcess = recursionNode(firstProcess, endElement);
		if(beginProcess != null && beginProcess.element.equals(beginElement)
				&& endProcess != null && endProcess.element.equals(endElement)){
			if(process == null){
				linkBranch(beginProcess, element, endProcess);
			}else{
				setLinkBranch(beginProcess, process, endProcess);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 设置枚举元素并关连顺序，如果枚举元素不存在则添加
	 * @param beginElement 要设置此流程之后的流程元素，即beginElement->element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param endElement 给该设置的枚举元素加上下一个流程的流程元素，即element->endElement，必须是已添加的枚举元素
	 * @param processEnum 设置流程节点流转条件
	 * @return boolean
	 */
	public boolean setBranch(E beginElement, E element, E endElement, Process processEnum) {
		final EnumProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final EnumProcess<E> process = recursionNode(firstProcess, element);
		final EnumProcess<E> endProcess = recursionNode(firstProcess, endElement);
		if(beginProcess != null && beginProcess.element.equals(beginElement)
				&& endProcess != null && endProcess.element.equals(endElement)){
			if(process == null){
				linkBranch(beginProcess, element, endProcess, processEnum);
			}else{
				process.process = processEnum;
				setLinkBranch(beginProcess, process, endProcess);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 给下一个流程设置默认分支流程
	 * @param beginElement 要设置此流程之后的流程元素，即beginElement->element，必须是已添加的枚举元素
	 * @param element 枚举元素，必须是已添加的枚举元素
	 * @return boolean
	 */
	public boolean defaultBranch(E beginElement, E element) {
		final EnumProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		return linkDefaultBranch(beginProcess, element);
	}

	/**
	 * 在开头添加枚举元素
	 * @param element 枚举元素
	 */
	public void addFirst(E element) {
        linkFirst(element);
    }

	/**
	 * 在末尾添加枚举元素
	 * @param element 枚举元素
	 */
	public void addLast(E element) {
        linkLast(element);
    }
	
	/**
	 * 在末尾添加枚举元素
	 * @param element 枚举元素
	 */
	public void add(E element) {
		linkLast(element);
	}
	
	/**
	 * 在末尾添加多个枚举元素
	 * @param element[] 枚举元素数组
	 */
	public void add(E[] elements) {
		for (E element : elements) {
			linkLast(element);
		}
	}
	
	/**
	 * 获取主线流程元素大小
	 * @return int
	 */
	public int size() {
        return size;
    }
	
	/**
	 * 获取流程元素总数
	 * @return int
	 */
	public int count() {
		return modCount;
	}
	
	/**
	 * 获取下一个流程元素
	 * @param element 流程元素
	 * @return Enum
	 */
	public E next(E element) {
		EnumProcess<E> process = recursionNode(firstProcess, element);
		return process.defaultNext.element;
	}
	
	/**
	 * 获取下一个流程元素
	 * @param element 流程元素
	 * @param processEnum 流程节点流转条件
	 * @return Enum
	 */
	public E next(E element, Process processEnum) {
		EnumProcess<E>[] nextProcesses = recursionNode(firstProcess, element).nextProcesses;
		for (EnumProcess<E> enumProcess : nextProcesses) {
			if(enumProcess.process == processEnum){
				return enumProcess.element;
			}
		}
		return null;
	}
	
	/**
	 * 获取上一个流程元素
	 * @param element 流程元素
	 * @return Enum
	 */
	public E pre(E element) {
		EnumProcess<E> process = recursionNode(firstProcess, element);
		return process.defaultPrevious.element;
	}
	
//	/**
//	 * 根据枚举成员获取枚举流程类
//	 * @param element 流程元素
//	 * @return EnumProcess
//	 */
//	public EnumProcess<E> get(E element){
//		return recursionNode(firstProcess, element);
//	}
//
//	/**
//	 * 根据枚举成员获取下一个流程的枚举流程类
//	 * @param element 流程元素
//	 * @return EnumProcess
//	 */
//	public EnumProcess<E> nextProcess(E element) {
//		return recursionNode(firstProcess, element).defaultNext;
//	}
//
//	/**
//	 * 根据枚举成员获取上一个流程的枚举流程类
//	 * @param element 流程元素
//	 * @return EnumProcess
//	 */
//	public EnumProcess<E> preProcess(E element) {
//		return recursionNode(firstProcess, element).defaultPrevious;
//	}
}
