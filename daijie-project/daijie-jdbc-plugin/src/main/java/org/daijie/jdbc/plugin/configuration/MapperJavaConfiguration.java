package org.daijie.jdbc.plugin.configuration;

import java.util.Set;

/**
 * 实体类文件属性配置
 * @author daijie
 * @since 2019/9/22
 */
public class MapperJavaConfiguration {

    /**
     * 自定义的mapper配置类
     */
    private String mapperClass;

    /**
     * 包路径
     */
    private String targetPackage;

    /**
     * 项目的跟路径
     */
    private String targetProject;

    /**
     * 继承类和接口类的路径
     */
    private Set<String> interfacePackages;

    public String getMapperClass() {
        return mapperClass;
    }

    public void setMapperClass(String mapperClass) {
        this.mapperClass = mapperClass;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getTargetProject() {
        return targetProject;
    }

    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }

    public Set<String> getInterfacePackages() {
        return interfacePackages;
    }

    public void setInterfacePackages(Set<String> interfacePackages) {
        this.interfacePackages = interfacePackages;
    }
}
