package org.daijie.core.lock;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 分布式锁工具类
 * 提供静态方法调用，执行锁
 * @author daijie_jay
 * @since 2017年11月24日
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
     * @return Object 返回函数自由定义的返回类型
     */
	public static Object execute(String lockId, int timeout, Callback callback) {
		return distributedLockTemplate.execute(lockId, timeout, callback);
	}
}
