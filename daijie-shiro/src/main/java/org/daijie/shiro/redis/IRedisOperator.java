package org.daijie.shiro.redis;

import java.util.TreeSet;

/**
 * redis操作工厂类
 * @author daijie
 * @since 2017年6月22日
 */
public interface IRedisOperator {
  /**
   * 根据pattern 获取所有的keys
   * @param pattern 数据源
   * @return TreeSet
   * @exception Exception 抛出异常
   */
  TreeSet<String> keys(String pattern) throws Exception;

  /**
   * 删除所有的keys
   * @exception Exception 抛出异常
   */
  void flushDB()throws Exception;
}
