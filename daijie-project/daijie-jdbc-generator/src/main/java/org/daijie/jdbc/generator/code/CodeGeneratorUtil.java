package org.daijie.jdbc.generator.code;

/**
 * 代码生成工具类
 * @author daijie
 * @since 2019/9/16
 */
public class CodeGeneratorUtil {

    /**
     * 驼峰转下划线
     * @param field 字段名
     * @return 返回下划线字段名
     */
    public static String humpToUnderscore(String field) {
        return field.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    /**
     * 下划线转驼峰
     * @param field 字段名
     * @return 返回驼峰字段名
     */
    public static String UnderlineToHump(String field){
        StringBuilder result = new StringBuilder();
        String str[] = field.split("_");
        for(String s : str){
            if (!field.contains("_")) {
                result.append(s);
                continue;
            }
            if(result.length() == 0){
                result.append(s.toLowerCase());
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 第一个字母大写
     * @param name 字符串
     * @return 返回第一个字母大写的字符串
     */
    public static String firstCharToUpperCase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * 下划线转驼峰类名
     * @param name 字符串
     * @return 返回一个驼峰类名
     */
    public static String UnderlineToClassName(String name) {
        return firstCharToUpperCase(UnderlineToHump(name));
    }

    /**
     * 获取短类名
     * @param name 字符串
     * @return 返回一个短类名
     */
    public static String shortClassName(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    /**
     * 路径点转斜杠
     * @param path 路径
     * @return 返回一个路径
     */
    public static String pathSpotToSlash(String path) {
        return path.replaceAll("\\.", "/");
    }

    /**
     * 路径斜杠转点
     * @param path 路径
     * @return 返回一个路径
     */
    public static String pathSlashToSpot(String path) {
        return path.replaceAll("/", ".");
    }
}
