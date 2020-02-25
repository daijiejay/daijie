package org.daijie.shiro.redis;

import redis.clients.jedis.JedisCluster;

import java.util.TreeSet;

/**
 * 集群redis管理
 * @author daijie
 * @since 2017年6月22日
 */
public class ClusterRedisManager {

  private JedisCluster jedisCluster;

  private RedisOperator redisOperator;

  private int expire = 0;

  public RedisOperator getRedisOperator() {
    return redisOperator;
  }

  public void setRedisOperator(RedisOperator redisOperator) {
    this.redisOperator = redisOperator;
  }

  public int getExpire() {
    return expire;
  }

  public void setExpire(int expire) {
    this.expire = expire;
  }

  public JedisCluster getJedisCluster() {
    return jedisCluster;
  }

  public void setJedisCluster(JedisCluster jedisCluster) {
    this.jedisCluster = jedisCluster;
  }

  public ClusterRedisManager() {

  }

  /**
   * 初始化方法
   */
  public void init() {

  }

  /**
   * 根据键获取值
   *
   * @param key 键
   * @return byte[] 值
   */
  public byte[] get(byte[] key) {
    byte[] value = null;
    value = jedisCluster.get(key);
    return value;
  }

  /**
   * 设置值
   *
   * @param key 键
   * @param value 值
   * @return byte[] 值
   */
  public byte[] set(byte[] key, byte[] value) {
    jedisCluster.set(key, value);
    if (this.expire != 0) {
      jedisCluster.expire(key, this.expire);
    }
    return value;
  }

  /**
   * 设置值
   *
   * @param key 键
   * @param value 值
   * @param expire 超时时间
   * @return byte[]
   */
  public byte[] set(byte[] key, byte[] value, int expire) {
    jedisCluster.set(key, value);
    if (expire != 0) {
      jedisCluster.expire(key, expire);
    }
    return value;
  }

  /**
   * 根据键删除值
   *
   * @param key 健
   */
  public void del(byte[] key) {
    jedisCluster.del(key);
  }

  /**
   * 刷新数据源
   * @throws Exception 抛出异常
   */
  public void flushDB() throws Exception {
    redisOperator.flushDB();
  }

  /**
   * 获取数据源大小
   * @param pattern 数据源
   * @return Long
   */
  public Long dbSize(String pattern) {
    Long dbSize = 0L;
    TreeSet<String> keys = null;
    try {
      keys = keys(pattern);
    } catch (Exception e) {
      e.printStackTrace();
    }
    dbSize = Long.valueOf(keys.size());
    return dbSize;
  }

  /**
   * 获取数据源中所有的key
   * @param pattern 数据源
   * @return TreeSet
   * @throws Exception 抛出异常
   */
  public TreeSet<String> keys(String pattern) throws Exception {
    TreeSet<String> treeSet = new TreeSet<String>();
    treeSet = redisOperator.keys(pattern);
    return treeSet;
  }

}
