# 工程简介
* 基于spring-cloud与spring-boot系列jar包，以解决分布式问题为目标提供简单易用的集成工具。
* 封装RESTful风格统一返回参数实体，包括正常返回及异常返回，Controller异常全局处理。
* 集成swagger生成api文档，以配置形式自定义docket，可配置需要指定生成的组、包路径等。
* 微服务请求报文与客服端请求header报文一致性处理，请求数据json转换param处理。
* 单点登录集成Kisso管理客服端cookie。
* 加入了redis和zookeeper分布式锁，可配置单机或集群的redis及zookeeper，由@EnableRedisLock和@EnableZKLock开启自动装置。(注意：redis用到了avel命令，只支持2.6版本以上服务器)
* 提供一些常用工具类。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-core-spring-boot-starter</artifactId>
	<version>1.0.1-RELEASE</version>
</dependency>
```
## 异常全局处理
* 自定义`RestController`与`Controller`需要分别继承`ApiController`与`WebController`，其目的是需要统一管理Controller，目前已实现了异常处理，`ApiController`与`WebController`保证反给消费者的是`ModelResult`实体与`String`路径，`WebController`异常默认返回路径是“/error”，可以在`Controller`上加`@ErrorMapping`类注解自定义异常时跳转路径。
```
@RestController
public class TestController extends ApiController {
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	public ModelResult<String> getData(){
		return Result.build("data");
	}
}
```
```
@ErrorMapping(path="/error")
@Controller
public class HomeController extends WebController {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getData(){
		return "index";
	}
}
```
## 生成api文档
* 启动类引用`@EnableMySwagger`注解，官方的`@EnableSwagger2`注解被重写
```
@EnableMySwagger
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
* properties相关配置
```
#文档生成需要扫描的包路径，多个以“,”号隔开
swagger.basePackage=org.daijie.api
#标题
swagger.title=
#描述
swagger.description=
#路径
swagger.termsOfServiceUrl=
#联系方式
swagger.contact=
#版本
swagger.version=1.0

文档组配置
#文档组名称，多个以“,”号隔开
swagger.groupNames=user
swagger.user.basePackage=org.daijie.api
swagger.user.title=
swagger.user.description=
swagger.user.termsOfServiceUrl=
swagger.user.contact=
swagger.user.version=1.0
```
## 分布式锁
* 启动类引用`@EnableRedisLock`注解开启redis分布式锁，引用`@EnableZKLock`注解开启zookeeper分布式锁
```
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
```
@RestController
public class LockController {
	private static final Logger logger = Logger.getLogger(LockController.class);
	
	@RequestMapping(value = "testLock", method = RequestMethod.GET)
	public ModelResult<Object> testLock(){
		Object result = LockTool.execute("test", 1000, new Callback() {
			@Override
			public Object onTimeout() throws InterruptedException {
				logger.info("锁超时业务处理");
				return 0;
			}
			@Override
			public Object onGetLock() throws InterruptedException {
				logger.info("获取锁业务处理");
				return 1;
			}
		});
		return Result.build(result);
	}
}
```
## 图形验证码工具使用
```
Captcha captcha = CaptchaTool.getCaptcha();
String randomStr = captcha.getChallenge();
```