# 工程简介
* 基于spring-cloud与spring-boot系列jar包，以解决分布式问题为目标提供简单易用的集成工具。
* 封装RESTful风格统一返回参数实体，包括正常返回及异常返回，Controller异常全局处理。
* 集成swagger生成api文档，以配置形式自定义docket，可配置需要指定生成的组、包路径等。
* 微服务请求报文与客服端请求header报文一致性处理，请求数据json转换param处理。
* 单点登录集成Kisso管理客服端cookie。
* 加入了redis和zookeeper分布式锁，可配置单机或集群的redis及zookeeper，由@EnableRedisLock和@EnableZKLock开启自动装置。(注意：redis用到了avel命令，只支持2.6版本以上服务器)
* 添加流程枚举存储容器，枚举成员节点提供线性数据结构的有序序列存储和双向链表存储、树形数据结构的树形链表存储、图形数据结构的十字链表存储。
* 提供一些常用工具类。
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
```java
## 生成api文档
* 启动类引用`@EnableMySwagger`注解，官方的`@EnableSwagger2`注解被重写
```java
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
```java
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
```java
@RestController
public class LockController {
	private static final Logger logger = Logger.getLogger(LockController.class);
	@RequestMapping(value = "testLock", method = RequestMethod.GET)
	public ModelResult<Object> testLock(){
		Object result = LockTool.execute("test", 1000, new Callback() {
			@Override
			public Object onTimeout() throws InterruptedException {
				logger.info("锁占用业务处理");
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
* 注解类使用

引用org.daijie.core.lock.@Lock注解，该类需要被spring管理方可生效，具体配置（
argName：param参数作为锁业务ID；
lockId: 业务ID，优先级大于argNme配置，默认方法名作为唯一字符串；
timeOut: 锁时长，默认5秒；
timeOutMethodName：锁占用时需要执行的方法，默认不执行；
errorMethodName：锁异常时需要执行的方法，默认不执行。）
```java
@RestController
public class LockController {
	private static final Logger logger = Logger.getLogger(LockController.class);
	@Lock(argName = "id", 
			timeout = 5000,
			errorMethodName = "org.daijie.api.controller.LockController.lockError(java.lang.String)", 
			timeOutMethodName = "org.daijie.api.controller.LockController.lockTimeOut(java.lang.String)")
	@ApiOperation(notes = "锁注解测试", value = "锁注解测试")
	@RequestMapping(value = "testLockAnnotation", method = RequestMethod.GET)
	public ModelResult<Object> testLockAnnotation(@ApiParam(value="业务编号") @RequestParam String id){
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Result.build("业务"+id+"锁已获取，获取锁业务处理");
	}
	public String lockTimeOut(String id){
		return "业务"+id+"锁已被占用，锁占用业务处理";
	}
	public String lockError(String id){
		return "业务"+id+"锁获取失败，锁异常业务处理";
	}
}
```
## 图形验证码工具使用
```java
Captcha captcha = CaptchaTool.getCaptcha();
String randomStr = captcha.getChallenge();
```
## 流程枚举存储容器
### 线性枚举成员节点的有序序列存储
* 有序序列存储需要实现`OrderEnumProcessFactory`接口，并实现它的方法
* 举例，请假流程为“请假申请”->"项目组长审批"->"项目经理审批"->"部门经理审批"->"人事自动审批"->"请假完成"->"流程结束"，其中领导审批有一个不同意都是直接流程结束。
```java
public enum LeaveStatus implements OrderEnumProcessFactory<LeaveStatus> {
	APPLY(1, null, "请假申请"),	
	PROJECT_LEADER(2, "projectLeaderId", "项目组长审批"),	
	PROJECT_MANAGER(3, "projectManagerId", "项目经理审批"),	
	DEPARTMENT_MANAGER(4, "departmentManagerId", "部门经理审批"),	
	PERSONNEL(5, null, "人事自动审批"),	
	COMPLATE(6, null, "请假完成"),	
	REFUSE(7, null, "流程结束");
	
	private Integer status;	
	private String assignee;	
	private String msg;
	
	LeaveStatus(Integer status, String assignee, String msg){
		this.status = status;
		this.assignee = assignee;
		this.msg = msg;
	}

	@Override
	public Integer getStatus() {
		return status;
	}
	@Override
	public String getMsg() {
		return msg;
	}
	@Override
	public String getAssignee() {
		return assignee;
	}
	@Override
	public LeaveStatus getEnumType() {
		return this;
	}
	@Override
	public LeaveStatus[] getEnumTypes() {
		return LeaveStatus.values();
	}
```
* 容器使用
```java
//获取下一个流程节点
LeaveStatus.APPLY.nextProcess(Process.THROUGH);
//获取上一个流程节点
LeaveStatus.PROJECT_LEADER.preProcess(Process.THROUGH);
//获取流程结束节点
LeaveStatus.PROJECT_MANAGER.nextProcess(Process.NOT_THROUGH);
```
### 树形枚举成员节点的链表树状存储
* 链表树状存储需要实现`TreeEnumProcessFactory`接口，并实现它的方法
* 举例，文物备案主线流程为“申请文物备案”->“用户支付”->“初审”->“复审”->“预约实物线下终审”->“客户到场确认”->“终审”->“备案入库”->“备案完成”，例举其中一个分支支线任务，“初审”->“重新提交”或者“初审”->“退款”，在流程当中一般只有四种流程走向（并行和串行走也都是同一个流程），这里定义了枚举类`Process`，分别对应为“通过”、“不通过”、“拒绝”、“退回”四个操作，流转时根据业务自由定义。
```java
public enum RelicStatus implements TreeEnumProcessFactory<RelicStatus> {
	APPLY("username", "申请文物备案"),
	PAY("username", "用户支付"),
	TRIAL("auditor", "初审"),
	REVIWE(null, "复审"),
	APPOINTENT("customerService", "预约实物线下终审"),
	ARRIVE("customerService", "客户到场确认"),
	LAST_TRIAL(null, "终审"),
	RECORD("operator", "备案入库"),
	SUBMIT("username", "重新提交"),	
	REFUND(null, "退款"),
	FAIL(null, "备案失败"),
	CANEL(null, "备案取消"),
	COMPLATE(null, "备案完成");
	
	private String assignee;
	private String msg;

	RelicStatus(String assignee, String msg){
		this.assignee = assignee;
		this.msg = msg;
	}

	@Override
	public String getMsg() {
		return msg;
	}
	@Override
	public String getAssignee() {
		return assignee;
	}
	@Override
	public RelicStatus getEnumType() {
		return this;
	}	
	@Override
	public RelicStatus[] getEnumTypes() {
		return RelicStatus.values();
	}
	@Override
	public TreeEnumProcess<RelicStatus> getEnumProcess() {
		TreeEnumProcess<RelicStatus> process = new TreeEnumProcess<>();
		//初始化主线流程，这里会从上往下关连流程，或者是这样this.process.add(new RelicStatus[]{APPLY, PAY, ...});
		process.add(APPLY);//申请备案
		process.add(PAY);//支付
		process.add(TRIAL);//初审
		process.add(REVIWE);//复审
		process.add(APPOINTENT);//预约线下审核
		process.add(ARRIVE);//用户到场确认
		process.add(LAST_TRIAL);//终审
		process.add(RECORD);//备案入库
		process.add(COMPLATE);//备案完成
		//设置支线流程，关连其它节点
		process.setBranch(PAY, CANEL, Process.NOT_THROUGH);//支付->备案取消，条件为不通过（如用户取消订单操作）
		process.setBranch(TRIAL, SUBMIT, TRIAL, Process.REJECT);//初审->重新提交->初审，条件为拒绝（如初审为图片不清晰）
		process.setBranch(TRIAL, REFUND, CANEL);//初审->退款->备案取消，下节点只有一个
		process.setBranch(REVIWE, FAIL, Process.NOT_THROUGH);//复审->备案失败，条件为不通过（如文物没有价值意义）
		process.setBranch(APPOINTENT, CANEL, Process.NOT_THROUGH);//预约线下审核->备案取消，条件为不通过（如客服在电话预约中用户取消备案）
		process.setBranch(ARRIVE, APPOINTENT, Process.RETURN);//用户到场确认->预约线下审核，条件为退回（如用户约定当天未到场）
		process.setBranch(LAST_TRIAL, FAIL, Process.NOT_THROUGH);//终审->备案失败，条件为不通过（如文物仿造）
		return process;
	}
}
```
* 容器使用
```java
//获取主线流程的下一个流程节点（节点“APPLY（申请）”申请成功，得到下一个节点为“PAY（支付）”）
LeaveStatus.APPLY.nextProcess(Process.THROUGH);
//获取主线流程的上一个流程节点（得到上一个节点为“REVIWE（复审）”）
LeaveStatus.APPOINTENT.preProcess(Process.THROUGH);
//获取支线流程的下个流程节点（节点“TRIAL（初审）”审批为拒绝，得到下一个节点为“SUBMIT（重新提交）”）
LeaveStatus.TRIAL.nextProcess(Process.REJECT);
//获取支线流程的下个流程节点（节点“TRIAL（初审）”审批为不通过，得到下一个节点为“REFUND（退款）”）
LeaveStatus.TRIAL.nextProcess(Process.NOT_THROUGH);
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
* 请求body转param参数的配置
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
```