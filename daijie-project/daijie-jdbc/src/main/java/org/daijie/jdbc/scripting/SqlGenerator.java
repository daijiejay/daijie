package org.daijie.jdbc.scripting;

import org.daijie.jdbc.matedata.AgileTableMateData;
import org.daijie.jdbc.matedata.TableMatedataManage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daijie
 * @since 2019/8/28
 */
public class SqlGenerator {

    private SqlAnalyzer sqlAnalyzer = new SqlAnalyzerImpl();

    public static String generator(Class returnClass, AgileWrapper agileWrapper) {
        AgileTableMateData tableMatedata = TableMatedataManage.initTable(returnClass, agileWrapper);
        SqlSpelling sqlSpelling = SqlSpelling.getInstance();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sqlSpelling.agileSql(sql, tableMatedata, agileWrapper, params);
        return sql.toString();
    }
}
