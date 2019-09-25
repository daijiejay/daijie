package org.daijie.jdbc.generator.code;

import org.apache.commons.lang3.StringUtils;
import org.daijie.jdbc.scripting.SqlSpelling;

import java.util.List;

/**
 * JAVA类代码生成器
 * @author daijie
 * @since 2019/9/16
 */
public class JavaClassGenerator extends CodeGenerator {

    /**
     * 是否内部类
     */
    private boolean isInnerClass;

    /**
     * JAVA类信息
     */
    private JavaClassInfo javaClassInfo;

    public JavaClassGenerator(JavaClassInfo javaClassInfo) {
        this.javaClassInfo = javaClassInfo;
    }

    public JavaClassGenerator(boolean isInnerClass, JavaClassInfo javaClassInfo) {
        this.isInnerClass = isInnerClass;
        this.javaClassInfo = javaClassInfo;
    }

    @Override
    public List<String> generate() {
        CodeString codeString = new CodeString();
        if (!this.isInnerClass) {
            generateHeader(codeString);
            codeString.andCodeLine("\n");
        }
        generateAnnotation(codeString, this.javaClassInfo.getJavaAnnotationInfos(), "");
        if (JavaClassInfo.ABSTRACT.equals(this.javaClassInfo.getClassDecorate())) {
            codeString.append("public abstract class ").append(this.javaClassInfo.getClassName());
        } else if (JavaClassInfo.INTERFACE.equals(this.javaClassInfo.getClassDecorate())) {
            codeString.append("public interface ").append(this.javaClassInfo.getClassName());
        } else {
            codeString.append("public class ").append(this.javaClassInfo.getClassName());
        }
        if (StringUtils.isNotEmpty(this.javaClassInfo.getParentPackage())) {
            codeString.append(" extends ").append(this.javaClassInfo.getParentPackage());
        }
        if (!this.javaClassInfo.getInterfacePackages().isEmpty()) {
            if (JavaClassInfo.INTERFACE.equals(this.javaClassInfo.getClassDecorate())) {
                codeString.append(" extends ").append(new SqlSpelling().collectionToCommaDelimitedString(this.javaClassInfo.getInterfacePackages()));
            } else {
                codeString.append(" implements ").append(new SqlSpelling().collectionToCommaDelimitedString(this.javaClassInfo.getInterfacePackages()));
            }
        }
        codeString.append(" {");
        codeString.andCodeLine("\n");
        if (!this.javaClassInfo.getJavaFieldInfos().isEmpty()) {
            codeString.andCodeLine("\n");
            for (JavaFieldInfo javaFieldInfo : this.javaClassInfo.getJavaFieldInfos()) {
                generateAnnotation(codeString, javaFieldInfo.getJavaAnnotationInfos(), "\t");
                codeString.append("\t").append(new JavaFieldGenerator(javaFieldInfo).generate().get(0)).append("\n");
                codeString.andCodeLine("\n");
            }
        }
        if (!this.javaClassInfo.getJavaMethodInfos().isEmpty()) {
            for (JavaMethodInfo javaMethodInfo : this.javaClassInfo.getJavaMethodInfos()) {
                generateAnnotation(codeString, javaMethodInfo.getJavaAnnotationInfos(), "\t");
                codeString.andCodeLines("\t", new JavaMethodGenerator(javaMethodInfo).generate());
                codeString.andCodeLine("\n");
                codeString.andCodeLine("\n");
            }
        }
        codeString.andCodeLine("}");
        return codeString.getCodeLines();
    }

    private void generateHeader(CodeString codeString) {
        if (StringUtils.isNotEmpty(this.javaClassInfo.getTargetPackage())) {
            codeString.append("package ").append(this.javaClassInfo.getTargetPackage()).append(";\n");
            codeString.andCodeLine("\n");
        }
        for (String inportPackge : this.javaClassInfo.getImportPackages()) {
            codeString.append("import ").append(inportPackge).append(";");
            codeString.andCodeLine("\n");
        }
    }

    private void generateAnnotation(CodeString codeString, List<JavaAnnotationInfo> javaAnnotationInfos, String space) {
        if (!javaAnnotationInfos.isEmpty()) {
            for (JavaAnnotationInfo javaAnnotationInfo : javaAnnotationInfos) {
                codeString.append(space).append(new JavaAnnotationGenerator(javaAnnotationInfo).generate().get(0));
                codeString.andCodeLine("\n");
            }
        }
    }
}
