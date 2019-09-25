package org.daijie.jdbc.generator.config;


import org.daijie.jdbc.generator.code.HtmlGenerator;
import org.daijie.jdbc.generator.executor.Generator;
import org.daijie.jdbc.matedata.TableMateData;

import java.util.List;

/**
 * 生成文件的基本配置
 * @author daijie
 * @since 2019/9/16
 */
public abstract class AbstractHtmlFileConfiguration implements FileConfiguration {

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 代码行字符串
     */
    private String code = null;

    /**
     * 表元数据
     */
    private List<TableMateData> tableMateDatas;

    public AbstractHtmlFileConfiguration(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        Generator generator = initGenerator();
        code = (String) generator.execute();
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public String getSuffix() {
        return ".html";
    }

    /**
     * 初始化一个生成器
     * @return 返回生成器
     */
    public Generator initGenerator() {
        return new HtmlGenerator(this.tableMateDatas);
    }

    public String getPath() {
        return path;
    }

    public List<TableMateData> getTableMateDatas() {
        return tableMateDatas;
    }

    public void setTableMateDatas(List<TableMateData> tableMateDatas) {
        this.tableMateDatas = tableMateDatas;
    }
}
