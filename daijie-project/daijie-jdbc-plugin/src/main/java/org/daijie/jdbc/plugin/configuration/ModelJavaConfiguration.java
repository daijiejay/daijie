package org.daijie.jdbc.plugin.configuration;

/**
 * 实体类文件属性配置
 * @author daijie
 * @since 2019/9/22
 */
public class ModelJavaConfiguration {

    /**
     * 自定义的model配置类
     */
    private String modeConfigurationClass;

    /**
     * 包路径
     */
    private String targetPackage;

    /**
     * 项目的跟路径
     */
    private String targetProject;

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

    public String getModeConfigurationClass() {
        return modeConfigurationClass;
    }

    public void setModeConfigurationClass(String modeConfigurationClass) {
        this.modeConfigurationClass = modeConfigurationClass;
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

    public boolean isOverrideToString() {
        return isOverrideToString;
    }

    public void setOverrideToString(boolean overrideToString) {
        isOverrideToString = overrideToString;
    }

    public boolean isOverrideEquats() {
        return isOverrideEquats;
    }

    public void setOverrideEquats(boolean overrideEquats) {
        isOverrideEquats = overrideEquats;
    }

    public boolean isLombok() {
        return isLombok;
    }

    public void setLombok(boolean lombok) {
        isLombok = lombok;
    }

    public boolean isSerializeble() {
        return isSerializeble;
    }

    public void setSerializeble(boolean serializeble) {
        isSerializeble = serializeble;
    }
}
