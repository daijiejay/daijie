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

    public static String generator(Class returnClass, MultiWrapper multiWrapper) {
        MultiTableMateData tableMatedata = TableMatedataManage.initTable(returnClass, multiWrapper);
        SqlSpelling sqlSpelling = SqlSpelling.getInstance();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sqlSpelling.agileSql(sql, tableMatedata, multiWrapper, params);
        return sql.toString();
    }
}
