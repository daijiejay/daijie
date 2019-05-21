package org.daijie.lock;

import org.daijie.lock.redis.EnableRedisLock;
import org.daijie.lock.zk.ZKLockAutoConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * redis实现锁工具类测试，使用默认为本地redis服务地址
 * @author daijie
 * @since 2019/5/17
 */
@ComponentScan(excludeFilters= {@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value= {ZKLockAutoConfiguration.class})})
@EnableRedisLock
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisLockSpringBootTest.class)
public class RedisLockSpringBootTest {
    //操作公共数据，测试是否安全
    private int increment = 0;
    //使用一个原子类的公共数据与之比较，如果用锁结果应该是一致，如果不用锁则反之
    private AtomicInteger runFrequency = new AtomicInteger(0);
    // 请求总数
    public static int clientTotal = 2000;
    // 同时并发执行的线程数
    public static int threadTotal = 200;

    @Autowired
    private LockService lockService;

    public static void main(String[] args) {
        SpringApplication.run(RedisLockSpringBootTest.class, args);
    }

    /**
     * 测试有锁和没有锁的情况下操作数据
     * @throws Exception
     */
    @Test
    public void testLock() throws Exception {
        //不加锁
        concurrentExecute(LockType.NONE);
        Assert.assertNotEquals(increment, runFrequency.get());
        Assert.assertTrue(increment < clientTotal);
        Assert.assertTrue(runFrequency.get() == clientTotal);
        //重置累加数
        increment = 0;
        runFrequency = new AtomicInteger(0);
        //用工具类加锁
        concurrentExecute(LockType.TOOL);
        Assert.assertEquals(increment, runFrequency.get());
        Assert.assertTrue(runFrequency.get() >= 1);
        //重置累加数
        increment = 0;
        runFrequency = new AtomicInteger(0);
        //用注解加锁
        System.out.println("注解加锁执行前-increment:" + increment + "runFrequency" + runFrequency.get());
        concurrentExecute(LockType.ANONTATION);
        System.out.println("注解加锁执行后-increment:" + increment + "runFrequency" + runFrequency.get());
        Assert.assertEquals(increment, runFrequency.get());
        Assert.assertTrue(runFrequency.get() >= 1);
    }

    /**
     * 并发执行
     * @param lockType 加锁类型
     */
    public void concurrentExecute(LockType lockType) throws Exception {
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
                    switch (lockType) {
                        case NONE:
                            add();
                            break;
                        case TOOL:
                            lockToolAdd();
                            break;
                        case ANONTATION:
                            anontationLockAdd();
                            break;
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
    public void lockToolAdd() {
        LockTool.execute("test",5000, new Callback() {
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

    public void anontationLockAdd() {
        lockService.anontationLockAdd(increment, runFrequency);
    }

    static enum  LockType {
        NONE,
        TOOL,
        ANONTATION
    }
}
