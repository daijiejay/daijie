# 工程简介
* 在daijie-shiro-security基础上集成spring-security-oauth2
* 此包必须是资源服务与第三方访问资源接口的中间服务，结合使用`@EnableShiro`注解且有实现了shiro登录接口的微服务做角色授权和跳转交互，
类似`@EnableEurekaServer`的服务一样不做开发。 

# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-shiro-oauth2-spring-boot-starter</artifactId>
	<version>1.1.0-RELEASE</version>
</dependency>
```
## 启动shiro安全服务
* 启动类引用`@EnableShiroOauth2SecurityServer`注解
```java
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

#用户授权登录请求接口，默认是参数名是username和password，如需自定义http://daijie.org/login?user={username}&password={password}
shiro.oauth2.loginUrl=http://daijie.org/login
#用户授权登录请求接口方式
shiro.oauth2.loginMethod=post
#请求权限，比如请求路径/api/user，需要有角色为USER,ADMIN才能访问。（/api/user等于api_user，/api/user/**等于api_user_）
shiro.oauth2.matcher.api_user=USER,ADMIN
shiro.oauth2.matcher.api_setRedis=USER
shiro.oauth2.matcher.api_getRedis=USER

#kisso的属性配置一定要与调用login的服务配置一致
#是否开启kisso cookie机制
shiro.kissoEnable=true
#加密随机码
kisso.config.signkey=C691d971EJ3H376G81
#cookie名称
kisso.config.cookieName=token
#cookie的作用域
kisso.config.cookieDomain=daijie.org

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
