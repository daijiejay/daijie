package org.daijie.jdbc.generator.code;

import com.google.common.collect.Lists;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * JAVA类注释代码生成器
 * @author daijie
 * @since 2019/9/22
 */
public class JavaNoteGenerator extends CodeGenerator {

    private final JavaNoteInfo javaNoteInfo;

    private final String noteType;

    public JavaNoteGenerator(JavaNoteInfo javaNoteInfo, String noteType) {
        this.javaNoteInfo = javaNoteInfo;
        this.noteType = noteType;
    }

    @Override
    public List<String> generate() {
        switch (this.noteType) {
            case JavaNoteInfo.NOTE_CLASS:
                return generateClassNote();
            case JavaNoteInfo.NOTE_FIELD:
                return generateFieldNote();
            case JavaNoteInfo.NOTE_METHOD:
                return generateMethodNote();
            case JavaNoteInfo.NOTE_ROW:
                return generateRowNote();
        }
        return Lists.newArrayList();
    }

    /**
     * 生成类注释
     * @return 返回生成代码
     */
    private List<String> generateClassNote() {
        CodeString codeString = new CodeString();
        codeString.andCodeLine("/**\n");
        for (String content : this.javaNoteInfo.getContents()) {
            codeString.append(" * ").append(content);
            codeString.andCodeLine("\n");
        }
        codeString.append(" * @author ").append(System.getenv().get("USERNAME"));
        codeString.andCodeLine("\n");
        codeString.append(" * @date ").append(DateUtil.formatDate(new Date(), "yyyy年MM月dd日"));
        codeString.andCodeLine("\n");
        for (String genericity : this.javaNoteInfo.getGenericities()) {
            String[] note = genericity.split(",");
            codeString.append(" * @param ").append(note[0]).append(" ").append(note[1]);
            codeString.andCodeLine("\n");
        }
        codeString.andCodeLine(" */\n");
        return codeString.getCodeLines();
    }

    /**
     * 生成类属性注释
     * @return 返回生成代码
     */
    private List<String> generateFieldNote() {
        CodeString codeString = new CodeString();
        codeString.andCodeLine("/**\n");
        for (String content : this.javaNoteInfo.getContents()) {
            codeString.append(" * ").append(content);
            codeString.andCodeLine("\n");
        }
        codeString.andCodeLine(" */\n");
        return codeString.getCodeLines();
    }

    /**
     * 生成类方法注释
     * @return 返回生成代码
     */
    private List<String> generateMethodNote() {
        CodeString codeString = new CodeString();
        codeString.andCodeLine("/**\n");
        for (String content : this.javaNoteInfo.getContents()) {
            codeString.append(" * ").append(content);
            codeString.andCodeLine("\n");
        }
        for (String methodArg : this.javaNoteInfo.getMethodArgs()) {
            String[] note = methodArg.split(",");
            if (note.length == 2) {
                codeString.append(" * @param ").append(note[0]).append(" ").append(note[1]);
            } else {
                codeString.append(" * @param ").append(note[0]);
            }
            codeString.andCodeLine("\n");
        }
        for (String genericity : this.javaNoteInfo.getGenericities()) {
            String[] note = genericity.split(",");
            if (note.length == 2) {
                codeString.append(" * @param ").append(note[0]).append(" ").append(note[1]);
            } else {
                codeString.append(" * @param ").append(note[0]);
            }
            codeString.andCodeLine("\n");
        }
        if (StringUtils.isNotEmpty(this.javaNoteInfo.getReturnContent())) {
            if (this.javaNoteInfo.getReturnContent().indexOf(",") != -1) {
                String[] note = this.javaNoteInfo.getReturnContent().split(",");
                codeString.append(" * @return ").append(note[0]).append(" ").append(note[1]);
            } else {
                codeString.append(" * @return ").append(this.javaNoteInfo.getReturnContent());
            }
            codeString.andCodeLine("\n");
        }
        for (String methodException : this.javaNoteInfo.getMethodExceptions()) {
            String[] note = methodException.split(",");
            if (note.length == 2) {
                codeString.append(" * @throws ").append(note[0]).append(" ").append(note[1]);
            } else {
                codeString.append(" * @throws ").append(note[0]);
            }
            codeString.andCodeLine("\n");
        }
        codeString.andCodeLine(" */\n");
        return codeString.getCodeLines();
    }

    /**
     * 生成类行注释
     * @return 返回生成代码
     */
    private List<String> generateRowNote() {
        CodeString codeString = new CodeString();
        for (String content : this.javaNoteInfo.getContents()) {
            codeString.append("// ").append(content);
            codeString.andCodeLine("\n");
        }
        codeString.andCodeLine("\n");
        return codeString.getCodeLines();
    }
}
