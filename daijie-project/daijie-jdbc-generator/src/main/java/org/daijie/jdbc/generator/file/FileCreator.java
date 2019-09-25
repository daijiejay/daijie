package org.daijie.jdbc.generator.file;

/**
 * 文件构建
 * @author daijie
 * @since 2019/9/16
 */
public interface FileCreator {

    /**
     * 创建文件
     * @param path 文件路径
     * @param code 文件内容
     * @param fileName 文件名
     */
    void createFile(String path, String code, String fileName);
}
