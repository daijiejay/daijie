package org.daijie.jdbc.generator.code;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * JAVA类方法信息
 * @author daijie
 * @since 2019/9/16
 */
public class JavaMethodInfo {

    /**
     * 方法名
     */
    private String name;

    /**
     * 返回类类型
     */
    private String returnType;

    /**
     * 方法参数集，key是参数变量名，value是参数类类型
     */
    private Map<String, String> agrs = Maps.newHashMap();

    /**
     * 是否静态修饰
     */
    private boolean isStatic;

    /**
     * 可见度修饰
     */
    private String visibleDecorate;

    /**
     * 方法体内容
     */
    private String content;

    /**
     * 是否实体类的set或get方法
     */
    private boolean isModelSetterOrGetter;

    /**
     * 方法注解集
     */
    private List<JavaAnnotationInfo> javaAnnotationInfos = Lists.newArrayList();

    /**
     * 方法注释
     */
    private JavaNoteInfo javaNoteInfo;

    public JavaMethodInfo(String name, String returnType, Map<String, String> agrs, boolean isStatic, String visibleDecorate, String content) {
        this.name = name;
        this.returnType = returnType;
        this.agrs = agrs;
        this.isStatic = isStatic;
        this.visibleDecorate = visibleDecorate;
        this.content = content;
    }

    public JavaMethodInfo(String name, String returnType, Map<String, String> agrs, boolean isStatic, String visibleDecorate, String content, List<JavaAnnotationInfo> javaAnnotationInfos) {
        this.name = name;
        this.returnType = returnType;
        this.agrs = agrs;
        this.isStatic = isStatic;
        this.visibleDecorate = visibleDecorate;
        this.content = content;
        this.javaAnnotationInfos = javaAnnotationInfos;
    }

    public JavaMethodInfo(String name, String returnType, Map<String, String> agrs, boolean isStatic, String visibleDecorate, boolean isModelSetterOrGetter, JavaNoteInfo javaNoteInfo) {
        this.name = name;
        this.returnType = returnType;
        this.agrs = agrs;
        this.isStatic = isStatic;
        this.visibleDecorate = visibleDecorate;
        this.isModelSetterOrGetter = isModelSetterOrGetter;
        this.javaNoteInfo = javaNoteInfo;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public Map<String, String> getAgrs() {
        return agrs;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public String getVisibleDecorate() {
        return visibleDecorate;
    }

    public String getContent() {
        return content;
    }

    public boolean isModelSetterOrGetter() {
        return isModelSetterOrGetter;
    }

    public void addJavaAnnotationInfo(JavaAnnotationInfo javaAnnotationInfo) {
        this.javaAnnotationInfos.add(javaAnnotationInfo);
    }

    public List<JavaAnnotationInfo> getJavaAnnotationInfos() {
        return javaAnnotationInfos;
    }

    public JavaNoteInfo getJavaNoteInfo() {
        return javaNoteInfo;
    }
}
