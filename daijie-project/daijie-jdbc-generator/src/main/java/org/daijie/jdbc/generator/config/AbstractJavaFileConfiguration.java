package org.daijie.jdbc.generator.config;


import com.google.common.collect.Lists;
import org.daijie.jdbc.generator.code.JavaFieldInfo;
import org.daijie.jdbc.generator.executor.Generator;
import org.daijie.jdbc.matedata.TableMateData;

import java.util.List;

/**
 * 生成文件的基本配置
 * @author daijie
 * @since 2019/9/16
 */
public abstract class AbstractJavaFileConfiguration implements FileConfiguration {

    /**
     * 包路径
     */
    private String targetPackage;

    /**
     * 项目的跟路径
     */
    private String targetProject;

    /**
     * 代码行字符串
     */
    private List<String> codeLines = Lists.newArrayList();

    /**
     * 表元数据
     */
    private TableMateData tableMateData;

    public AbstractJavaFileConfiguration(String targetPackage, String targetProject) {
        this.targetPackage = targetPackage;
        this.targetProject = targetProject;
    }

    /**
     * 获取包路径
     * @return 包路径
     */
    public String getTargetPackage() {
        return targetPackage;
    }

    /**
     * 获取项目的跟路径
     * @return 项目的跟路径
     */
    public String getTargetProject() {
        return targetProject;
    }

    @Override
    public void execute() {
        Generator generator = initGenerator();
        Object result = generator.execute();
        if (result instanceof List) {
            this.codeLines = (List<String>) result;
        }
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();
        for (String codeLine : this.codeLines) {
            code.append(codeLine);
        }
        return code.toString();
    }

    @Override
    public String getSuffix() {
        return ".java";
    }

    /**
     * 初始化一个生成器
     * @return 返回生成器
     */
    public abstract Generator initGenerator();

    /**
     * 添加类属性注解的规则实现
     * @param javaFieldInfo 某个类属性信息
     */
    public abstract void addFieldAnnotationRule(JavaFieldInfo javaFieldInfo);

    public TableMateData getTableMateData() {
        return tableMateData;
    }

    public void setTableMateData(TableMateData tableMateData) {
        this.tableMateData = tableMateData;
    }
}
