# 工程简介
* 集成swagger生成api文档，以配置形式自定义docket，可配置需要指定生成的组、包路径等。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-swagger-spring-boot-starter</artifactId>
	<version>2.0.0</version>
</dependency>
```
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
#组名称
swagger.groupName=
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