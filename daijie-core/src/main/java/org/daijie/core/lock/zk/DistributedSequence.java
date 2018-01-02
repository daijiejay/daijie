package org.daijie.core.lock.zk;

/**
 * zookeeper操作的抽象类
 * @author daijie_jay
 * @since 2018年1月2日
 */
public interface DistributedSequence {

	/**
	 * 存储目录
	 * @param sequenceName 目录名称
	 * @return Long
	 */
    public Long sequence(String sequenceName);
}
