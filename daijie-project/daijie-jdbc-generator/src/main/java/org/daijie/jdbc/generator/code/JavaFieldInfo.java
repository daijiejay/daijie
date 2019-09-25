package org.daijie.jdbc.generator.code;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * JAVA类属性信息
 * @author daijie
 * @since 2019/9/16
 */
public class JavaFieldInfo {

    /**
     * 属性变量名
     */
    private String name;

    /**
     * 是否静态修饰
     */
    private boolean isStatic;

    /**
     * 是否常量修饰
     */
    private boolean isFinal;

    /**
     * 变量类类型
     */
    private String type;

    /**
     * 变量默认值
     */
    private Object value;

    /**
     * 可见度修饰
     */
    private String visibleDecorate;

    /**
     * 属性注解集
     */
    private List<JavaAnnotationInfo> javaAnnotationInfos = Lists.newArrayList();

    public JavaFieldInfo(String name, boolean isStatic, boolean isFinal, String type, Object value, String visibleDecorate) {
        this.name = name;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.type = type;
        this.value = value;
        this.visibleDecorate = visibleDecorate;
    }

    public JavaFieldInfo(String name, boolean isStatic, boolean isFinal, String type, Object value, String visibleDecorate, List<JavaAnnotationInfo> javaAnnotationInfos) {
        this.name = name;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.type = type;
        this.value = value;
        this.visibleDecorate = visibleDecorate;
        this.javaAnnotationInfos = javaAnnotationInfos;
    }

    public String getName() {
        return name;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public String getVisibleDecorate() {
        return visibleDecorate;
    }

    public void addJavaAnnotationInfo(JavaAnnotationInfo javaAnnotationInfo) {
        this.javaAnnotationInfos.add(javaAnnotationInfo);
    }

    public List<JavaAnnotationInfo> getJavaAnnotationInfos() {
        return javaAnnotationInfos;
    }
}
