package org.daijie.shiro.redis;

import java.util.TreeSet;

/**
 * redis操作工厂类
 * @author daijie
 * @date 2017年6月22日
 */
public interface IRedisOperator {
  /**
   * 根据pattern 获取所有的keys
   * @param pattern
   * @return
   */
  TreeSet<String> keys(String pattern) throws Exception;

  /**
   * 删除所有的keys
   */
  void flushDB()throws Exception;
}
