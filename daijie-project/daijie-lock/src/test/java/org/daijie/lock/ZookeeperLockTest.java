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
    //定义一个不安全的公共数据，测试是否安全
    private int unsafeIncrement = 0;
    //线程记数器
    private AtomicInteger runFrequency = new AtomicInteger(0);
    //获取锁成功的计数器，使用一个原子类的公共数据与之比较，如果用锁结果应该是一致，如果不用锁则反之
    private AtomicInteger successIncrement = new AtomicInteger(0);
    //获取锁超时的计数器
    private AtomicInteger timeOutIncrement = new AtomicInteger(0);
    //获取锁异常的计数器
    private AtomicInteger errorIncrement = new AtomicInteger(0);
    // 请求总数
    public static int clientTotal = 2000;
    // 同时并发执行的线程数
    public static int threadTotal = 200;
    //构建锁
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
        init();
        //不加锁
        concurrentExecute(false);
        validate(false);
        //重置累加数
        init();
        //用工具类加锁
        concurrentExecute(true);
        validate(true);
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
                        addSucess();
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
     * 加入redis锁累加
     */
    public void lockAdd() {
        proxy.invoke("test",5000, new Callback() {
            @Override
            public Object onGetLock() throws InterruptedException {
                System.out.println(Thread.currentThread().getName() + ":获取锁成功，开始处理业务");
                addSucess();
                Thread.sleep(ThreadLocalRandom.current().nextInt(5)*1000);
                return null;
            }
            @Override
            public Object onTimeout() throws InterruptedException {
                addTimeOut();
                System.out.println(Thread.currentThread().getName() + ":获取锁超时.....");
                return null;
            }
            @Override
            public Object onError(Exception exception){
                addError();
                System.out.println(Thread.currentThread().getName() + ":获取锁异常.....");
                return null;
            }
        });
    }

    /**
     * 初始化计数器
     */
    public void init() {
        unsafeIncrement = 0;
        runFrequency = new AtomicInteger(0);
        successIncrement = new AtomicInteger(0);
        timeOutIncrement = new AtomicInteger(0);
        errorIncrement = new AtomicInteger(0);
    }

    /**
     * 累加
     */
    public void add() {
        runFrequency.incrementAndGet();
    }

    /**
     * 成功锁累加
     */
    public void addSucess() {
        add();
        unsafeIncrement ++;
        successIncrement.incrementAndGet();
    }

    /**
     * 超时锁累加
     */
    public void addTimeOut() {
        add();
        timeOutIncrement.incrementAndGet();
    }

    /**
     * 异常锁累加
     */
    public void addError() {
        add();
        errorIncrement.incrementAndGet();
    }

    /**
     * 校验数据是不是达到要求
     * @param isLock 是否加锁
     */
    public void validate(boolean isLock) {
        Assert.assertTrue(runFrequency.get() == clientTotal);
        if (!isLock) {
            Assert.assertNotEquals(unsafeIncrement, successIncrement.get());
            Assert.assertTrue(unsafeIncrement < clientTotal);
            Assert.assertTrue(successIncrement.get() == clientTotal);
        } else {
            Assert.assertEquals(unsafeIncrement, successIncrement.get());
            Assert.assertTrue(successIncrement.addAndGet(timeOutIncrement.addAndGet(errorIncrement.get())) == clientTotal);
        }
    }
}
