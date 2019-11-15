package org.daijie.jdbc.generator.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.daijie.jdbc.generator.code.*;
import org.daijie.jdbc.generator.executor.Generator;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 生成实体类文件基本配置
 * @author daijie
 * @since 2019/9/16
 */
public abstract class ModelFileConfiguration extends AbstractJavaFileConfiguration {

    /**
     * 引入的包路径
     */
    private Set<String> importPackages;

    /**
     * 实体类类注解
     */
    private List<JavaAnnotationInfo> classAnnotations;

    /**
     * 实体类字段注解
     */
    private List<JavaAnnotationInfo> fieldAnnotations;

    /**
     * 实体类主键字段注解
     */
    private List<JavaAnnotationInfo> keyFieldAnnotations;

    /**
     * 是否重写toString方法
     */
    private boolean isOverrideToString;

    /**
     * 是否重写Equats方法，如果重写也会重写hasCode方法
     */
    private boolean isOverrideEquats;

    /**
     * 是否使用lombok注解
     */
    private boolean isLombok;

    /**
     * 是否序列化
     */
    private boolean isSerializeble;

    public ModelFileConfiguration(String targetPackage, String targetProject) {
        super(targetPackage, targetProject);
        this.importPackages = Sets.newHashSet();
        this.classAnnotations = Lists.newArrayList();
        this.fieldAnnotations = Lists.newArrayList();
        this.keyFieldAnnotations = Lists.newArrayList();
        this.isOverrideToString = false;
        this.isOverrideEquats = false;
        this.isLombok = false;
        this.isSerializeble = false;
    }

    public ModelFileConfiguration(String targetPackage, String targetProject, Boolean isOverrideToString, Boolean isOverrideEquats, Boolean isLombok, Boolean isSerializeble) {
        super(targetPackage, targetProject);
        this.importPackages = Sets.newHashSet();
        this.classAnnotations = Lists.newArrayList();
        this.fieldAnnotations = Lists.newArrayList();
        this.keyFieldAnnotations = Lists.newArrayList();
        this.isOverrideToString = isOverrideToString;
        this.isOverrideEquats = isOverrideEquats;
        this.isLombok = isLombok;
        this.isSerializeble = isSerializeble;
    }

    public void addClassAnnotation(String className) {
        this.classAnnotations.add(new JavaAnnotationInfo(CodeGeneratorUtil.shortClassName(className), null));
        this.importPackages.add(className);
    }

    public List<JavaAnnotationInfo> getClassAnnotations() {
        return classAnnotations;
    }

    public void addFieldAnnotation(String className) {
        this.fieldAnnotations.add(new JavaAnnotationInfo(CodeGeneratorUtil.shortClassName(className), null));
        this.importPackages.add(className);
    }

    public List<JavaAnnotationInfo> getFieldAnnotations() {
        return fieldAnnotations;
    }

    public void addKeyFieldAnnotation(String className) {
        this.keyFieldAnnotations.add(new JavaAnnotationInfo(CodeGeneratorUtil.shortClassName(className), null));
        this.importPackages.add(className);
    }

    public List<JavaAnnotationInfo> getKeyFieldAnnotations() {
        return keyFieldAnnotations;
    }

    public boolean isOverrideToString() {
        return isOverrideToString;
    }

    public boolean isOverrideEquats() {
        return isOverrideEquats;
    }

    public Set<String> getImportPackages() {
        return importPackages;
    }

    public boolean isLombok() {
        return isLombok;
    }

    public boolean isSerializeble() {
        return isSerializeble;
    }

    @Override
    public String getFileName() {
        return CodeGeneratorUtil.UnderlineToClassName(this.getTableMateData().getName());
    }

    @Override
    public Generator initGenerator() {
        Map<String, String[]> interfacePackages = Maps.newHashMap();
        if (this.isSerializeble()) {
            this.importPackages.add("java.io.Serializable");
            interfacePackages.put("Serializable", null);
        }
        JavaClassInfo javaClassInfo = new JavaClassInfo(this.getTargetPackage(), this.getImportPackages(), interfacePackages, null, JavaClassInfo.COMMON, this.getTableMateData(), this.isLombok());
        javaClassInfo.addClassAnnotation(this.classAnnotations);

        for (JavaFieldInfo javaFieldInfo : javaClassInfo.getJavaFieldInfos()) {
            if (!this.keyFieldAnnotations.isEmpty()) {
                addKeyFieldAnnotationRule(javaFieldInfo);
            }
            if (!this.fieldAnnotations.isEmpty()) {
                addFieldAnnotationRule(javaFieldInfo);
            }
        }
        if (this.isOverrideToString()) {
            generateHashCode(javaClassInfo);
            generateToString(javaClassInfo);
        }
        if (this.isOverrideEquats()) {
            generateEquals(javaClassInfo);
        }
        return new ModelCodeGenerator(javaClassInfo);
    }

