# 工程简介
* 基于spring-cloud与spring-boot系列jar包，以解决分布式问题为目标提供简单易用的集成工具。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-core-spring-boot-starter</artifactId>
	<version>1.1.0-RELEASE</version>
</dependency>
```
## 异常全局处理
* 自定义`@RestController`与`@Controller`需要分别继承`ApiController`与`WebController`，其目的是需要统一管理Controller，目前已实现了异常处理，`ApiController`与`WebController`保证反给消费者的是`ModelResult`实体与`String`路径，`WebController`异常默认返回路径是“/error”，可以在`Controller`上加`@ErrorMapping`类注解自定义异常时跳转路径。
```java
@RestController
public class TestController extends ApiController {
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	public ModelResult<String> getData(){
		return Result.build("data");
	}
}
```
```java
@ErrorMapping(path="/error")
@Controller
public class HomeController extends WebController {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getData(){
		return "index";
	}
}
```
* 通过注解`@EnableExceptionHandler`开启异常处理，检测`@RestController`或`@ResponseBody`的请求返回的是`ModelResult`实体，检测`@Controller`的请求返回的是`String`路径，默认返回路径是“/error”，可以在`Controller`上加`@ErrorMapping`类注解自定义异常时跳转路径。
```java
@EnableExceptionHandler
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
## 图形验证码工具使用
```java
Captcha captcha = CaptchaTool.getCaptcha();
String randomStr = captcha.getChallenge();
```
## 请求处理配置
* 启动类引用`@EnableParametersFilter`注解
```java
@EnableParametersFilter
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
* 任何请求参数格式转param参数的配置
```
//开启body转param参数
http.bodyByParamEanble=true
//需要处理的请求方式
http.bodyByParamMethods=GET,POST,PUT,DELETE,OPTIONS
```
* ajax跨域的配置
```
//开启ajax跨域请求
http.remoteAjaxEanble=true
//允许跨域请求的地址集
http.accessControlAllowOrigin=http://daijie.org
//允许跨域请求的请求方式
http.accessControlAllowMethods=GET,POST,PUT,DELETE,OPTIONS
//允许设置headers的字段名
http.accessControlAllowHeaders=Content-Type,Access-Control-Allow-Headers,Access-Control-Allow-Origin,Authorization,X-Requested-With
```