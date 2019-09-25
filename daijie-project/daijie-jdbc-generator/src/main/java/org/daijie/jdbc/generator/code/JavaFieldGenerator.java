package org.daijie.jdbc.generator.code;

import java.util.List;

/**
 * JAVA类属性代码生成器
 * @author daijie
 * @since 2019/9/16
 */
public class JavaFieldGenerator extends CodeGenerator {

    private final JavaFieldInfo javaFieldInfo;

    public JavaFieldGenerator(JavaFieldInfo javaFieldInfo) {
        this.javaFieldInfo = javaFieldInfo;
    }

    @Override
    public List<String> generate() {
        CodeString codeString = new CodeString();
        codeString.append(this.javaFieldInfo.getVisibleDecorate());
        if (this.javaFieldInfo.isStatic()) {
            codeString.append(" static");
        }
        if (this.javaFieldInfo.isFinal()) {
            codeString.append(" final");
        }
        codeString.append(" ").append(this.javaFieldInfo.getType()).append(" ").append(this.javaFieldInfo.getName()).append(";");
        return codeString.getCodeLines();
    }
}
