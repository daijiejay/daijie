package org.daijie.jdbc.generator.executor;

import com.google.common.collect.Lists;
import org.daijie.jdbc.matedata.ColumnMateData;
import org.daijie.jdbc.matedata.TableMateData;

import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * 数据库连接客户端
 * @author daijie
 * @since 2019/9/16
 */
public class ConnectionClientManage {

    /**
     * 执行查询语句
     * @param url 连接地址，包含服务器名，数据库名，用户名，密码等相关参数
     * @param driverName 驱动类名
     * @param sql 需要执行的SQL语句
     * @return 返回查询结果集
     */
    public static ResultSet executeQuery(String url, String driverName, String sql) {
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取数据库表信息
     * createStatement参数1
     * TYPE_FORWORD_ONLY 结果集的游标只能向下滚动。
     * TYPE_SCROLL_INSENSITIVE 结果集的游标可以上下移动，当数据库变化时，当前结果集不变。
     * TYPE_SCROLL_SENSITIVE 返回可滚动的结果集，当数据库变化时，当前结果集同步改变。
     * createStatement参数2
     * CONCUR_READ_ONLY 不能用结果集更新数据库中的表。
     * CONCUR_UPDATETABLE 能用结果集更新数据库中的表
     * @param url 连接地址，包含服务器名，数据库名，用户名，密码等相关参数
     * @param driverName 驱动类名
     * @param properties 获取表信息的相关配置
     * @return 返回查询结果集
     */
    public static List<TableMateData> getMatedata(String url, String driverName, Properties properties) {
        List<TableMateData> list = Lists.newArrayList();
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, properties);
            conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables(null, "%", "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String remarks = resultSet.getString("REMARKS");
                ResultSet keySet = metaData.getPrimaryKeys(null, "%", tableName);
                TableMateData tableMateData = new TableMateData(tableName, remarks);
                String key = null;
                if (keySet.next()) {
                    key = keySet.getString(4);
                }
                ResultSet columnSet = metaData.getColumns(null,"%", tableName,"%");
                while(columnSet.next()) {
                    ColumnMateData columnMateData = newColumnMateData(columnSet);
                    tableMateData.addColumn(newColumnMateData(columnSet));
                    if (columnMateData.getName().equals(key)) {
                        tableMateData.setPrimaryKey(columnMateData);
                    }
                }
                list.add(tableMateData);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 创建表字段元数据
     * @param columnSet 表字段查询结果
     * @return 返回表字段元数据
     * @throws SQLException
     */
    private static ColumnMateData newColumnMateData(ResultSet columnSet) throws SQLException {
        String columnName = columnSet.getString("COLUMN_NAME");
        String columnType = columnSet.getString("TYPE_NAME");
        int columnSize = columnSet.getInt("COLUMN_SIZE");
        int decimalDigits = columnSet.getInt("DECIMAL_DIGITS");
        boolean nullable = columnSet.getBoolean("NULLABLE");
        String remarks = columnSet.getString("REMARKS");
        return new ColumnMateData(columnName, remarks, columnType, columnSize, decimalDigits, nullable);
    }
}
