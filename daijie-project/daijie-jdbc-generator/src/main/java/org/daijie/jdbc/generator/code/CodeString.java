package org.daijie.jdbc.generator.code;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 代码拼接工具
 * @author daijie
 * @since 2019/9/16
 */
public class CodeString {

    /**
     * 代码行集合
     */
    private List<String> codeLines = Lists.newArrayList();

    /**
     * 单行代码拼接缓存
     */
    private StringBuilder code;

    /**
     * 单行代码拼接
     * @param code 代码块
     * @return 返回代码拼接工具
     */
    protected CodeString append(String code) {
        if (this.code == null) {
            this.code = new StringBuilder();
        }
        this.code.append(code);
        return this;
    }

    /**
     * 添加整行代码
     * @param code 代码块
     */
    protected void andCodeLine(String code) {
        andCodeEnd();
        this.codeLines.add(code);
    }

    /**
     * 添加多行代码
     * @param blankCharacter 空格符
     * @param codeLines 多行代码块
     */
    protected void andCodeLines(String blankCharacter, List<String> codeLines) {
        andCodeEnd();
        for (String codeLine : codeLines) {
            this.codeLines.add(blankCharacter + codeLine);
        }
    }

    /**
     * 获取代码行集合
     * @return 返回代码行集合
     */
    public List<String> getCodeLines() {
        andCodeEnd();
        return this.codeLines;
    }

    /**
     * 代码拼接结束
     */
    protected void andCodeEnd() {
        if (code != null && code.length() > 0) {
            this.codeLines.add(this.code.toString());
            this.code.delete( 0, code.length());
        }
    }
}
