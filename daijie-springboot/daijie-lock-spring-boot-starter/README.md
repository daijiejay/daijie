# 工程简介
* 提供spring boot配置的分布式锁，基于redis和zookepper中间件实现
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-lock-spring-boot-starter</artifactId>
	<version>2.0.0</version>
</dependency>
```
## 代码说明
* 启动类引用`@EnableRedisLock`注解开启redis分布式锁，引用`@EnableZKLock`注解开启zookeeper分布式锁
```java
@EnableRedisLock
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
* properties相关配置
```
#redis分布式锁配置-----------------------------start
#redis地址，集群服务以“,”号隔开
lock.redis.addresses=127.0.0.1:6379
#redis密码，没有不需要配置
#lock.redis.password=
#redis分布式锁配置-----------------------------end

##zookeeper分布式锁配置-----------------------------start
##zookeeper地址，多个服务以“,”号隔开
#lock.zk.addresses=127.0.0.1:2181
##重试间隔时间
#lock.zk.baseSleepTimeMs=1000
##重试次数
#lock.zk.maxRetries=3
##zookeeper分布式锁配置-----------------------------end
```
* 工具类使用
```java
LockTool.execute("test",5000, new Callback() {
    @Override
    public Object onGetLock() throws InterruptedException {
        // 获取锁成功，开始处理业务
        Thread.sleep(ThreadLocalRandom.current().nextInt(5)*1000);
        return null;
    }
    @Override
    public Object onTimeout() throws InterruptedException {
        // 获取锁超时
        return null;
    }
    @Override
    public Object onError(Exception exception){
        // 获取锁异常
        return null;
    }
});
```
* 注解类使用

引用org.daijie.core.lock.@Lock注解，该类需要被spring管理方可生效，具体配置（
argName：param参数作为锁业务ID；
lockId: 业务ID，优先级大于argNme配置，默认方法名作为唯一字符串；
timeOut: 锁时长，默认5秒；
timeOutMethodName：锁占用时需要执行通知的方法，默认可以不用配置；
errorMethodName：锁异常时需要执行通知的方法，默认可以不用配置。）
```java
@Service
public class LockService {
    @Lock(argName = "id",
            timeout = 5000,
            errorMethodName = "org.daijie.lock.LockService.lockError",
            timeOutMethodName = "org.daijie.lock.LockService.lockTimeOut")
    public void andAnontationLockService(String id) {
        // 获取锁成功，开始处理业务
    }
    
    public void lockError(String id) {
        // 获取锁异常
    }
    
    public void lockTimeOut(String id) {
        // 获取锁超时
    }
}
```
如果获取锁失败会分别抛出LockException和LockTimeOutException，可以捕获对应的异常处理逻辑
```java
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
```