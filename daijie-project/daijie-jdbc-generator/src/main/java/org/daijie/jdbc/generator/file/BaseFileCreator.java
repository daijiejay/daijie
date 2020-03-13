package org.daijie.jdbc.generator.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 创建文件的基础实现
 * @author daijie
 * @since 2019/9/16
 */
public class BaseFileCreator implements FileCreator {

    private Logger logger = LoggerFactory.getLogger(BaseFileCreator.class);

    @Override
    public void createFile(String path, String code, String fileName) {
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()+"/"+ fileName));
            bufferedWriter.write(code);
        } catch (IOException e) {
            logger.error("创建文件失败：", e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    logger.error("创建文件失败：", e);
                }
            }
        }

    }
}
