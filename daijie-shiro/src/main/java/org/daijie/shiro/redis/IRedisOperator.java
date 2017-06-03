/**
 * Copyright 2017 asiainfo Inc.
 **/
package org.daijie.shiro.redis;

import java.util.TreeSet;

/**
 * @author zhangsy
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
