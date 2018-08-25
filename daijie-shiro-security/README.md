# 工程简介
* 集成zuul服务代理，通过`@EnableShiroSecurityServer`注解开启访问权限控制。
* 配置shiro角色权限拦截，再重定向到对应的子微服务。
* 结合zuul的微服务管理配置，自动生成被反向代理的所有微服务集中式接口文档。
#使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-shiro-security-spring-boot-starter</artifactId>
	<version>1.1.0-RELEASE</version>
</dependency>
```
## 启动shiro安全服务
* 启动类引用`@EnableShiroSecurityServer`注解
```java
@EnableShiroSecurityServer
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
* shiro安全服务properties相关配置
```
#是否开启redis集群
shiro.redis.cluster=false
#服务地址
shiro.redis.address=127.0.0.1:6379
#访问密码，没有则不用设置
#shiro.redis.password=
#默认连接超时时间
shiro.redis.connectionTimeout=5000
#返回值的超时时间
shiro.redis.timeout=5000
#默认存储超时时间
shiro.redis.expire=360000
#出现异常最大重试次数
shiro.redis.maxAttempts=1

#忽略已经添加的服务
zuul.ignored-services=*
#全局设置
zuul.sensitive-headers=
#监控路径
zuul.routes.api.path=/**
#重定向到指定服务
zuul.routes.api.serviceId=daijie-api-cloud
#重定向到指定服务的显示名称
zuul.routes.api.serviceName=api接口
#为true时，访问/api/** = daijie-api-cloud/**，为false时，访问/api/** = daijie-api-cloud/api/**
zuul.routes.api.stripPrefix=false

```
* shiro角色权限properties相关配置
```
shiro.filterClasses=org.daijie.shiro.filter.SecurityFilter
#允许admin这个角色的用户访问，需要调用Auth.refreshRoles(new ArrayList<String>())添加权限
#请求权限，比如请求路径/api/user，需要有角色为USER,ADMIN才能访问。（/api/user等于api_user，/api/user/**等于api_user_）
shiro.matcher.api_user_=security[ADMIN,USER]
```