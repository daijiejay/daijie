package org.daijie.jdbc.result;

import org.daijie.jdbc.matedata.TableMatedata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询结果封装对象
 * @author daijie
 * @since 2019/7/4
 */
public class PageResult<T> extends BaseResult {

    /**
     * 总条数
     */
    private long total;

    /**
     * 当前页数据
     */
    private List<T> rows;

    public PageResult(Class<?> returnClass, boolean isMulti) {
        super(returnClass, isMulti);
        this.total = 0L;
        this.rows = new ArrayList<>();
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getRows() {
        return rows;
    }

    @Override
    public Object mappingObjectResult(ResultSet resultSet, TableMatedata tableMatedata) throws SQLException {
        this.rows = (List<T>) super.mappingObjectResult(resultSet, tableMatedata);
        return this;
    }

    public void pageResult(ResultSet resultSet) throws SQLException  {
        resultSet.next();
        this.total = resultSet.getLong(1);
    }
}
