# 工程简介
* 集成第三方接口，提供QQ、微信、支付宝、新浪、百度登录。
* 实现了web端第三方授权跳转页登录。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-social-spring-boot-starter</artifactId>
	<version>1.1.0-RELEASE</version>
</dependency>
```
## 第三方登录
* 启动类引用`@EnableSocialLogin`：
```java
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
```java
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
