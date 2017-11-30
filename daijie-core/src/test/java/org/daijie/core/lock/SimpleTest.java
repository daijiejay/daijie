//package org.daijie.core.lock;
//
//import java.util.concurrent.TimeUnit;
//
//import org.daijie.core.lock.redis.RedisDistributedLockTemplate;
//import org.daijie.core.lock.redis.RedisReentrantLock;
//
//import redis.clients.jedis.JedisPool;
//
///**
// * Created by sunyujia@aliyun.com on 2016/2/24.
// */
//public class SimpleTest {
//
//	public static void main(String[] args) throws Exception {
//		JedisPool jedisPool=new JedisPool("127.0.0.1",6379);//实际应用时可通过spring注入
//		RedisReentrantLock lock=new RedisReentrantLock(jedisPool,"抢单");
//		try {
//			if (lock.tryLock(5000L, TimeUnit.MILLISECONDS)) {
//				//TODO 获得锁后要做的事
//			}else{
//				//TODO 获得锁超时后要做的事
//			}
//		}finally {
//			lock.unlock();
//		}
//	}
//
//	public static void test1(){
//		JedisPool jedisPool=new JedisPool("127.0.0.1",6379);//实际应用时可通过spring注入
//		final RedisDistributedLockTemplate template=new RedisDistributedLockTemplate(jedisPool);//本类多线程安全,可通过spring注入
//		Object obj = template.execute("抢单", 5000, new Callback() {
//			@Override
//			public Object onGetLock() throws InterruptedException {
//				return "获得锁后要做的事";
//			}
//
//			@Override
//			public Object onTimeout() throws InterruptedException {
//				return "获得锁超时后要做的事";
//			}
//		});
//		System.out.println(obj.toString());
//	}
//}
