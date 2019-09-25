package org.daijie.jdbc.generator.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author daijie
 * @since 2019/9/16
 */
public class BaseFileCreator implements FileCreator {

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
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
