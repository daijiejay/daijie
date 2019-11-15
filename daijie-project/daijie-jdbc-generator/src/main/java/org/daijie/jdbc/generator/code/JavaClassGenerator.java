package org.daijie.jdbc.generator.code;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;
import org.daijie.jdbc.scripting.SqlSpelling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JAVA类代码生成器
 * @author daijie
 * @since 2019/9/16
 */
public class JavaClassGenerator extends CodeGenerator {

    private static final Logger log = LoggerFactory.getLogger(JavaClassGenerator.class);

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
        generateNote(codeString, this.javaClassInfo.getJavaNoteInfo(), "", JavaNoteInfo.NOTE_CLASS);
        generateAnnotation(codeString, this.javaClassInfo.getJavaAnnotationInfos(), "");
        if (JavaClassInfo.ABSTRACT.equals(this.javaClassInfo.getClassDecorate())) {
            codeString.append("public abstract class ").append(this.javaClassInfo.getClassName());
        } else if (JavaClassInfo.INTERFACE.equals(this.javaClassInfo.getClassDecorate())) {
            codeString.append("public interface ").append(this.javaClassInfo.getClassName());
        } else {
            codeString.append("public class ").append(this.javaClassInfo.getClassName());
        }
        if (CollUtil.isNotEmpty(this.javaClassInfo.getParentPackages())) {
            generateInheritClass(codeString, this.javaClassInfo.getParentPackages(), true);

        }
        if (CollUtil.isNotEmpty(this.javaClassInfo.getInterfacePackages())) {
            generateInheritClass(codeString, this.javaClassInfo.getInterfacePackages(), false);
        }
        codeString.append(" {");
        codeString.andCodeLine("\n");
        if (!this.javaClassInfo.getJavaFieldInfos().isEmpty()) {
            codeString.andCodeLine("\n");
            for (JavaFieldInfo javaFieldInfo : this.javaClassInfo.getJavaFieldInfos()) {
                generateNote(codeString, javaFieldInfo.getJavaNoteInfo(), "\t", JavaNoteInfo.NOTE_FIELD);
                generateAnnotation(codeString, javaFieldInfo.getJavaAnnotationInfos(), "\t");
                codeString.append("\t").append(new JavaFieldGenerator(javaFieldInfo).generate().get(0)).append("\n");
                codeString.andCodeLine("\n");
            }
        }
        if (!this.javaClassInfo.getJavaMethodInfos().isEmpty()) {
            for (JavaMethodInfo javaMethodInfo : this.javaClassInfo.getJavaMethodInfos()) {
                generateNote(codeString, javaMethodInfo.getJavaNoteInfo(), "\t", JavaNoteInfo.NOTE_METHOD);
                generateAnnotation(codeString, javaMethodInfo.getJavaAnnotationInfos(), "\t");
                codeString.andCodeLines("\t", new JavaMethodGenerator(javaMethodInfo).generate());
                codeString.andCodeLine("\n");
                codeString.andCodeLine("\n");
            }
        }
        codeString.andCodeLine("}");
        return codeString.getCodeLines();
    }

    /**
     * 生成类头部的导入包代码
     * @param codeString
     */
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

    /**
     * 生成多条注解代码
     * @param codeString 代码字符串
     * @param javaAnnotationInfos 多个注解信息
     * @param space 使用"\n"占用多少个空格
     */
    private void generateAnnotation(CodeString codeString, List<JavaAnnotationInfo> javaAnnotationInfos, String space) {
        if (!javaAnnotationInfos.isEmpty()) {
            for (JavaAnnotationInfo javaAnnotationInfo : javaAnnotationInfos) {
                codeString.append(space).append(new JavaAnnotationGenerator(javaAnnotationInfo).generate().get(0));
                codeString.andCodeLine("\n");
            }
        }
    }

    /**
     * 生成注释代码
     * @param codeString 代码字符串
     * @param javaNoteInfo 注释信息
     * @param space 使用"\n"占用多少个空格
     * @param noteType 注释类型
     */
    private void generateNote(CodeString codeString, JavaNoteInfo javaNoteInfo, String space, String noteType) {
        if (javaNoteInfo != null) {
            codeString.andCodeLines(space, new JavaNoteGenerator(javaNoteInfo, noteType).generate());
        }
    }

    /**
     * 生成继承class名，包括类泛型
     * @param codeString 代码字符串
     * @param genericities 类和泛型
     * @param commonClass 普通泛型类的父类
     */
    private void generateInheritClass(CodeString codeString, Map<String, String[]> genericities, boolean commonClass) {
        if (commonClass || JavaClassInfo.INTERFACE.equals(this.javaClassInfo.getClassDecorate())) {
            codeString.append(" extends ");
        } else {
            codeString.append(" implements ");
        }
        Iterator<String> it = genericities.keySet().iterator();
        while (it.hasNext()) {
            String className = it.next();
            String[] genericity = genericities.get(className);
            codeString.append(className);
            if (genericity != null && genericity.length > 0) {
                codeString.append("<").append(new SqlSpelling().collectionToCommaDelimitedString(Arrays.asList(genericity))).append(">");
            }
            if (it.hasNext()) {
                codeString.append(", ");
            }
        }
    }
}
