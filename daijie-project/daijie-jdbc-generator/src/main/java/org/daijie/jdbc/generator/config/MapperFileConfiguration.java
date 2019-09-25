package org.daijie.jdbc.generator.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.daijie.jdbc.generator.code.CodeGeneratorUtil;
import org.daijie.jdbc.generator.code.JavaAnnotationInfo;
import org.daijie.jdbc.generator.code.JavaClassGenerator;
import org.daijie.jdbc.generator.code.JavaClassInfo;
import org.daijie.jdbc.generator.executor.Generator;

import java.util.List;
import java.util.Set;

/**
 * 生成mapper类文件基本配置
 * @author daijie
 * @since 2019/9/16
 */
public abstract class MapperFileConfiguration extends AbstractJavaFileConfiguration {

    /**
     * 引入的包路径
     */
    private Set<String> importPackages;

    /**
     * 实体类类注解
     */
    private List<JavaAnnotationInfo> classAnnotations;

    /**
     * 继承类和接口类的路径
     */
    private Set<String> interfacePackages;

    public MapperFileConfiguration(String targetPackage, String targetProject) {
        super(targetPackage, targetProject);
        this.importPackages = Sets.newHashSet();
        this.classAnnotations = Lists.newArrayList();
        this.interfacePackages = Sets.newHashSet();
    }

    public void addClassAnnotation(String className) {
        this.classAnnotations.add(new JavaAnnotationInfo(CodeGeneratorUtil.shortClassName(className), null));
        this.importPackages.add(className);
    }

    public List<JavaAnnotationInfo> getClassAnnotations() {
        return classAnnotations;
    }

    public void addInterfacePackages(String className) {
        this.interfacePackages.add(CodeGeneratorUtil.shortClassName(className));
        this.importPackages.add(className);
    }

    public Set<String> getInterfacePackages() {
        return interfacePackages;
    }

    public Set<String> getImportPackages() {
        return importPackages;
    }

    @Override
    public String getFileName() {
        return CodeGeneratorUtil.UnderlineToClassName(this.getTableMateData().getName()) + "Mapper";
    }

    @Override
    public Generator initGenerator() {
        JavaClassInfo javaClassInfo = new JavaClassInfo(this.getTargetPackage(), this.getImportPackages(), interfacePackages, null, JavaClassInfo.INTERFACE, this.getFileName());
        return new JavaClassGenerator(javaClassInfo);
    }
}
