package org.daijie.jdbc.scripting;

import org.daijie.jdbc.executor.SqlExecutor;

import java.io.*;
import java.util.regex.Pattern;

/**
 * SQL文件读取内容实现
 */
public class SqlFileReaderImpl extends AbstractSqlScript implements SqlFileReader {

    private final String CLASS_PATH = "classpath:";

    /**
     * classPathName SQL文件名
     */
    private String fileName;

    public SqlFileReaderImpl(String fileName) {
        this.fileName = fileName;
        readSqlFile();
    }

    @Override
    public void readSqlFile() {
        StringBuilder sql = new StringBuilder();
        BufferedReader reader = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
            if (this.fileName.startsWith(this.CLASS_PATH)) {
                inputStream = getClass().getClassLoader().getResourceAsStream(this.fileName.replaceAll(this.CLASS_PATH, ""));
            } else {
                inputStream = new FileInputStream(this.fileName);
            }
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                if (!tempString.startsWith("--")) {
                    sql.append(tempString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
        }
        this.sql = sql.toString();
    }

    @Override
    public SqlExecutor.Type getScriptType() {
        return SqlExecutor.Type.BATCH;
    }
}