    /**
     * 添加类主键属性注解的规则实现
     * @param javaFieldInfo 某个类属性信息
     */
    private void addKeyFieldAnnotationRule(JavaFieldInfo javaFieldInfo) {
        if (this.getTableMateData().getPrimaryKey().getName().equals(javaFieldInfo.getName())) {
            javaFieldInfo.getJavaAnnotationInfos().addAll(this.keyFieldAnnotations);
        }
    }

    /**
     * 生成toString方法
     * @param javaClassInfo 已初始化的类信息
     */
    private void generateToString(JavaClassInfo javaClassInfo) {
        List<String> contents = Lists.newArrayList();
        JavaMethodInfo javaMethodInfo = new JavaMethodInfo("toString", "String", Maps.newHashMap(), false, JavaClassInfo.VISIBLE_PUBLIC, contents);
        javaMethodInfo.addJavaAnnotationInfo(new JavaAnnotationInfo("Override"));
        contents.add("StringBuilder sb = new StringBuilder();\n");
        contents.add("sb.append(getClass().getSimpleName());\n");
        contents.add("sb.append(\" [\");\n");
        contents.add("sb.append(\"Hash = \").append(hashCode());\n");
        for (JavaFieldInfo javaFieldInfo : javaClassInfo.getJavaFieldInfos()) {
            contents.add("sb.append(\", "+javaFieldInfo.getName()+"=\").append(this."+javaFieldInfo.getName()+");\n");
        }
        contents.add("sb.append(\"]\");\n");
        contents.add("return sb.toString();\n");
        javaClassInfo.addMethodInfo(javaMethodInfo);
    }

    /**
     * 生成hashCode方法
     * @param javaClassInfo 已初始化的类信息
     */
    private void generateHashCode(JavaClassInfo javaClassInfo) {
        List<String> contents = Lists.newArrayList();
        JavaMethodInfo javaMethodInfo = new JavaMethodInfo("hashCode", "int", Maps.newHashMap(), false, JavaClassInfo.VISIBLE_PUBLIC, contents);
        javaMethodInfo.addJavaAnnotationInfo(new JavaAnnotationInfo("Override"));
        contents.add("final int prime = 31;\n");
        contents.add("int result = 1;\n");
        for (JavaFieldInfo javaFieldInfo : javaClassInfo.getJavaFieldInfos()) {
            contents.add("result = prime * result + ((this."+javaFieldInfo.getName()+" == null) ? 0 : this."+javaFieldInfo.getName()+".hashCode());\n");
        }
        contents.add("return result;\n");
        javaClassInfo.addMethodInfo(javaMethodInfo);
    }

    /**
     * 生成equals方法
     * @param javaClassInfo 已初始化的类信息
     */
    private void generateEquals(JavaClassInfo javaClassInfo) {
        Map<String, String> agrs = Maps.newHashMap();
        agrs.put("that", "Object");
        List<String> contents = Lists.newArrayList();
        JavaMethodInfo javaMethodInfo = new JavaMethodInfo("equals", "boolean", agrs, false, JavaClassInfo.VISIBLE_PUBLIC, contents);
        javaMethodInfo.addJavaAnnotationInfo(new JavaAnnotationInfo("Override"));
        contents.add("if (this == that) {\n");
        contents.add("\treturn true;\n");
        contents.add("}\n");
        contents.add("if (that == null) {\n");
        contents.add("\treturn false;\n");
        contents.add("}\n");
        contents.add("if (getClass() != that.getClass()) {\n");
        contents.add("\treturn false;\n");
        contents.add("}\n");
        contents.add(javaClassInfo.getClassName()+" other = ("+javaClassInfo.getClassName()+") that;\n");
        int index = 1;
        Iterator<JavaFieldInfo> it = javaClassInfo.getJavaFieldInfos().iterator();
        while (it.hasNext()) {
            JavaFieldInfo javaFieldInfo = it.next();
            String name = CodeGeneratorUtil.firstCharToUpperCase(javaFieldInfo.getName());
            if (index == 1) {
                contents.add("return (this.get"+name+"() == null ? other.get"+name+"() == null : this.get"+name+"().equals(other.get"+name+"()))\n");
            } else {
                if (it.hasNext()) {
                    contents.add("\t&& (this.get" + name + "() == null ? other.get" + name + "() == null : this.get" + name + "().equals(other.get" + name + "()))\n");
                } else {
                    contents.add("\t&& (this.get" + name + "() == null ? other.get" + name + "() == null : this.get" + name + "().equals(other.get" + name + "()));\n");
                }
            }
            index ++;
        }
        javaClassInfo.addMethodInfo(javaMethodInfo);
    }
}
