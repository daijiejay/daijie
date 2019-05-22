package org.daijie.lock;

import org.daijie.lock.Exception.LockException;
import org.daijie.lock.Exception.LockTimeOutException;
import org.daijie.lock.redis.EnableRedisLock;
import org.daijie.lock.zk.ZKLockAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

/**
 * redis实现锁工具类测试，使用默认为本地redis服务地址
 * 这里需要排除ZKLockAutoConfiguration类，是因为启动类的包路径是org.daijie.lock，
 * 与配置类的包路径一致会被同时扫描到redis和zookepper实现两个配置文件导致冲突，在实际应用中不需要排除。
 * @author daijie
 * @since 2019/5/17
 */
@ComponentScan(excludeFilters= {@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value= {ZKLockAutoConfiguration.class})})
@EnableRedisLock
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisLockSpringBootTest.class)
public class RedisLockSpringBootTest {
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
        //重置累加数
        lockService.init();
        //不加锁
        concurrentExecute(LockType.NONE);
        lockService.validate(false, clientTotal);
        lockService.init();
        //用工具类加锁
        concurrentExecute(LockType.TOOL);
        lockService.validate(true, clientTotal);
        //重置累加数
        lockService.init();
        //用注解加锁
        concurrentExecute(LockType.ANONTATION);
        lockService.validate(true, clientTotal);
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
                            lockService.addSucess();
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
     * 工具类加锁测试
     */
    public void lockToolAdd() {
        LockTool.execute("test",5000, new Callback() {
            @Override
            public Object onGetLock() throws InterruptedException {
                System.out.println(Thread.currentThread().getName() + ":获取锁成功，开始处理业务");
                lockService.addSucess();
                Thread.sleep(ThreadLocalRandom.current().nextInt(5)*1000);
                return null;
            }
            @Override
            public Object onTimeout() throws InterruptedException {
                lockService.addTimeOut();
                System.out.println(Thread.currentThread().getName() + ":获取锁超时.....");
                return null;
            }
            @Override
            public Object onError(Exception exception){
                lockService.addError();
                System.out.println(Thread.currentThread().getName() + ":获取锁异常.....");
                return null;
            }
        });
    }

    /**
     * 注解类加锁测试
     */
    public void anontationLockAdd() {
        try {
            //调用加注解锁的方法
            lockService.andAnontationLockService("test");
            //只有成功获取锁时才会往下执行，否则抛出异常
        } catch (Exception e) {
            if (e instanceof LockTimeOutException) {
                //执行锁超时处理
            } else if(e instanceof LockException) {
                //执行锁异常处理
            }
        }
    }
}
