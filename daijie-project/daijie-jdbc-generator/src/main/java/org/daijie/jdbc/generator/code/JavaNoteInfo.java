package org.daijie.jdbc.generator.code;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * JAVA类代码注释信息
 * @author daijie
 * @since 2019/9/16
 */
public class JavaNoteInfo {

    /**
     * 注释类型：1，类注释；2，字段注释；3，方法注释；4，行注释
     */
    public static final String NOTE_CLASS = "1";
    public static final String NOTE_FIELD = "2";
    public static final String NOTE_METHOD = "3";
    public static final String NOTE_ROW = "4";

    /**
     * 注释内容
     */
    private List<String> contents = Lists.newArrayList();

    /**
     * 返回类注释内容，名称和内容以","号分隔
     */
    private String returnContent;

    /**
     * 方法参数注释内容，名称和内容以","号分隔
     */
    private List<String> methodArgs = Lists.newArrayList();

    /**
     * 方法异常注释内容，名称和内容以","号分隔
     */
    private List<String> methodExceptions = Lists.newArrayList();

    /**
     * 类或方法泛型注释内容，名称和内容以","号分隔
     */
    private List<String> genericities = Lists.newArrayList();

    public JavaNoteInfo(String[] contents) {
        this.contents = Arrays.asList(contents);
    }

    public JavaNoteInfo(String[] contents, String returnContent) {
        this.contents = Arrays.asList(contents);
        this.returnContent = returnContent;
    }

    public JavaNoteInfo(String[] contents, String returnContent, String[] methodArgs, String[] methodExceptions, String[] genericities) {
        this.contents = Arrays.asList(contents);
        this.returnContent = returnContent;
        this.methodArgs = Arrays.asList(methodArgs);
        this.methodExceptions = Arrays.asList(methodExceptions);
        this.genericities = Arrays.asList(genericities);
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public String getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(String returnContent) {
        this.returnContent = returnContent;
    }

    public List<String> getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(List<String> methodArgs) {
        this.methodArgs = methodArgs;
    }

    public List<String> getMethodExceptions() {
        return methodExceptions;
    }

    public void setMethodExceptions(List<String> methodExceptions) {
        this.methodExceptions = methodExceptions;
    }

    public List<String> getGenericities() {
        return genericities;
    }

    public void setGenericities(List<String> genericities) {
        this.genericities = genericities;
    }
}
