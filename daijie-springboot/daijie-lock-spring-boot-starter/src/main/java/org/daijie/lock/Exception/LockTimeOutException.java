package org.daijie.lock.Exception;

/**
 * 锁获取超时异常类
 * @author daijie
 * @since 2019/5/21
 */
public class LockTimeOutException extends RuntimeException {

    public LockTimeOutException(String message) {
        super(message);
    }
}
