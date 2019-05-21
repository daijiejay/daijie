package org.daijie.lock;

/**
 * 调用锁执行回调
 * @author daijie_jay
 * @since 2017年11月24日
 */
public interface Callback {

	/**
	 * 获取锁回调执行的方法
	 * @return Object 返回参数自由定义
	 * @throws InterruptedException 抛出异常
	 */
    public Object onGetLock() throws InterruptedException;

    /**
	 * 锁超时回调执行的方法
	 * @return Object 返回参数自由定义
	 * @throws InterruptedException 抛出异常
	 */
    public Object onTimeout() throws InterruptedException;
    
    /**
     * 获取锁时出现异常执行的方法
     * @param exception 捕获的异常
     * @return Object 返回参数自由定义
     */
    default public Object onError(Exception exception){
    	return null;
    }
}
