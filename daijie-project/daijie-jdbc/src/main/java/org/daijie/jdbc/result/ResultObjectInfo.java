package org.daijie.jdbc.result;

import com.google.common.collect.Maps;

import javax.persistence.JoinColumn;
import java.util.List;
import java.util.Map;

/**
 * 查询结果映射对象信息
 *
 * @author daijie
 * @since 2020年03月04日
 */
public class ResultObjectInfo {

    /**
     * 对象转Map
     */
    Map<String, ResultFieldInfo> properties = Maps.newHashMap();

    /**
     * 对象关联查询映射字段信息
     */
    Map<String, List<JoinColumn>> propertiesJoinColumns = Maps.newHashMap();

    public Map<String, ResultFieldInfo> getProperties() {
        return properties;
    }

    public void addProperties(String filedName, ResultFieldInfo resultFieldInfo) {
        this.properties.put(filedName, resultFieldInfo);
    }

    public Map<String, List<JoinColumn>> getPropertiesJoinColumns() {
        return propertiesJoinColumns;
    }

    public void addPropertiesJoinColumns(String filedName, List<JoinColumn> joinColumns) {
        this.propertiesJoinColumns.put(filedName, joinColumns);
    }
}
