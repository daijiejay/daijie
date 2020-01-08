package org.daijie.jdbc.session;

import org.daijie.jdbc.result.PageResult;
import org.daijie.jdbc.scripting.Wrapper;

import java.io.Serializable;
import java.util.List;

/**
 * 操作数据库映射对象的会话
 * @param <T> 具体映射对象类型
 */
public interface SessionMapper<T> {

    /**
     * 根据表主键查询
     * @param id 主键
     * @return 返回映射对象
     */
    T selectById(Serializable id);

    /**
     * 根据映射对象参数查询一条数据
     * @param entity 条件映射对象
     * @return 返回映射对象
     */
    T selectOne(T entity);

    /**
     * 根据映射对象参数查询多条数据
     * @param entity 条件映射对象
     * @return 返回映射对象数组
     */
    List<T> selectList(T entity);

    /**
     * 查询所有数据
     * @return 返回映射对象数组
     */
    List<T> selectAll();

    /**
     * 根据条件包装对象查询多条数据
     * @param wrapper 条件包装
     * @return 返回映射对象数组
     */
    List<T> selectByWrapper(Wrapper wrapper);

    /**
     * 根据条件包装对象分页查询数据
     * @param wrapper 条件包装
     * @return 返回映射对象分页结果数据
     */
    PageResult<T> selectPageByWrapper(Wrapper wrapper);

    /**
     * 根据映射对象参数查询数据的总条数
     * @param entity 条件映射对象
     * @return 返回总条数
     */
    long selectCount(T entity);

    /**
     * 根据条件包装对象参数查询数据的总条数
     * @param wrapper 条件包装
     * @return 返回总条数
     */
    long selectCountByWrapper(Wrapper wrapper);

    /**
     * 根据主键更新
     * @param entity 需要修改的映射对象
     * @return 是否操作成功
     */
    boolean updateById(T entity);

    /**
     * 根据主键更新（只更新数据不等于null的数据）
     * @param entity 需要修改的映射对象
     * @return 是否操作成功
     */
    boolean updateSelectiveById(T entity);

    /**
     * 根据条件更新数据
     * @param entity 需要修改的映射对象
     * @param wrapper 条件包装
     * @return 是否操作成功
     */
    boolean updateByWrapper(T entity, Wrapper wrapper);

    /**
     * 根据条件更新数据（只更新数据不等于null的数据）
     * @param entity 需要修改的映射对象
     * @param wrapper 条件包装
     * @return 是否操作成功
     */
    boolean updateSelectiveByWrapper(T entity, Wrapper wrapper);

    /**
     * 插入单条数据
     * @param entity 插入数据的映射对象
     * @return 是否操作成功
     */
    boolean insert(T entity);

    /**
     * 插入单条数据（只插入数据不等于null的数据）
     * @param entity 插入数据的映射对象
     * @return 是否操作成功
     */
    boolean insertSelective(T entity);

    /**
     * 插入多条数据
     * @param entities 插入数据的映射对象
     * @return 是否操作成功
     */
    boolean insert(List<T> entities);

    /**
     * 根据主键删除
     * @param id 主键
     * @return 是否操作成功
     */
    boolean deleteById(Serializable id);

    /**
     * 根据条件删除数据
     * @param wrapper 条件包装
     * @return 是否操作成功
     */
    boolean deleteByWrapper(Wrapper wrapper);
}
