package org.daijie.jdbc.transaction;

/**
 * 当前事务状态
 */
public enum StatusType {

    /**
     * 可提交
     */
    COMMIT(1),

    /**
     * 不可提交
     */
    NOT_COMMIT(0);

    private int status;

    StatusType(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
