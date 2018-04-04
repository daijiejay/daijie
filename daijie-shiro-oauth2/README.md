# 工程简介
* 在daijie-shiro-security基础上集成spring-security-oauth2
#使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-shiro-oauth2-spring-boot-starter</artifactId>
	<version>1.0.4-RELEASE</version>
</dependency>
```
## 启动shiro安全服务
* 启动类引用`@EnableShiroSecurityServer`注解
```
@EnableShiroOauth2SecurityServer
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

#是否开启kisso cookie机制
shiro.kissoEnable=true
#加密随机码
kisso.config.signkey=C691d971EJ3H376G81
#cookie名称
kisso.config.cookieName=token
#cookie的作用域
kisso.config.cookieDomain=daijie.org

#用户授权登录请求接口
shiro.oauth2.loginUrl=http://daijie.org/login
#用户授权登录请求接口方式
shiro.oauth2.loginMethod=post

#忽略已经添加的服务
zuul.ignored-services=*
#全局设置
zuul.sensitive-headers=
#监控路径
zuul.routes.api.path=/api/**
#重定向到指定服务
zuul.routes.api.serviceId=daijie-api-cloud
#为true时，访问/api/** = daijie-api-cloud/**，为false时，访问/api/** = daijie-api-cloud/api/**
zuul.routes.api.stripPrefix=true
```
