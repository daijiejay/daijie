package org.daijie.shiro;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * redis客服端管理类
 * @author daijie
 * @date 2017年6月22日
 */
public class JedisManager {

    private JedisPool jedisPool;

    /**
     * 建立redis客户端
     * @return
     */
    public Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = getJedisPool().getResource();
        } catch (Exception e) {
            throw new JedisConnectionException(e);
        }
        return jedis;
    }

    @SuppressWarnings("deprecation")
	public void returnResource(Jedis jedis, boolean isBroken) {
        if (jedis == null)
            return;
        if (isBroken)
            getJedisPool().returnBrokenResource(jedis);
        else
            getJedisPool().returnResource(jedis);
    }

    /**
     * 获取redis数据库中的值
     * @param dbIndex
     * @param key
     * @return
     * @throws Exception
     */
    public byte[] getValueByKey(int dbIndex, byte[] key) throws Exception {
        Jedis jedis = null;
        byte[] result = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.get(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
        return result;
    }

    /**
     * 删除redis数据库中的值
     * @param dbIndex
     * @param key
     * @return
     * @throws Exception
     */
    public void deleteByKey(int dbIndex, byte[] key) throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.del(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }

    /**
     * 保存redis数据库中的值
     * @param dbIndex
     * @param key
     * @return
     * @throws Exception
     */
    public void saveValueByKey(int dbIndex, byte[] key, byte[] value, int expireTime)
            throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.set(key, value);
            if (expireTime > 0)
                jedis.expire(key, expireTime);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
