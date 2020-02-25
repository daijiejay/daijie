package org.daijie.shiro.redis;

import org.crazycake.shiro.RedisManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 单个redis管理，重写方法，解决redis升级3.0以上版本兼容性问题
 */
public class SingleRedisManager extends RedisManager {

    @Override
    public Long dbSize(byte[] pattern) {
        long dbSize = 0L;
        Jedis jedis = this.getJedis();

        try {
            ScanParams params = new ScanParams();
            params.count(this.getCount());
            params.match(pattern);
            byte[] cursor = ScanParams.SCAN_POINTER_START_BINARY;

            ScanResult scanResult;
            do {
                scanResult = jedis.scan(cursor, params);
                List<byte[]> results = scanResult.getResult();

                for(Iterator var9 = results.iterator(); var9.hasNext(); ++dbSize) {
                    byte[] result = (byte[])var9.next();
                }

                cursor = scanResult.getCursorAsBytes();
            } while(scanResult.getCursor().compareTo(ScanParams.SCAN_POINTER_START) > 0);
        } finally {
            jedis.close();
        }

        return dbSize;
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        Set<byte[]> keys = new HashSet();
        Jedis jedis = this.getJedis();

        try {
            ScanParams params = new ScanParams();
            params.count(this.getCount());
            params.match(pattern);
            byte[] cursor = ScanParams.SCAN_POINTER_START_BINARY;

            ScanResult scanResult;
            do {
                scanResult = jedis.scan(cursor, params);
                keys.addAll(scanResult.getResult());
                cursor = scanResult.getCursorAsBytes();
            } while(scanResult.getCursor().compareTo(ScanParams.SCAN_POINTER_START) > 0);
        } finally {
            jedis.close();
        }

        return keys;
    }
}
