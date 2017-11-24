package org.daijie.core.lock;

/**
 * 调用锁执行回调
 * @author daijie_jay
 * @date 2017年11月24日
 */
public interface Callback {

    public Object onGetLock() throws InterruptedException;

    public Object onTimeout() throws InterruptedException;
}
