# 工程简介
* daijie-jdbc集成Spring boot，Spring管理的自动注入Mapper和Spring事务。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-jdbc-spring-boot-starter</artifactId>
	<version>2.0.0</version>
</dependency>
```
## 代码说明
* 单数据源配置
```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/demo?characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=123456
```
* 多数据源配置
```
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
* 多数据源下选择哪个数据源，接口引用`@SelectDataSource`注解，不配置将使用默认配置的defaultName数据源。
mybatis使用：
```java
@SelectDataSource(name = "demo1")
public interface UserMapper extends Mapper<User>, ConditionMapper<User>, MySqlMapper<User> {
	...
}
```
* 创建mapper会话完全由spring管理，使用增删改查操作情况请看[daijie-jdbc](https://github.com/daijiejay/daijie/tree/master/daijie-project/daijie-jdbc "daijie-jdbc")
