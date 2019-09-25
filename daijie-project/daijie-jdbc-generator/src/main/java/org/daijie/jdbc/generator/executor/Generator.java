package org.daijie.jdbc.generator.executor;

/**
 * 生成器基本属性类
 * @author daijie
 * @since 2019/9/16
 */
public interface Generator {

    /**
     * 执行生成器
     * @return 返回具体生成结果
     */
    Object execute();
}
