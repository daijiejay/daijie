package org.daijie.jdbc.generator.config;


/**
 * 生成文件的基本配置
 * @author daijie
 * @since 2019/9/16
 */
public interface FileConfiguration {

    /**
     * 文件名
     * @return 文件名
     */
    String getFileName();

    /**
     * 文件后缀名
     * @return 文件后缀名
     */
    String getSuffix();

    /**
     * 执行任务
     */
    void execute();

    /**
     * 获取生成的代码
     * @return 返回生成的代码
     */
    String getCode();
}
