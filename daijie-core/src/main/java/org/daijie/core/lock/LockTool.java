package org.daijie.core.lock;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 使用zookeeper锁的工具
 * @author daijie_jay
 * @date 2017年11月24日
 */
public class LockTool {

	private static DistributedLockTemplate distributedLockTemplate;

	@Autowired
	public void setDistributedLockTemplate(DistributedLockTemplate distributedLockTemplate) {
		LockTool.distributedLockTemplate = distributedLockTemplate;
	}

    /**
     *
     * @param lockId 锁id(对应业务唯一ID)
     * @param timeout 设置需要锁住的时间（算法需要执行的时间，执行完成后自动放开锁），单位毫秒
     * @param callback 回调函数
     * @return
     */
	public static Object execute(String lockId, int timeout, Callback callback) {
		return distributedLockTemplate.execute(lockId, timeout, callback);
	}
}
