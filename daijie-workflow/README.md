# 工程简介
* 集成了activiti版本7-201802-EA，只是解决了spring boot版本兼容问题。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-workflow-spring-boot-starter</artifactId>
	<version>1.1.0-RELEASE</version>
</dependency>
```
## 使用activiti
* 启动类引用`@EnableActiviti`：
```java
@EnableActiviti
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
