# 工程简介
* 集成shiro，提供单机和集群redis自动配置。
* shiro工具类封装，使用登录登出简单化，实现了session集群，任何工程只需依赖本工程就可获取当前登录用户信息和角色权限信息。
* shiro的cookie优化为更安全kisso进行管理，可以开关配置，默认kisso管理。
* shiro配置修改为properties和yml读取，保留shiro原来的配置方式一致，filterClassNames的名字前缀与filterChainDefinitions必须要一致，第一个字母小写，比如UserFilter对应user。
* 登录方法实现了RSA非对称加密算法。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-shiro-spring-boot-starter</artifactId>
	<version>1.0.4-RELEASE</version>
</dependency>
```
## SSO登录实现
* 启动类引用`@EnableShiro`注解
```
@EnableShiro
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
#cookie的作用域，kisso只能是域名才生效，如果是本地调试，可以配置host或者关闭kisso
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
		//加入角色权限
		Auth.refreshRoles(Redis.getToken(), new ArrayList<String>());
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