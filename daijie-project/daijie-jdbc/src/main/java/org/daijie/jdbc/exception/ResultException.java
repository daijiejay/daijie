package org.daijie.jdbc.exception;

/**
 * 查询结果映射异常
 * @author daijie_jay
 * @since 2020年02月22日
 */
public class ResultException extends RuntimeException {

    private final String fieldType;

    public ResultException(final String fieldType, final Exception cause) {
        super("查询结果映射对象" + fieldType + "失败！", cause);
        this.fieldType = fieldType;
    }
}
