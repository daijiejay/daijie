package org.daijie.jdbc.scripting;

import org.daijie.jdbc.matedata.MultiTableMateData;
import org.daijie.jdbc.matedata.TableMatedataManage;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据包装工具类生成SQL
 * @author daijie
 * @since 2019/8/28
 */
public class SqlGenerator {

    /**
     * 生成多表关联查询SQL语句
     * @param returnClass 执行SQL需要映射的返回对象
     * @param multiWrapper 多表关联的包装工具类
     * @return 返回SQL语句
     */
    public static String generator(Class returnClass, MultiWrapper multiWrapper) {
        MultiTableMateData tableMatedata = TableMatedataManage.initTable(returnClass, multiWrapper);
        SqlSpelling sqlSpelling = SqlSpelling.getInstance();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sqlSpelling.agileSql(sql, tableMatedata, multiWrapper, params);
        return sql.toString();
    }
}
