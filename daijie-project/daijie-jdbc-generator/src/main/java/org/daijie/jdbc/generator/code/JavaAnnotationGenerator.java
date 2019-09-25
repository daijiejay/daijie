package org.daijie.jdbc.generator.code;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JAVA类代码生成器
 * @author daijie
 * @since 2019/9/16
 */
public class JavaAnnotationGenerator extends CodeGenerator {

    /**
     * JAVA类信息
     */
    private JavaAnnotationInfo javaAnnotationInfo;

    public JavaAnnotationGenerator(JavaAnnotationInfo javaAnnotationInfo) {
        this.javaAnnotationInfo = javaAnnotationInfo;
    }

    @Override
    public List<String> generate() {
        CodeString codeString = new CodeString();
        codeString.append("@").append(this.javaAnnotationInfo.getName());
        Map<String, Object> members = this.javaAnnotationInfo.getMembers();
        Iterator<String> iterator = members.keySet().iterator();
        if (iterator.hasNext()) {
            codeString.append("(");
        }
        while (iterator.hasNext()) {
            String key = iterator.next();
            codeString.append(key).append(" = ");
            if (members.get(key) instanceof Byte || members.get(key) instanceof Boolean || members.get(key) instanceof Integer
                    || members.get(key) instanceof Double || members.get(key) instanceof Long || members.get(key) instanceof Short
                    || members.get(key) instanceof Float || members.get(key) instanceof Long) {
                codeString.append(members.get(key).toString());
            } else if (members.get(key) instanceof Character) {
                codeString.append("'").append(members.get(key).toString()).append(",");
            } else {
                String value = members.get(key).toString();
                codeString.append("\"").append(value).append("\"");
            }
            if (iterator.hasNext()) {
                codeString.append(", ");
            }
        }
        if (!members.isEmpty()) {
            codeString.append(")");
        }
        return codeString.getCodeLines();
    }
}
