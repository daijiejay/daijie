# 工程简介
* maven插件，提供生成mysql数据库结构文档、生成java文件。
# 使用说明
## maven依赖
```
            <plugin>
                <groupId>org.daijie</groupId>
                <artifactId>daijie-jdbc-plugin</artifactId>
                <version>2.0.0</version>
                <configuration>
                    <datasources>
                        <datasource>
                            <driverClassName>com.mysql.jdbc.Driver</driverClassName>
                            <url>jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&amp;characterEncoding=UTF8</url>
                            <username>root</username>
                            <password>123456</password>
                        </datasource>
                    </datasources>
                    <model>
                        <!--自定义model生成规则的指定类-->
                        <!--<modeConfigurationClass></modeConfigurationClass>-->
                        <targetPackage>org.daijie.springboot.orm.daijiejdbc.model</targetPackage>
                        <targetProject>src/main/java</targetProject>
                        <isOverrideToString>true</isOverrideToString>
                        <isOverrideEquats>true</isOverrideEquats>
                        <isLombok>false</isLombok>
                        <isSerializeble>true</isSerializeble>
                    </model>
                    <mapper>
                        <!--自定义mapper生成规则的指定类-->
                        <!--<mapperConfigurationClass></mapperConfigurationClass>-->
                        <targetPackage>org.daijie.springboot.orm.daijiejdbc.mapper</targetPackage>
                        <targetProject>src/main/java</targetProject>
                        <interfacePackages>
                            <interfacePackage>org.daijie.jdbc.session.SessionMapper</interfacePackage>
                        </interfacePackages>
                    </mapper>
                    <path>d://work//</path>
                    <fileName>doc</fileName>
                </configuration>
            </plugin>
```
## 生成JAVA文件
* maven命令
mvn daijie-jdbc:generator-java
## 生成HTML数据结构文档
* maven命令
mvn daijie-jdbc:doc
* 效果图
![数据结构文档](http://m.qpic.cn/psb?/V14KUPlZ1oRvxL/6KHHl5u1wFfftUW7F*SiXGRxB2jhUsT*wKBCgTmDWCE!/b/dEEBAAAAAAAA&bo=gAcGBAAAAAARF6U!&rf=viewer_4)