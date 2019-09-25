package org.daijie.jdbc.generator.code;

import java.util.List;

/**
 * 实体类代码生成器
 * @author daijie
 * @since 2019/9/16
 */
public class ModelCodeGenerator extends CodeGenerator {

    private final JavaClassInfo javaClassInfo;

    public ModelCodeGenerator(JavaClassInfo javaClassInfo) {
        this.javaClassInfo = javaClassInfo;
    }

    @Override
    public List<String> generate() {
        JavaClassGenerator javaClassGenerator = new JavaClassGenerator(this.javaClassInfo);
        return javaClassGenerator.generate();
    }
}
