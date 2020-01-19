package org.daijie.jdbc.generator.code;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.daijie.jdbc.matedata.ColumnMateData;
import org.daijie.jdbc.matedata.TableMateData;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JAVA类信息
 * @author daijie
 * @since 2019/9/16
 */
public class JavaClassInfo {

    /**
     * 类修饰，普通类，抽象类，接口类
     */
    public static final String COMMON = "";
    public static final String ABSTRACT = "abstract";
    public static final String INTERFACE = "interface";

    /**
     * 类成员属性或方法可见度修饰
     */
    public static final String VISIBLE_PUBLIC = "public";
    public static final String VISIBLE_PROTECT = "protect";
    public static final String VISIBLE_DEFAULT = "default";
    public static final String VISIBLE_PRIVATE = "private";

    /**
     * 当前类包路径
     */
    private String targetPackage;

    /**
     * 引入的包路径
     */
    private Set<String> importPackages;

    /**
     * 继承类和接口类的路径，value是类泛型
     */
    private Map<String, String[]> interfacePackages;

    /**
     * 父类路径，value是类泛型
     */
    private Map<String, String[]> parentPackages;

    /**
     * 类修饰关键字
     */
    private String classDecorate = JavaClassInfo.COMMON;

    /**
     * 类名
     */
    private String className;

    /**
     * JAVA类属性信息
     */
    private List<JavaFieldInfo> javaFieldInfos = Lists.newArrayList();

    /**
     * JAVA类方法信息
     */
    private List<JavaMethodInfo> javaMethodInfos = Lists.newArrayList();

    /**
     * 类注解集
     */
    private List<JavaAnnotationInfo> javaAnnotationInfos = Lists.newArrayList();

    /**
     * 类注释
     */
    private JavaNoteInfo javaNoteInfo;

    /**
     * JAVA类信息初始化，初始化一个类的信息
     * @param targetPackage 当前类包路径
     * @param importPackages 引入的包路径
     * @param interfacePackages 继承类和接口类的路径
     * @param parentPackages 父类路径
     * @param classDecorate 类修饰关键字
     * @param className 类名
     */
    public JavaClassInfo(String targetPackage, Set<String> importPackages, Map<String, String[]> interfacePackages, Map<String, String[]> parentPackages, String classDecorate, String className) {
        this.targetPackage = targetPackage;
        this.importPackages = importPackages;
        this.interfacePackages = interfacePackages;
        this.parentPackages = parentPackages;
        this.classDecorate = classDecorate;
        this.className = className;
    }

    /**
     * JAVA类信息初始化，初始化一个类的信息
     * @param targetPackage 当前类包路径
     * @param importPackages 引入的包路径
     * @param interfacePackages 继承类和接口类的路径
     * @param parentPackages 父类路径
     * @param classDecorate 类修饰关键字
     * @param className 类名
     * @param javaAnnotationInfos 类注解信息
     */
    public JavaClassInfo(String targetPackage, Set<String> importPackages, Map<String, String[]> interfacePackages, Map<String, String[]> parentPackages, String classDecorate, String className, List<JavaAnnotationInfo> javaAnnotationInfos) {
        this.targetPackage = targetPackage;
        this.importPackages = importPackages;
        this.interfacePackages = interfacePackages;
        this.parentPackages = parentPackages;
        this.classDecorate = classDecorate;
        this.className = className;
        this.javaAnnotationInfos = javaAnnotationInfos;
    }

