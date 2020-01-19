package org.daijie.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * 读取配置文件工具类
 * @author daijie
 * @since 2019-11-14
 */
public class PropertiesUtils {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    public static String readValue(String filePath,String key) {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(filePath));
            if (in != null) {
                prop.load(in);
            }
            return prop.getProperty(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }



    /**
     * 获取配置文件
     * @param filePath 配置文件路径
     * @return 返回配置文件信息
     */
    public static Properties getProperties(String filePath) {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            File file = new File(filePath);
            if (file.canRead()) {
                in = new BufferedInputStream(new FileInputStream(file));
            } else {
                in = PropertiesUtils.class.getClassLoader().getResourceAsStream(filePath);
            }
            if (in != null) {
                prop.load(in);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return prop;
    }
}
