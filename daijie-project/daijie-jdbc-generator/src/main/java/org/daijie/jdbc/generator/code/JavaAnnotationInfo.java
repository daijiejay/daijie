package org.daijie.jdbc.generator.code;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * JAVA类注解信息
 * @author daijie
 * @since 2019/9/16
 */
public class JavaAnnotationInfo {

    /**
     * 注解名称
     */
    private String name;

    /**
     * 注解成员属性
     */
    private Map<String, Object> members;

    public JavaAnnotationInfo(String name) {
        this.name = name;
        this.members = Maps.newHashMap();
    }

    public JavaAnnotationInfo(String name, Map<String, Object> members) {
        this.name = name;
        if (members == null) {
            members = Maps.newHashMap();
        }
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getMembers() {
        return members;
    }

    public JavaAnnotationInfo addMember(String key, Object value) {
        this.members.put(key, value);
        return this;
    }
}