    /**
     * JAVA类信息初始化，用来根据表元数据生成java代码
     * @param targetPackage 当前类包路径
     * @param importPackages 引入的包路径
     * @param interfacePackages 继承类和接口类的路径
     * @param parentPackages 父类路径
     * @param classDecorate 类修饰关键字
     * @param tableMateData 表元数据
     * @param isLombok 是否使用lombok注解
     */
    public JavaClassInfo(String targetPackage, Set<String> importPackages, Map<String, String[]> interfacePackages, Map<String, String[]> parentPackages, String classDecorate, TableMateData tableMateData, boolean isLombok) {
        this.importPackages = Sets.newHashSet();
        this.targetPackage = targetPackage;
        this.interfacePackages = interfacePackages;
        this.parentPackages = parentPackages;
        this.classDecorate = classDecorate;
        this.className = CodeGeneratorUtil.UnderlineToClassName(tableMateData.getName());
        if (importPackages != null) {
            this.importPackages.addAll(importPackages);
        }
        this.javaNoteInfo = new JavaNoteInfo(new String[]{tableMateData.getRemarks()});
        this.importPackages.add("javax.persistence.*");
        this.javaAnnotationInfos.add(new JavaAnnotationInfo("Table").addMember("name", tableMateData.getName()));
        if (isLombok) {
            this.importPackages.add("lombok.Data");
            this.javaAnnotationInfos.add(new JavaAnnotationInfo("Data"));
        }
        MysqlTypeConvertor typeConvertor = new MysqlTypeConvertor();
        for (ColumnMateData columnMateData : tableMateData.getColumns().values()) {
            String name = CodeGeneratorUtil.UnderlineToHump(columnMateData.getName());
            String fieldType = typeConvertor.databaseTypeToJavaType(columnMateData.getColumnType());
            String shortFieldType = CodeGeneratorUtil.shortClassName(fieldType);
            if (!fieldType.contains("java.lang")) {
                this.importPackages.add(fieldType);
            }
            JavaNoteInfo fieldNote = new JavaNoteInfo(new String[]{columnMateData.getRemarks()});
            List<JavaAnnotationInfo> javaAnnotationInfos = Lists.newArrayList();
            if (tableMateData.getPrimaryKey().getName().equals(columnMateData.getName())) {
                javaAnnotationInfos.add(new JavaAnnotationInfo("Id"));
                List<JavaFieldInfo> javaFieldInfos = Lists.newArrayList();
                javaFieldInfos.add(new JavaFieldInfo(name, false, false, shortFieldType, null, JavaClassInfo.VISIBLE_PUBLIC, javaAnnotationInfos, fieldNote));
                javaFieldInfos.addAll(this.javaFieldInfos);
                this.javaFieldInfos = javaFieldInfos;
            } else {
                this.javaFieldInfos.add(new JavaFieldInfo(name, false, false, shortFieldType, null, JavaClassInfo.VISIBLE_PUBLIC, javaAnnotationInfos, fieldNote));
            }
            javaAnnotationInfos.add(new JavaAnnotationInfo("Column").addMember("name", columnMateData.getName()));

            if (!isLombok) {
                Map<String, String> agrs = Maps.newHashMap();
                agrs.put(name, shortFieldType);
                JavaNoteInfo setterNote = new JavaNoteInfo(new String[]{"设置" + columnMateData.getRemarks()}, null, new String[]{name + "," + columnMateData.getRemarks()}, new String[]{}, new String[]{});
                this.javaMethodInfos.add(new JavaMethodInfo(name, null, agrs, false, JavaClassInfo.VISIBLE_PUBLIC, true, setterNote));
                String returnContent = columnMateData.getName();
                if (StringUtils.isNotEmpty(columnMateData.getRemarks())) {
                    returnContent += "," + columnMateData.getRemarks();
                }
                JavaNoteInfo getterNote = new JavaNoteInfo(new String[]{"获取" + columnMateData.getRemarks()}, returnContent);
                this.javaMethodInfos.add(new JavaMethodInfo(name, shortFieldType, Maps.newHashMap(), false, JavaClassInfo.VISIBLE_PUBLIC, true, getterNote));
            }
        }
    }

    /**
     * 添加类上的注解信息
     * @param javaAnnotationInfos java注解信息
     */
    public void addClassAnnotation(List<JavaAnnotationInfo> javaAnnotationInfos) {
        this.javaAnnotationInfos.addAll(javaAnnotationInfos);
    }

    /**
     * 添加类上的注解信息
     * @param className 注解类名
     * @param args 注解参数集
     */
    public void addClassAnnotation(String className, Map<String, Object> args) {
        this.importPackages.add(className);
        this.javaAnnotationInfos.add(new JavaAnnotationInfo(CodeGeneratorUtil.shortClassName(className), args));
    }

    /**
     * 添加类属性上的注解信息
     * @param javaAnnotationInfos java注解信息
     */
    public void addFieldAnnotation(List<JavaAnnotationInfo> javaAnnotationInfos) {
        this.javaAnnotationInfos.addAll(javaAnnotationInfos);
    }

    /**
     * 添加类属性上的注解信息
     * @param className 注解类名
     * @param args 注解参数集
     */
    public void addFieldAnnotation(String className, Map<String, Object> args) {
        this.importPackages.add(className);
        for (JavaFieldInfo javaFieldInfo : this.javaFieldInfos) {
            javaFieldInfo.addJavaAnnotationInfo(new JavaAnnotationInfo(CodeGeneratorUtil.shortClassName(className), args));
        }
    }

    /**
     * 添加类方法上的注解信息
     * @param javaAnnotationInfos java注解信息
     */
    public void addMethodAnnotation(List<JavaAnnotationInfo> javaAnnotationInfos) {
        this.javaAnnotationInfos.addAll(javaAnnotationInfos);
    }

    /**
     * 添加类方法上的注解信息
     * @param className 注解类名
     * @param args 注解参数集
     */
    public void addMethodAnnotation(String className, Map<String, Object> args) {
        this.importPackages.add(className);
        for (JavaMethodInfo javaMethodInfo : this.javaMethodInfos) {
            javaMethodInfo.addJavaAnnotationInfo(new JavaAnnotationInfo(CodeGeneratorUtil.shortClassName(className), args));
        }
    }

    public void addMethodInfo(JavaMethodInfo javaMethodInfo) {
        this.javaMethodInfos.add(javaMethodInfo);
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public Set<String> getImportPackages() {
        return importPackages;
    }

    public Map<String, String[]> getInterfacePackages() {
        return interfacePackages;
    }

    public Map<String, String[]> getParentPackages() {
        return parentPackages;
    }

    public String getClassDecorate() {
        return classDecorate;
    }

    public String getClassName() {
        return className;
    }

    public List<JavaFieldInfo> getJavaFieldInfos() {
        return javaFieldInfos;
    }

    public List<JavaMethodInfo> getJavaMethodInfos() {
        return javaMethodInfos;
    }

    public List<JavaAnnotationInfo> getJavaAnnotationInfos() {
        return javaAnnotationInfos;
    }

    public JavaNoteInfo getJavaNoteInfo() {
        return javaNoteInfo;
    }
}
