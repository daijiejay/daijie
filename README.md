# 项目简介
提供基于spring-cloud系列整合的依赖jar包，比如分布式锁，接口文档，多数据源，单点登录，第三方集成等等可spring-boot配置的，简单化的使用工具。

本着一个学习的态度，可以是实现自己的一个想法，对我来说这是一个很有乐趣的事。项目是由我个人开发，主要解决工作项目中可能会遇到的各种问题，在第三方框架集成的基础上拓展需求，不实现具体业务。如果您在微服务中遇到了一些问题或有疑问，欢迎大家来这里提问或提出您的想法，我会及时给予回复，
并会在工作之余尽我所能的与您一起探讨钻研与实现。如果问题只需要集成第三方且不需要再次封装就能简单完成的，请关注我的dome（[daijie-example](https://github.com/daijiejay/daijie-example "demo")）项目。非常感谢！

邮箱：jay@daijie.org
## 文档介绍 
#### daijie-core [开发文档](https://github.com/daijiejay/daijie/tree/master/daijie-core)
* 提供访问异常统一处理、接口文档生成、流程枚举存储容器、分布式锁。
#### daijie-jdbc [开发文档](https://github.com/daijiejay/daijie/tree/master/daijie-jdbc)
* 提供mybatis与jpa多数据源配置，分布式事务。
#### daijie-jdbc-plugin [开发文档](https://github.com/daijiejay/daijie/tree/master/daijie-jdbc-plugin)
* maven插件，提供生成mysql数据库结构文档。
#### daijie-shiro [开发文档](https://github.com/daijiejay/daijie/tree/master/daijie-shiro)
* 提供非对称加密的单点登录。
#### daijie-shiro-security [开发文档](https://github.com/daijiejay/daijie/tree/master/daijie-shiro-security)
* 提供shiro权限路由跳转配置服务。
#### daijie-shiro-oauth2 [开发文档](https://github.com/daijiejay/daijie/tree/master/daijie-shiro-oauth2)
* 提供oauth2协议shiro权限路由跳转配置服务。
#### daijie-social [开发文档](https://github.com/daijiejay/daijie/tree/master/daijie-social)
* 提供第三方接口集成，已集成了第三方登录。
#### daijie-hadoop [开发文档](https://github.com/daijiejay/daijie/tree/master/daijie-hadoop)
* 提供hadoop服务，已集成了文件上传
## 项目demo
* [daijie-example](https://github.com/daijiejay/daijie-example "demo")

## 更新日志
标题 | 内容 | 完成情况 | 完成时间 | 发布状态 | 版本号
---|---|---|---|---|---
http请求响应封装 | 响应json对象封装，解决所有请求接口统一返回指定属性，包括异常处理，跳转路径异常时指定响应路径。 | 已完成 | 2017年5月10日 | 已发布 | 1.0.1-RELEASE
http请求数据处理 | 微服务请求报文与客服端请求header报文一致性处理，请求数据json转换param处理。 | 已完成 | 2017年5月13日 | 已发布 | 1.0.1-RELEASE
mybatis主从库配置 | 提供服务数据库读写分离，启用注解和指定数据源注解。 | 已完成 | 2017年5月14日 | 已发布 | 1.0.1-RELEASE
单点登录 | 集成shiro实现session集群单点登录，提供登录登出与登录用户获取工具类。 | 已完成 | 2017年6月24日 | 已发布 | 1.0.1-RELEASE
单点登录 | 登录工具类实现密码非对称加密，生成公钥密钥工具类。 | 已完成 | 2017年7月1日 | 已发布 | 1.0.1-RELEASE
消息队列 | 完成消息队列实例 | 已完成 | 2017年8月12日 | 已发布 | 1.0.1-RELEASE
定时任务 | 完成定时任务实例 | 已完成 | 2017年8月12日 | 已发布 | 1.0.1-RELEASE
第三方登录 | 提供QQ、微信、支付宝、新浪、百度登录工具类。 | 已完成 | 2017年10月10日 | 已发布 | 1.0.1-RELEASE
单点登录与访问权限服务 | 单点登录抽离成一个启动服务，集成zuul实现统一权限控制跳转。 | 已完成 | 2017年10月13日 | 已发布 | 1.0.1-RELEASE
outh2授权 | 集成spring-security、shiro、zuul开发启动服务，实现请求统一权限控制。 | 已完成 | 2017年10月15日 | 已发布 | 1.0.1-RELEASE
多数据源配置 | 提供mybatis、jpa多数据源配置，启用注解和指定数据源注解。 | 已完成 | 2017年10月19日 | 已发布 | 1.0.1-RELEASE
生成接口文档 | 集成swagger生成api文档，以配置形式自定义docket，可配置需要指定生成的组、包路径等。 | 已完成 | 2017年10月20日 | 已发布 | 1.0.1-RELEASE
统一配置服务 | 微服务统一配置服务 | 已完成 | 2017年10月25日 | 已发布 | 1.0.1-RELEASE
监控服务 | sleuth监控跟踪服务 | 已完成 | 2017年10月27日 |	已发布 | 1.0.1-RELEASE
工作流 | 集成activiti实现工作流案例 | 已完成 | 2017年11月10日 | 已发布 | 1.0.1-RELEASE
分布式锁 | 基于redis和zookeeper实现的分布式锁工具类。 | 已完成 | 2017年11月11日 | 已发布 | 1.0.1-RELEASE
单点登录 | 单点登录集成Kisso管理客服端cookie。 | 已完成 | 	2017年11月12日 | 已发布 | 1.0.1-RELEASE
流程枚举 | 添加流程枚举存储容器，提供线性枚举成员节点的有序序列存储和树形枚举成员节点的链表树状存储。 | 已完成 | 2017年12月20日 | 已发布 | 1.0.2-RELEASE
分布式锁 | 分布式锁添加异常回调处理。 | 已完成 | 2018年1月1日 | 已发布 | 1.0.2-RELEASE
分布式锁 | 定义在类和方法上的注解，配置业务编码做为锁，锁占用和锁异常时默认处理和配置指定方法处理。 | 已完成 | 2018年3月8日 | 已发布 | 1.0.3-RELEASE
设计模式 | 围绕着AOP设计模式定义工厂方法 | 已完成 | 2018年3月9日 | 已发布 | 1.0.3-RELEASE
请求处理 | 使用@RestController或@Controller时，处理异常默认结果，替代掉继承ApiController与WebController。 | 已完成 | 2018年3月23日 | 已发布 | 1.0.3-RELEASE
文件上传 | fastDFS文件上传工具类 | 已完成 | 2018年4月4日 | 已发布 | 1.0.4-RELEASE
升级版本 | spring-boot升级到2.0，spring-framework升级到5.0，解决兼容性问题 | 已完成 | 2018年4月24日 | 未发布 | 1.1.0-RELEASE
文件上传 | hadoop HDFS文件上传工具类 | 已完成 | 2018年4月25日 | 未发布 | 1.1.0-RELEASE
分布式事务 | 单线程mybatis、jpa多数据源动态切换分布式数据库事务处理 | 已完成 | 2018年5月28日 | 未发布 | 1.1.0-RELEASE
数据库结构文档 | 提供maven插件生成mysql数据库结构文档 | 已完成 | 2018年5月28日 | 未发布 | 1.1.0-RELEASE

## 任务计划
* 定义在方法上的注解，异步调用日志处理业务，可以从用户的几个角度解决问题，操作记录，行为记录，爱好记录，在线记录，地点记录，这些记录可以是日志文件输出，可以是数据库存储，这些只需要集成相关的第三方插件就能完成。
* 分布式服务下分布式事务解决方案。 
* 分布式锁实现高并发批处理。 
* 通过监控注册中心，利用swagger功能扩展对分布式项目接口文档集成式管理。
* 初步了解大数据，集成hadoop完成一个简单案例。
* 初步了解区块链，集成web3j完成一个简单案例。
