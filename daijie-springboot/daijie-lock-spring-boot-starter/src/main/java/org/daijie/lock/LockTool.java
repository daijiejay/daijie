package org.daijie.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 分布式锁工具类
 * 提供静态方法调用，执行锁
 * @author daijie_jay
 * @since 2017年11月24日
 */
public class LockTool {

	private static LockCreator lockCreator;

	@Autowired
	public void setDistributedLockTemplate(LockCreator lockCreator) {
		LockTool.lockCreator = lockCreator;
	}

    /**
     *
     * @param lockId 锁id(对应业务唯一ID)
     * @param timeout 设置需要锁住的时间（算法需要执行的时间，执行完成后自动放开锁），单位毫秒
     * @param callback 回调函数
     * @return Object 返回函数自由定义的返回类型
     */
	public static Object execute(String lockId, int timeout, Callback callback) {
		final Logger logger = LoggerFactory.getLogger(LockTool.class);
		if(lockCreator == null){
			logger.error("未开启分布式锁，请加入@EnableZkLock或@EnableRedisLock注解开启");
		}
		return lockCreator.invoke(lockId, timeout, callback);
	}
}
