# 架构拓朴图
![架构拓朴图](http://a1.qpic.cn/psb?/V14KUPlZ1oRvxL/9kRazxnujXv0imPslwT*YCJr93o73Oaucso7uxJUexc!/b/dPMAAAAAAAAA&bo=CQOAAgAAAAARAL8!&rf=viewer_4&t=5)
# 工程简介
## daijie-core
* 框架主要工程，集成spring-cloud与spring-boot系列jar包，所有的子工程都依赖本工程。
* 封装RESTful风格统一返回参数实体，包括正常返回及异常返回，Controller异常全局处理。
* 集成swagger生成api文档。
* 微服务请求报文与客服端请求报文一致性处理。
* 单点登录集成Kisso管理客服端cookie。
* 提供一些常用工具类。
* 加入了redis和zookeeper分布式锁，可配置单机或集群的redis及zookeeper，由@EnableRedisLock和@EnableZKLock开启自动装置。(注意：redis用到了avel命令，只支持2.6版本以上服务器)
## daijie-mybatis
* 集成tk-mybatis，提供单机和集群数据库自动配置。
* mybatis配置修改为properties和yml读取。
## daijie-shiro
* 集成shiro，提供单机和集群redis自动配置。
* shiro使用登录登出简单化，实现了session集群，任何工程只需依赖本工程就可获取当前登录用户信息和角色权限信息。
* shiro的cookie优化为更安全kisso进行管理，可以开关配置，默认kisso管理。
* shiro配置修改为properties和yml读取。
* 登录方法实现了RSA非对称加密算法。
## daijie-social
* 集成第三方接口，提供QQ、微信、支付宝、新浪、百度登录。
* 实现了web端第三方授权跳转页登录。
###使用说明
*启动类引用`@EnableSocialLogin`：
```
@EnableSocialLogin
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
*配置中心相关配置：
```
weixin.login.appid=
weixin.login.appsecret=
weixin.login.redirectUri=/index
weixin.login.errorUri=/error
weixin.login.callbackUri=/weixin/callback
```
*工具类使用
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
		return LoginTool.loadQrcode(state, SocialLoginEnum.WEIXIN);
	}
	/**
	 * 微信扫码回调登录业务处理
	 * @param code
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "weixin/callback", method = RequestMethod.GET)
	public String wxCallback(@RequestParam String code, String state){
		return LoginTool.login(code, SocialLoginEnum.WEIXIN, new WeixinLoginCallback() {
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
## daijie-eureka-service
* eureka注册中心服务器。
## daijie-config
* 配置中心集中式配置文件管理。
## daijie-config-service
* 配置中心服务器。
## daijie-seluth-service
* 微服务监控服务器。
## daijie-mybatis-model
* 集成mybatis-generator工具自动生成model和mapper的实例。
* 工程作用定义：与数据库对应实体统一管理，不做人工代码修改，利于数据库结构变动只需重新生成即可。
## daijie-mybatis-cloud & daijie-jpa-cloud & daijie-mybatis-shardingjdbc-cloud
* 对数据库进行crud操作实例。
* mybatis-shardingjdbc-cloud工程集成sharding-jdbc完成分表操作实例，sharding-jdbc对分库分表水平拆分做了很好支持。
* 工程作用定义：产品模块化的分布式直接访问数据库，可以有对业务性逻辑处理，但此工程不与客户端业务直接交互，比如获取客户端请求报文不在此工程处理。
## daijie-shiro-api
* 依赖daijie-shiro提供客服端RESTful api接口实例。
* 完成在此工程操作集群session中的登录用户信息实例及redis单独使用实例。
* 完成一个简单的分布式锁实例。
* 工程作用定义：产品模块化的分布式接口提供，并形成swagger可视化接口文档，由此工程请求其它的cloud服务，注意的是此工程不提供给客户端直接访问，只提供给shiro-security工程访问。
## daijie-shiro-security
* 依赖daijie-shiro完成单点登录，session集群，cookie安全实例。
* 此工程集成了zuul，实现反向代理请求api工程。
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
