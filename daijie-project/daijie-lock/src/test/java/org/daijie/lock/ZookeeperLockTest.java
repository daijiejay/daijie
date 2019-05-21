package org.daijie.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.daijie.lock.zk.ZkDistributedLockTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * zookepper锁测试
 * @author daijie
 * @since 2019/5/9
 */
public class ZookeeperLockTest {
    //操作公共数据，测试是否安全
    private int increment = 0;
    //使用一个原子类的公共数据与之比较，如果用锁结果应该是一致，如果不用锁则反之
    private AtomicInteger runFrequency = new AtomicInteger(0);
    // 请求总数
    public static int clientTotal = 1000;
    // 同时并发执行的线程数
    public static int threadTotal = 200;
    private LockCreator proxy;
    @Before
    public void initJedis() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("127.0.0.1:2181",
                new ExponentialBackoffRetry(1000, 3));
        LockCreator proxy = new LockCreator(new ZkDistributedLockTemplate(curatorFramework));
        this.proxy = proxy;
    }

    /**
     * 测试有锁和没有锁的情况下操作数据
     * @throws Exception
     */
    @Test
    public void testLock() throws Exception {
        //不加锁
        concurrentExecute(false);
        Assert.assertNotEquals(increment, runFrequency.get());
        Assert.assertTrue(increment < clientTotal);
        Assert.assertTrue(runFrequency.get() == clientTotal);
        //重置累加数
        increment = 0;
        runFrequency = new AtomicInteger(0);
        //加锁
        concurrentExecute(true);
        Assert.assertEquals(increment, runFrequency.get());
        Assert.assertTrue(runFrequency.get() >= 1);
    }

    /**
     * 并发执行
     * @param isLock 是否加锁
     */
    public void concurrentExecute(boolean isLock) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        //信号量，此处用于控制并发的线程数
        final Semaphore semaphore = new Semaphore(threadTotal);
        //闭锁，可实现计数器递减
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal ; i++) {
            executorService.execute(() -> {
                try {
                    //执行此方法用于获取执行许可，当总计未释放的许可数不超过200时，
                    //允许通行，否则线程阻塞等待，直到获取到许可。
                    semaphore.acquire();
                    if (isLock) {
                        lockAdd();
                    } else {
                        add();
                    }
                    //释放许可
                    semaphore.release();
                } catch (Exception e) {
                    //log.error("exception", e);
                    e.printStackTrace();
                }
                //闭锁减一
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();//线程阻塞，直到闭锁值为0时，阻塞才释放，继续往下执行
        executorService.shutdown();
    }

    /**
     * 累加
     */
    public void add() {
        runFrequency.incrementAndGet();
        increment ++;
    }

    /**
     * 加入redis锁累加
     */
    public void lockAdd() {
        proxy.invoke("test",5000, new Callback() {
            @Override
            public Object onGetLock() throws InterruptedException {
                System.out.println(Thread.currentThread().getName() + ":获取锁成功，开始处理业务");
                add();
                Thread.sleep(ThreadLocalRandom.current().nextInt(5)*1000);
                return null;
            }
            @Override
            public Object onTimeout() throws InterruptedException {
                System.out.println(Thread.currentThread().getName() + ":获取锁超时.....");
                return null;
            }
            @Override
            public Object onError(Exception exception){
                System.out.println(Thread.currentThread().getName() + ":获取锁异常.....");
                return null;
            }
        });
    }
}
