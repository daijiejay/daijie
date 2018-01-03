# 工程简介
* 替代daijie-mybatis，集成多个ORM框架，加入动态多数据源配置。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-jdbc-spring-boot-starter</artifactId>
	<version>1.0.1-RELEASE</version>
</dependency>
```
## 多数据源配置
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
* 目前只支持mybatis和jpa配置，配置基本一样，单数据源保持spirng-boot-autoconfigure的配置不变，多数据源需要定义names和defaultName。
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
