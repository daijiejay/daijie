package org.daijie.jdbc.generator.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.daijie.jdbc.generator.code.*;
import org.daijie.jdbc.generator.executor.Generator;

import java.util.List;
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

    public ModelFileConfiguration(String targetPackage, String targetProject, boolean isOverrideToString, boolean isOverrideEquats, boolean isLombok, boolean isSerializeble) {
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
        Set<String> interfacePackages = Sets.newHashSet();
        if (this.isSerializeble()) {
            this.importPackages.add("java.io.Serializable");
            interfacePackages.add("Serializable");
        }
        JavaClassInfo javaClassInfo = new JavaClassInfo(this.getTargetPackage(), this.getImportPackages(), interfacePackages, null, JavaClassInfo.COMMON, this.getTableMateData());
        javaClassInfo.addClassAnnotation(this.classAnnotations);

        for (JavaFieldInfo javaFieldInfo : javaClassInfo.getJavaFieldInfos()) {
            if (!this.keyFieldAnnotations.isEmpty()) {
                addKeyFieldAnnotationRule(javaFieldInfo);
            }
            if (!this.fieldAnnotations.isEmpty()) {
                addFieldAnnotationRule(javaFieldInfo);
            }
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
}
