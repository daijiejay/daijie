package org.daijie.core.lock;

/**
 * 分布式锁模板类
 * @author daijie_jay
 * @date 2017年11月24日
 */
public interface DistributedLockTemplate {

    /**
     *
     * @param lockId 锁id(对应业务唯一ID)
     * @param timeout 设置需要锁住的时间（算法需要执行的时间，执行完成后自动放开锁），单位毫秒
     * @param callback 回调函数
     * @return
     */
    public Object execute(String lockId,int timeout,Callback callback);
}
