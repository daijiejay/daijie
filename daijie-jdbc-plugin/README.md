# 工程简介
* maven插件，提供生成mysql数据库结构文档。
# 使用说明
## maven依赖
```
<plugin>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-jdbc-plugin</artifactId>
	<version>1.1.0-RELEASE</version>
	<configuration>
		<url>jdbc:mysql://localhost:3306/demo?user=root&amp;password=123456&amp;useUnicode=true&amp;charaterEncoding=utf-8</url>
		<path>d://doc.html</path>
	</configuration>
</plugin>
```
## maven命令
* mvn daijie-jdbc:doc
## 效果图
![数据结构文档](http://m.qpic.cn/psb?/V14KUPlZ1oRvxL/6KHHl5u1wFfftUW7F*SiXGRxB2jhUsT*wKBCgTmDWCE!/b/dEEBAAAAAAAA&bo=gAcGBAAAAAARF6U!&rf=viewer_4)