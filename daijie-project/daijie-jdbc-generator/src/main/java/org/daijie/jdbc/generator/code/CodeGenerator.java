package org.daijie.jdbc.generator.code;

import org.daijie.jdbc.generator.executor.Generator;

import java.util.List;

/**
 * 代码生成器
 * @author daijie
 * @since 2019/9/16
 */
public abstract class CodeGenerator implements Generator {

    /**
     * 执行代码生成
     * 生成为一个代码字符串集合，每一行代码集合长度加1
     * @return 返回生成代码的字符串集
     */
    public abstract List<String> generate();

    @Override
    public Object execute() {
        return generate();
    }
}
