package org.daijie.lock.Exception;

/**
 * 锁异常类
 * @author daijie
 * @since 2019/5/21
 */
public class LockException extends RuntimeException {

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }
}
