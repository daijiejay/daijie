# 架构拓朴图
![架构拓朴图](http://a1.qpic.cn/psb?/V14KUPlZ1oRvxL/9kRazxnujXv0imPslwT*YCJr93o73Oaucso7uxJUexc!/b/dPMAAAAAAAAA&bo=CQOAAgAAAAARAL8!&rf=viewer_4&t=5)
# 工程简介
## daijie-core
* 框架主要工程，集成spring-cloud与spring-boot系列jar包，所有的子工程都依赖本工程。
* 封装RESTful风格统一返回参数实体，包括正常返回及异常返回，Controller异常全局处理。
* 集成swagger生成api文档，以配置形式自定义docket，可配置需要指定生成的组、包路径等。
* 微服务请求报文与客服端请求报文一致性处理。
* 单点登录集成Kisso管理客服端cookie。
* 提供一些常用工具类。
* 加入了redis和zookeeper分布式锁，可配置单机或集群的redis及zookeeper，由@EnableRedisLock和@EnableZKLock开启自动装置。(注意：redis用到了avel命令，只支持2.6版本以上服务器)
### 使用说明
#### 基础说明
* 接口统一返回`ModelResult`实体，自定义`RestController`与`Controller`需要分别继承`ApiController`与`WebController`，其目的是需要统一管理Controller，目前已实现了异常处理，`ApiController`与`WebController`保证反给消费者的是`ModelResult`实体与`String`路径，`WebController`异常默认返回路径是“/error”，可以在加`@ErrorMapping`类注解自定义错误路径。
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
#### 生成api文档
* 启动类引用`@EnableMySwagger`注解
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
#### 分布式锁
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
#### 图形验证码工具使用
```
Captcha captcha = CaptchaTool.getCaptcha();
String randomStr = captcha.getChallenge();
```

## daijie-jdbc
* 替代daijie-mybatis，集成多个ORM框架，加入动态多数据源配置。
### 使用说明
#### 多数据源配置
* 启动类需要引用`@EnableMybatis`或`@EnableJpa`注解。
```
@EnableMybatis
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
* 目前只支付mybatis和jpa配置，配置基本一样，单数据源保持spirng-boot-autoconfigure的配置不变，多数据源需要定义names和defaultName。
```
#单数据源配置
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/demo?characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=123456
#多数据源配置
#spring.datasource.dataSourceType=com.alibaba.druid.pool.DruidDataSource
#spring.datasource.names=demo1,demo2
#spring.datasource.defaultName=demo1
#spring.datasource.demo1.driver-class-name=com.mysql.jdbc.Driver
#spring.datasource.demo1.url=jdbc:mysql://localhost:3306/demo1?characterEncoding=UTF-8
#spring.datasource.demo1.username=root
#spring.datasource.demo1.password=123456
#spring.datasource.demo2.driver-class-name=com.mysql.jdbc.Driver
#spring.datasource.demo2.url=jdbc:mysql://localhost:3306/demo2?characterEncoding=UTF-8
#spring.datasource.demo2.username=root
#spring.datasource.demo2.password=123456
#jpa配置需要添加扫描实体的包路径，多个以“,”号隔开
#spring.datasource.jpaEntityPackages=org.daijie.mybatis.model
```
* 多数据源下选择哪个数据源，service引用`@SelectDataSource`注解，不配置将使用默认配置的defaultName数据源。
```
@SelectDataSource("demo1")
@Service
public class UserService{
	@Autowired
	private UserMapper userMapper;
	...
}
```

## daijie-mybatis
* 集成tk-mybatis，提供单机和集群数据库自动配置。
* mybatis配置修改为properties和yml读取。

## daijie-shiro
* 集成shiro，提供单机和集群redis自动配置。
* shiro工具类封装，使用登录登出简单化，实现了session集群，任何工程只需依赖本工程就可获取当前登录用户信息和角色权限信息。
* shiro的cookie优化为更安全kisso进行管理，可以开关配置，默认kisso管理。
* shiro配置修改为properties和yml读取。
* 登录方法实现了RSA非对称加密算法。
* 集成zuul服务代理，通过`@EnableShiroSecurityServer`注解开启访问权限控制，再重定向到对应的子微服务。
### 使用说明
#### 启动shiro安全服务
* 启动类引用`@EnableShiroSecurityServer`注解
```
@EnableShiroSecurityServer(ShiroConfigure.class)
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
* shiro安全服务properties相关配置：
```
#添加自定义Filter，以“,”号隔开
shiro.filterClassNames=org.daijie.shiro.filter.SecurityFilter
#登录过期跳转的访问路径
shiro.loginUrl=/invalid
#登录成功跳转的访问路径
shiro.successUrl=/
#无权限时跳转的访问路径
shiro.unauthorizedUrl=/error
#拦截访问路径，以“,”号隔开
shiro.filterChainDefinitions=/**=anon,/login=credential,/api/user/**=security
#拦截访问路径，json对象格式
#shiro.filterChainDefinitionMap={"*/**":"anon"}

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
#为true时，访问/api/** = daijie-api-cloud/**，为false时，访问/api/** = daijie-api-cloud/api/**
zuul.routes.api.stripPrefix=false

```
#### SSO登录实现
* 启动类引用`@EnableShiro`注解
```
@EnableShiro(ShiroConfigure.class)
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
* properties相关配置：
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
```
* 工具类使用
```
@RestController
public class LoginController extends ApiController {
	private static final Logger logger = Logger.getLogger(LoginController.class);
	@Autowired
	private UserCloud userCloud;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelResult<Object> login(@RequestParam String username, @RequestParam String password) throws Exception{
		//公钥传给客户端
		String publicKey = Auth.getPublicKey();
		//客户端调用登录接口时进行公钥加密后传参调用此接口
		password = RSAUtil.encryptByPubKey(password, publicKey);
		
		//以下正式走登录流程
		User user = userCloud.getUser(username).getData();
		Auth.login(username, password, user.getSalt(), user.getPassword(), "user", user);
		return Result.build("登录成功", ApiResult.SUCCESS, ResultCode.CODE_200);
	}
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ModelResult<Object> logout(){
		Auth.logOut();
		return Result.build("退出成功", ApiResult.SUCCESS, ResultCode.CODE_200);
	}
	@ApiOperation(notes = "获取当前登录用户信息", value = "获取当前登录用户信息")
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelResult<User> getUser(){
		User user = (User) Auth.getAuthc("user");
		return userCloud.getUser(user.getUserId());
	}
}
```

## daijie-social
* 集成第三方接口，提供QQ、微信、支付宝、新浪、百度登录。
* 实现了web端第三方授权跳转页登录。
### 使用说明
#### 第三方登录
* 启动类引用`@EnableSocialLogin`：
```
@EnableSocialLogin
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
* properties相关配置，使用微信登录前缀为weixin，还有其它的baidu、ali、sina、qq登录配置
```
weixin.login.appid=
weixin.login.appsecret=
weixin.login.redirectUri=/index
weixin.login.errorUri=/error
weixin.login.callbackUri=/weixin/callback
```
* 工具类使用
```
@Controller
public class SocialLoginController {
	private static final Logger logger = Logger.getLogger(SocialLoginController.class);
	/**
	 * 访问微信二维码
	 * @param response
	 */
	@RequestMapping(value = "weixin/qrcode", method = RequestMethod.GET)
	public String loadQrcode(String state, HttpServletResponse response){
		return LoginTool.loadQrcode(state, SocialLoginType.WEIXIN);
	}
	/**
	 * 微信扫码回调登录业务处理
	 * @param code
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "weixin/callback", method = RequestMethod.GET)
	public String wxCallback(@RequestParam String code, String state){
		return LoginTool.login(code, SocialLoginType.WEIXIN, new WeixinLoginCallback() {
			@Override
			public void handle(WeixinUserInfo userInfo) {
				logger.info("登录成功业务处理");
			}
			@Override
			public void errer(WeixinError error) {
				logger.info("登录失败业务处理");
				logger.error(error.getErrmsg());
			}
		});
	}
}
```

---
## daijie-eureka-service
* eureka注册中心服务器。
## daijie-config
* 配置中心集中式配置文件管理。
## daijie-config-service
* 配置中心服务器。
## daijie-seluth-service
* 微服务监控服务器。
## daijie-admin-service
* 微服务监控与管理服务器。

---
## daijie-mybatis-model
* 集成mybatis-generator工具自动生成model和mapper的实例。
* 工程作用定义：与数据库对应实体统一管理，不做人工代码修改，利于数据库结构变动只需重新生成即可。
## daijie-mybatis-cloud & daijie-jpa-cloud & daijie-mybatis-shardingjdbc-cloud
* 对数据库进行crud操作实例。
* mybatis-shardingjdbc-cloud工程集成sharding-jdbc完成分表操作实例，sharding-jdbc对分库分表水平拆分做了很好支持，适当的解决了分布式事务。
* 工程作用定义：产品模块化的分布式直接访问数据库，可以有对业务性逻辑处理，但此工程不与客户端业务直接交互，比如获取客户端请求报文不在此工程处理。
## daijie-shiro-api
* 依赖daijie-shiro提供客服端RESTful api接口，完成单点登录，session集群，cookie安全实例。
* 完成在此工程操作集群session中的登录用户信息实例及redis单独使用实例。
* 完成一个简单的分布式锁实例。
* 工程作用定义：产品模块化的分布式接口提供，并形成swagger可视化接口文档，由此工程请求其它的cloud服务，注意的是此工程不提供给客户端直接访问，只提供给shiro-security工程访问。
## daijie-shiro-security
* 依赖daijie-shiro启动引用`@EnableShiroSecurityServer`注解安全控制实例。
* 工程作用定义：单点登录，shiro自定义请求权限统一管理，由客户端直接请求。
## daijie-elasticsearch-cloud
* 依赖elasticsearch完成搜索实例。
* 工程作用定义：数据搜索业务，搜索引擎通过api工程调用。
## daijie-rabbit-cloud
* 依赖rabbit完成生产消费消息实例。
* 工程作用定义：部分高并发，复杂算法，无需即时回馈的业务处理，自己生产自己消费，或其它集群rabbit服务消费，通过api工程调用。
## daijie-quaryz-cloud
* 依赖quaryz完成定时任务实例。
* 工程作用定义：处理系统自动执行的业务。
## daijie-activiti-cloud
* 依赖activiti完成流程实例。
* 工程作用定义：流程业务逻辑集中式处理，由api工程调用，此工程只处理流程中的业务逻辑和提供可视化的流程图，订单的处理人、处理结果、状态、历史记录等等需要维护到具体业务的数据库中。
## daijie-social-cloud
* 依赖social完成第三方登录实例。
* 工程作用定义：与第三方交互。
