package org.daijie.core.process;

import java.lang.reflect.Array;

import org.daijie.core.factory.IEnumFactory;

/**
 * 树结构算法实现的流程存储集合类
 * @author daijie_jay
 * @since 2018年1月11日
 * @param <E> Enum
 */
@SuppressWarnings("unchecked")
public class TreeEnumProcess<E extends IEnumFactory<E>> implements ProcessHandle<E> {
	
	private static final long serialVersionUID = -6144753183618957937L;

	protected transient int modCount;

	private transient int size;
	
	private transient TreeProcess<E> firstProcess;
	
	private transient TreeProcess<E> lastProcess;

	/**
	 * 在开头添加枚举元素
	 * @param element 枚举元素
	 */
	private void linkFirst(E element) {
        final TreeProcess<E> newFirstProcess = firstProcess;
        TreeProcess<E>[] newFirstProcesses = null;
        if(newFirstProcess != null){
        	newFirstProcesses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, 1);
        	newFirstProcesses[0] = newFirstProcess;
        }
        final TreeProcess<E> newProcess = new TreeProcess<>(null, element, newFirstProcess, null, newFirstProcesses);
        firstProcess = newProcess;
        if (newFirstProcess == null){
        	lastProcess = newProcess;
        }else{
        	TreeProcess<E>[] newPreviousProcessses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, 1);
        	newPreviousProcessses[0] = newProcess;
        	newFirstProcess.defaultPrevious = newProcess;
        	newFirstProcess.previousProcesses = newPreviousProcessses;
        }
        size++;
        modCount++;
    }

	/**
	 * 在末尾添加枚举元素
	 * @param element 枚举元素
	 */	
	private void linkLast(E element) {
        final TreeProcess<E> newLastProcess = lastProcess;
        TreeProcess<E>[] newLastProcesses = null;
        if(newLastProcess != null){
        	newLastProcesses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, 1);
        	newLastProcesses[0] = newLastProcess;
        }
        final TreeProcess<E> newProcess = new TreeProcess<>(newLastProcess, element, null, newLastProcesses, null);
        lastProcess = newProcess;
        if (newLastProcess == null){
        	firstProcess = newProcess;
        }else{
        	TreeProcess<E>[] newNextProcessses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, 1);
        	newNextProcessses[0] = newProcess;
        	newLastProcess.defaultNext = newProcess;
        	newLastProcess.nextProcesses = newNextProcessses;
        }
        size++;
        modCount++;
    }

	/**
	 * 在枚举类之前添加枚举元素
	 * @param element 枚举元素
	 * @param process 要插入此流程之前的枚举类，即element→process
	 */
	@SuppressWarnings("unused")
	private void linkBefore(E element, TreeProcess<E> process) {
        final TreeProcess<E> defaultPrevious = process.defaultPrevious;
        final TreeProcess<E>[] previousProcesses = process.previousProcesses;
		final TreeProcess<E>[] newLastProcesses = (TreeProcess<E>[])Array.newInstance(TreeProcess.class, 1);
		newLastProcesses[0] = process;
        final TreeProcess<E> newProcess = new TreeProcess<>(defaultPrevious, element, process, previousProcesses, newLastProcesses);
        process.defaultPrevious = newProcess;
        if (defaultPrevious == null){
        	firstProcess = newProcess;
        }else{
        	TreeProcess<E>[] newNextProcessses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, 1);
        	newNextProcessses[0] = newProcess;
        	defaultPrevious.defaultNext = newProcess;
        	defaultPrevious.nextProcesses = newNextProcessses;
        }
        size++;
        modCount++;
    }

	/**
	 * 在枚举类之后添加枚举元素
	 * @param element 枚举元素
	 * @param process 要插入此流程之后的枚举类，即process→element
	 */
	@SuppressWarnings("unused")
	private void linkAfter(E element, TreeProcess<E> process) {
		final TreeProcess<E> defaultNext= process.defaultNext;
		final TreeProcess<E>[] nextProcesses = process.nextProcesses;
		final TreeProcess<E>[] newPreviosProcesses = (TreeProcess<E>[])Array.newInstance(TreeProcess.class, 1);
		newPreviosProcesses[0] = process;
		final TreeProcess<E> newProcess = new TreeProcess<>(process, element, defaultNext, newPreviosProcesses, nextProcesses);
		process.defaultNext = newProcess;
		if (defaultNext == null){
			lastProcess = newProcess;
		}else{
        	TreeProcess<E>[] newPreviousProcessses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, 1);
        	newPreviousProcessses[0] = newProcess;
        	defaultNext.defaultPrevious = newProcess;
        	defaultNext.previousProcesses = newPreviousProcessses;
		}
		size++;
		modCount++;
	}

	/**
	 * 在枚举类之后添加流程分支枚举元素并关连顺序
	 * @param beginProcess 要插入此流程之后的枚举类，即beginProcess→element
	 * @param element 枚举元素
	 */
	private void linkBranch(TreeProcess<E> beginProcess, E element) {
		TreeProcess<E>[] nextProcesses = beginProcess.nextProcesses;
		final TreeProcess<E>[] newPreviosProcesses = (TreeProcess<E>[])Array.newInstance(TreeProcess.class, 1);
		newPreviosProcesses[0] = beginProcess;
		final TreeProcess<E> newProcess = new TreeProcess<>(beginProcess, element, null, newPreviosProcesses, null);
		TreeProcess<E>[] newNextProcessses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, nextProcesses.length+1);
		for (int i = 0; i < nextProcesses.length; i++) {
			if(nextProcesses[i].element == element){
				return;
			}
			newNextProcessses[i] = nextProcesses[i];
		}
		nextProcesses = newNextProcessses;
		nextProcesses[newNextProcessses.length - 1] = newProcess;
		beginProcess.nextProcesses = nextProcesses;
		if(beginProcess.defaultNext == null){
			beginProcess.defaultNext = newProcess;
		}
		size++;
		modCount++;
	}
	
	/**
	 * 在枚举类之后添加流程分支枚举元素并关连顺序
	 * @param beginProcess 要插入此流程之后的枚举类，即beginProcess→element
	 * @param element 枚举元素
	 * @param processEnum 设置流程节点流转条件
	 */
	private void linkBranch(TreeProcess<E> beginProcess, E element, Process processEnum) {
		TreeProcess<E>[] nextProcesses = beginProcess.nextProcesses;
		final TreeProcess<E>[] newPreviosProcesses = (TreeProcess<E>[])Array.newInstance(TreeProcess.class, 1);
		newPreviosProcesses[0] = beginProcess;
		final TreeProcess<E> newProcess = new TreeProcess<>(beginProcess, element, null, newPreviosProcesses, null);
		newProcess.process = processEnum;
		TreeProcess<E>[] newNextProcessses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, nextProcesses.length+1);
		for (int i = 0; i < nextProcesses.length; i++) {
			if(nextProcesses[i].element == element){
				return;
			}
			newNextProcessses[i] = nextProcesses[i];
		}
		nextProcesses = newNextProcessses;
		nextProcesses[newNextProcessses.length - 1] = newProcess;
		beginProcess.nextProcesses = nextProcesses;
		if(beginProcess.defaultNext == null){
			beginProcess.defaultNext = newProcess;
		}
		size++;
		modCount++;
	}
	
	/**
	 * 设置枚举类的关联顺序
	 * @param beginProcess 要设置此流程之后的枚举类，即beginProcess→process
	 * @param process 要设置的枚举类
	 */
	private void setLinkBranch(TreeProcess<E> beginProcess, TreeProcess<E> process) {
		TreeProcess<E>[] newNextProcessses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, beginProcess.nextProcesses.length+1);
		for (int i = 0; i < beginProcess.nextProcesses.length; i++) {
			if(beginProcess.nextProcesses[i].element == process.element){
				return;
			}
			newNextProcessses[i] = beginProcess.nextProcesses[i];
		}
		beginProcess.nextProcesses = newNextProcessses;
		beginProcess.nextProcesses[newNextProcessses.length - 1] = process;
		if(beginProcess.defaultNext == null){
			beginProcess.defaultNext = process;
		}
		TreeProcess<E>[] newPreviousProcessses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, process.previousProcesses.length+1);
		for (int i = 0; i < process.previousProcesses.length; i++) {
			newPreviousProcessses[i] = process.previousProcesses[i];
		}
		process.previousProcesses = newPreviousProcessses;
		process.previousProcesses[newPreviousProcessses.length - 1] = beginProcess;
		if(process.defaultPrevious == null){
			process.defaultPrevious = beginProcess;
		}
	}

	/**
	 * 在枚举类之后添加流程分支枚举元素并关连顺序
	 * @param beginProcess 要插入此流程之后的枚举类，即beginProcess→element
	 * @param element 枚举元素
	 * @param endProcess 给该插入的枚举元素加上下一个流程的枚举类，即element→endProcess
	 */
	private void linkBranch(TreeProcess<E> beginProcess, E element, TreeProcess<E> endProcess) {
		TreeProcess<E>[] nextProcesses = beginProcess.nextProcesses;
		final TreeProcess<E>[] newPreviosProcesses = (TreeProcess<E>[])Array.newInstance(TreeProcess.class, 1);
		newPreviosProcesses[0] = beginProcess;
		final TreeProcess<E>[] newLastProcesses = (TreeProcess<E>[])Array.newInstance(TreeProcess.class, 1);
		newLastProcesses[0] = endProcess;
		final TreeProcess<E> newProcess = new TreeProcess<>(beginProcess, element, endProcess, newPreviosProcesses, newLastProcesses);
		TreeProcess<E>[] newNextProcessses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, nextProcesses.length+1);
		for (int i = 0; i < nextProcesses.length; i++) {
			if(nextProcesses[i].element == element){
				return;
			}
			newNextProcessses[i] = nextProcesses[i];
		}
		nextProcesses = newNextProcessses;
		nextProcesses[newNextProcessses.length - 1] = newProcess;
		modCount++;
	}
	
	/**
	 * 在枚举类之后添加流程分支枚举元素并关连顺序
	 * @param beginProcess 要插入此流程之后的枚举类，即beginProcess→element
	 * @param element 枚举元素
	 * @param endProcess 给该插入的枚举元素加上下一个流程的枚举类，即element→endProcess
	 * @param processEnum 设置流程节点流转条件
	 */
	private void linkBranch(TreeProcess<E> beginProcess, E element, TreeProcess<E> endProcess, Process processEnum) {
		TreeProcess<E>[] nextProcesses = beginProcess.nextProcesses;
		final TreeProcess<E>[] newPreviosProcesses = (TreeProcess<E>[])Array.newInstance(TreeProcess.class, 1);
		newPreviosProcesses[0] = beginProcess;
		final TreeProcess<E>[] newLastProcesses = (TreeProcess<E>[])Array.newInstance(TreeProcess.class, 1);
		newLastProcesses[0] = endProcess;
		final TreeProcess<E> newProcess = new TreeProcess<>(beginProcess, element, endProcess, newPreviosProcesses, newLastProcesses);
		newProcess.process = processEnum;
		TreeProcess<E>[] newNextProcessses = (TreeProcess<E>[]) Array.newInstance(TreeProcess.class, nextProcesses.length+1);
		for (int i = 0; i < nextProcesses.length; i++) {
			if(nextProcesses[i].element == element){
				return;
			}
			newNextProcessses[i] = nextProcesses[i];
		}
		nextProcesses = newNextProcessses;
		nextProcesses[newNextProcessses.length - 1] = newProcess;
		modCount++;
	}
	
	/**
	 * 设置枚举类的关联顺序
	 * @param beginProcess 要设置此流程之后的枚举类，即beginProcess→process
	 * @param process 要设置的枚举类
	 * @param endProcess 要设置此流程之前的枚举类，即process→endProcess
	 */
	private void setLinkBranch(TreeProcess<E> beginProcess, TreeProcess<E> process, TreeProcess<E> endProcess) {
		setLinkBranch(beginProcess, process);
		setLinkBranch(process, endProcess);
	}
	
	/**
	 * 给下一个流程设置默认分支流程
	 * @param beginProcess 要设置此流程之后的枚举类，即beginProcess→element
	 * @param element 枚举元素
	 * @return boolean
	 */
	boolean linkDefaultBranch(TreeProcess<E> beginProcess, E element) {
		TreeProcess<E>[] nextProcesses = beginProcess.nextProcesses;
		for (TreeProcess<E> process : nextProcesses) {
			if(process.element.equals(element)){
				process.process = Process.THROUGH;
				beginProcess.defaultNext = process;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据枚举元素查找枚举类
	 * 以树型结构往下查找算法实现
	 * 因流程定义中节点会有退回重新流转，所以为了防止节点重复查找或进入死循环，
	 * 每个节点需要往上查找判断是否有与祖宗节点是同一个节点
	 * @param process 树结构的根节点枚举类
	 * @param element 要查找的枚举元素
	 * @return EnumProcess
	 */
	TreeProcess<E> recursionNode(TreeProcess<E> process, E element) {
		if(process != null){
			if(process.element.equals(element)){
				return process;
			}
			for (TreeProcess<E> enumProcess : process.nextProcesses) {
				if(upwardRecursionNode(process, enumProcess.element) != null){
					return null;
				}
				TreeProcess<E> obj;
				if((obj = recursionNode(enumProcess, element)) != null){
					return obj;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据枚举元素往上起始节点查找枚举类
	 * @param process 树结构的子节点枚举类
	 * @param element 要查找的枚举元素
	 * @return EnumProcess
	 */
	TreeProcess<E> upwardRecursionNode(TreeProcess<E> process, E element) {
		if(process.element.equals(element)){
			return process;
		}
		if(process.defaultPrevious == null){
			return null;
		}
		if(process.defaultPrevious.element.equals(element)){
			return process.defaultPrevious;
		}
		return upwardRecursionNode(process.defaultPrevious, element);
	}

	/**
	 * 在元素之后添加流程分支枚举元素
	 * @param beginElement 要插入此流程之后的流程元素，即beginElement→element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @return boolean
	 */
	public boolean addBranch(E beginElement, E element) {
		final TreeProcess<E> process = recursionNode(firstProcess, beginElement);
		if(process != null && process.element.equals(beginElement)){
			linkBranch(process, element);
			return true;
		}
		return false;
	}
	
	/**
	 * 在元素之后添加流程分支枚举元素
	 * @param beginElement 要插入此流程之后的流程元素，即beginElement→element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param processEnum 设置流程节点流转条件
	 * @return boolean
	 */
	public boolean addBranch(E beginElement, E element, Process processEnum) {
		final TreeProcess<E> process = recursionNode(firstProcess, beginElement);
		if(process != null && process.element.equals(beginElement)){
			linkBranch(process, element, processEnum);
			return true;
		}
		return false;
	}
	
	/**
	 * 设置枚举元素并关连顺序，如果枚举元素不存在则添加
	 * @param beginElement 要设置此流程之后的流程元素，即beginElement→element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @return boolean
	 */
	public boolean setBranch(E beginElement, E element) {
		final TreeProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final TreeProcess<E> process = recursionNode(firstProcess, element);
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
	 * @param beginElement 要设置此流程之后的流程元素，即beginElement→element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param processEnum 设置流程节点流转条件
	 * @return boolean
	 */
	public boolean setBranch(E beginElement, E element, Process processEnum) {
		final TreeProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final TreeProcess<E> process = recursionNode(firstProcess, element);
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
	 * @param beginElement 要插入此流程之后的流程元素，即beginElement→element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param endElement 给该入的枚举元素加上下一个流程的流程元素，即element→endElement，必须是已添加的枚举元素
	 * @return boolean
	 */
	public boolean addBranch(E beginElement, E element, E endElement) {
		final TreeProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final TreeProcess<E> endProcess = recursionNode(firstProcess, endElement);
		if(beginProcess != null && beginProcess.element.equals(beginElement)
				&& endProcess != null && endProcess.element.equals(endElement)){
			linkBranch(beginProcess, element, endProcess);
			return true;
		}
		return false;
	}
	
	/**
	 * 在元素之后添加流程分支枚举元素
	 * @param beginElement 要插入此流程之后的流程元素，即beginElement→element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param endElement 给该入的枚举元素加上下一个流程的流程元素，即element→endElement，必须是已添加的枚举元素
	 * @param processEnum 设置流程节点流转条件
	 * @return boolean
	 */
	public boolean addBranch(E beginElement, E element, E endElement, Process processEnum) {
		final TreeProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final TreeProcess<E> endProcess = recursionNode(firstProcess, endElement);
		if(beginProcess != null && beginProcess.element.equals(beginElement)
				&& endProcess != null && endProcess.element.equals(endElement)){
			linkBranch(beginProcess, element, endProcess, processEnum);
			return true;
		}
		return false;
	}
	
	/**
	 * 设置枚举元素并关连顺序，如果枚举元素不存在则添加
	 * @param beginElement 要设置此流程之后的流程元素，即beginElement→element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param endElement 给该设置的枚举元素加上下一个流程的流程元素，即element→endElement，必须是已添加的枚举元素
	 * @return boolean
	 */
	public boolean setBranch(E beginElement, E element, E endElement) {
		final TreeProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final TreeProcess<E> process = recursionNode(firstProcess, element);
		final TreeProcess<E> endProcess = recursionNode(firstProcess, endElement);
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
	 * @param beginElement 要设置此流程之后的流程元素，即beginElement→element，必须是已添加的枚举元素
	 * @param element 枚举元素
	 * @param endElement 给该设置的枚举元素加上下一个流程的流程元素，即element→endElement，必须是已添加的枚举元素
	 * @param processEnum 设置流程节点流转条件
	 * @return boolean
	 */
	public boolean setBranch(E beginElement, E element, E endElement, Process processEnum) {
		final TreeProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		final TreeProcess<E> process = recursionNode(firstProcess, element);
		final TreeProcess<E> endProcess = recursionNode(firstProcess, endElement);
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
	 * @param beginElement 要设置此流程之后的流程元素，即beginElement→element，必须是已添加的枚举元素
	 * @param element 枚举元素，必须是已添加的枚举元素
	 * @return boolean
	 */
	public boolean defaultBranch(E beginElement, E element) {
		final TreeProcess<E> beginProcess = recursionNode(firstProcess, beginElement);
		return linkDefaultBranch(beginProcess, element);
	}

	/**
	 * 在开头添加枚举元素
	 * @param element 枚举元素
	 * @return boolean
	 */
	public boolean addFirst(E element) {
        linkFirst(element);
        return true;
    }

	/**
	 * 在末尾添加枚举元素
	 * @param element 枚举元素
	 * @return boolean
	 */
	public boolean addLast(E element) {
        linkLast(element);
        return true;
    }
	
	@Override
	public boolean add(E element) {
		final TreeProcess<E> process = recursionNode(firstProcess, element);
		if(process == null){
			linkLast(element);
		}
		return true;
	}
	
	@Override
	public boolean add(E[] elements) {
		for (E element : elements) {
			final TreeProcess<E> process = recursionNode(firstProcess, element);
			if(process == null){
				linkLast(element);
			}
		}
		return true;
	}
	
	/**
	 * 添加流程元素节点
	 * @param from 来源元素
	 * @param to 去向元素
	 * @param processEnum 流程节点流转条件
	 * @return boolean
	 */
	public boolean add(E from, E to, Process processEnum) {
		add(from);
		setBranch(from, to, processEnum);
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
	
	public E next(E element) {
		TreeProcess<E> process = recursionNode(firstProcess, element);
		if(process == null || process.defaultNext == null){
			return null;
		}
		return process.defaultNext.element;
	}
	
	@Override
	public E next(E element, Process processEnum) {
		TreeProcess<E>[] nextProcesses = recursionNode(firstProcess, element).nextProcesses;
		for (TreeProcess<E> enumProcess : nextProcesses) {
			if(enumProcess.process == processEnum){
				return enumProcess.element;
			}
		}
		return null;
	}
	
	public E pre(E element) {
		TreeProcess<E> process = recursionNode(firstProcess, element);
		if(process == null || process.defaultPrevious == null){
			return null;
		}
		return process.defaultPrevious.element;
	}

	@Override
	public E pre(E element, Process processEnum) {
		TreeProcess<E>[] previousProcesses = recursionNode(firstProcess, element).previousProcesses;
		for (TreeProcess<E> enumProcess : previousProcesses) {
			for (TreeProcess<E> nextProcess : enumProcess.nextProcesses) {
				if(nextProcess.process == processEnum){
					return enumProcess.element;
				}
			}
		}
		return null;
	}
}
