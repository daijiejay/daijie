package org.daijie.jdbc.generator.code;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * JAVA类属性代码生成器
 * @author daijie
 * @since 2019/9/16
 */
public class JavaMethodGenerator extends CodeGenerator {

    private final JavaMethodInfo javaMethodInfo;

    public JavaMethodGenerator(JavaMethodInfo javaMethodInfo) {
        this.javaMethodInfo = javaMethodInfo;
    }

    @Override
    public List<String> generate() {
        if (this.javaMethodInfo.isModelSetterOrGetter()) {
            if (this.javaMethodInfo.getAgrs().isEmpty()) {
                return this.generateGetter();
            } else {
                return this.generateSetter();
            }
        } else {
            CodeString codeString = new CodeString();
            codeString.append(this.javaMethodInfo.getVisibleDecorate());
            if (this.javaMethodInfo.isStatic()) {
                codeString.append(" static ");
            }
            if (StringUtils.isNotEmpty(this.javaMethodInfo.getReturnType())) {
                codeString.append(this.javaMethodInfo.getReturnType()).append(" ");
            } else {
                codeString.append("void ");
            }
            codeString.append(this.javaMethodInfo.getName()).append("(");
            Iterator<String> iterator = this.javaMethodInfo.getAgrs().keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                codeString.append(this.javaMethodInfo.getAgrs().get(key)).append(" ").append(key);
                if (iterator.hasNext()) {
                    codeString.append(", ");
                }
            }
            codeString.append(") {");
            codeString.andCodeLine("\n");
            codeString.andCodeLine(this.javaMethodInfo.getContent());

            codeString.andCodeLine("}");
            return codeString.getCodeLines();
        }
    }

    private List<String> generateSetter() {
        CodeString codeString = new CodeString();
        codeString.append(this.javaMethodInfo.getVisibleDecorate()).append(" void set");
        codeString.append(CodeGeneratorUtil.firstCharToUpperCase(this.javaMethodInfo.getName())).append("(");
        codeString.append(this.generateMehtodArgs());
        codeString.append(") {");
        codeString.andCodeLine("\n");
        codeString.append("\tthis.").append(this.javaMethodInfo.getName()).append(" = ").append(this.javaMethodInfo.getName()).append(";");
        codeString.andCodeLine("\n");
        codeString.andCodeLine("}");
        return codeString.getCodeLines();
    }

    private List<String> generateGetter() {
        CodeString codeString = new CodeString();
        codeString.append(this.javaMethodInfo.getVisibleDecorate()).append(" ").append(this.javaMethodInfo.getReturnType()).append(" get");
        codeString.append(CodeGeneratorUtil.firstCharToUpperCase(this.javaMethodInfo.getName())).append("() {");
        codeString.andCodeLine("\n");
        codeString.append("\treturn this.").append(this.javaMethodInfo.getName()).append(";");
        codeString.andCodeLine("\n");
        codeString.andCodeLine("}");
        return codeString.getCodeLines();
    }

    private String generateMehtodArgs() {
        StringBuilder code = new StringBuilder();
        Iterator<String> iterator = this.javaMethodInfo.getAgrs().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            code.append(this.javaMethodInfo.getAgrs().get(key)).append(" ").append(key);
            if (iterator.hasNext()) {
                code.append(", ");
            }
        }
        return code.toString();
    }
}
