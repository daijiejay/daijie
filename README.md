# 架构拓朴图
![架构拓朴图](http://a1.qpic.cn/psb?/V14KUPlZ1oRvxL/9kRazxnujXv0imPslwT*YCJr93o73Oaucso7uxJUexc!/b/dPMAAAAAAAAA&bo=CQOAAgAAAAARAL8!&rf=viewer_4&t=5)
# 工程简介
## daijie-core
* 框架主要工程，集成spring-cloud与spring-boot系列jar包，所有的子工程都依赖本工程。
* 封装RESTful风格统一返回参数实体，包括正常返回及异常返回，Controller异常全局处理。
* 微服务请求报文与客服端请求报文一致性处理。
* 单点登录集成Kisso管理客服端cookie。
* 提供一些常用工具类。
## daijie-mybatis
* 集成tk-mybatis，提供单机和集群数据库自动配置。
* mybatis配置修改为properties和yml读取。
## daijie-shiro
* 集成shiro，提供单机和集群redis自动配置。
* shiro使用登录登出简单化，实现了session集群，任何工程只需依赖本工程就可获取当前登录用户信息和角色权限信息。
* shiro的cookie优化为更安全kisso进行管理。
