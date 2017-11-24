package org.daijie.core.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.daijie.core.lock.zk.ZkDistributedLockTemplate;
import org.junit.Test;

/**
 * Created by sunyujia@aliyun.com on 2016/2/24.
 */


public class ZkReentrantLockTemplateTest {

	@Test
	public void testTry() throws InterruptedException {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
		client.start();

		final ZkDistributedLockTemplate template=new ZkDistributedLockTemplate(client);
		int size=20;
		final CountDownLatch startCountDownLatch = new CountDownLatch(1);
		final CountDownLatch endDownLatch=new CountDownLatch(size);
		for (int i =0;i<size;i++){
			new Thread() {
				public void run() {
					try {
						startCountDownLatch.await();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					final int sleepTime=ThreadLocalRandom.current().nextInt(5)*1000;
					Object obj = template.execute("test",sleepTime, new Callback() {
						public Object onGetLock() throws InterruptedException {
							System.out.println(Thread.currentThread().getName() + ":getLock");
							Thread.sleep(sleepTime);
							System.out.println(Thread.currentThread().getName() + ":sleeped:"+sleepTime);
							endDownLatch.countDown();
							return "获得锁后要做的事";
						}
						public Object onTimeout() throws InterruptedException {
							System.out.println(Thread.currentThread().getName() + ":timeout");
							endDownLatch.countDown();
							return "获得锁超时后要做的事";
						}
					});
					System.out.println(obj.toString());
				}
			}.start();
		}
		startCountDownLatch.countDown();
		endDownLatch.await();
	}

	public static void main(String[] args){
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
		client.start();

		final ZkDistributedLockTemplate template=new ZkDistributedLockTemplate(client);//本类多线程安全,可通过spring注入
		Object obj = template.execute("抢单", 5000, new Callback() {
			@Override
			public Object onGetLock() throws InterruptedException {
				return "获得锁后要做的事";
			}

			@Override
			public Object onTimeout() throws InterruptedException {
				return "获得锁超时后要做的事";
			}
		});
		System.out.println(obj.toString());
	}
}
