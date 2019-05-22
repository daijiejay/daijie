# 工程简介
* 提供spring boot配置的分布式锁，基于redis和zookepper中间件实现
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-lock</artifactId>
	<version>2.0.0</version>
</dependency>
```
## 代码说明
以下是redis实现的代码示例
```java
JedisPool jedisPool = new JedisPool("localhost", 6379);
LockCreator proxy = new LockCreator(new RedisDistributedLockTemplate(jedisPool));
proxy.invoke("test",5000, new Callback() {
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
zookepeer实现创建锁代码
```java
CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
LockCreator proxy = new LockCreator(new ZkDistributedLockTemplate(curatorFramework));
```