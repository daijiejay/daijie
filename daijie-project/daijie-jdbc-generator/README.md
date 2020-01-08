# 工程简介
* 提供生成mysql数据库结构文档、生成java文件。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-jdbc-generator</artifactId>
	<version>2.0.0</version>
</dependency>          
```
## 生成JAVA文件
```java
GeneratorConfiguration generatorConfiguration = new GeneratorConfiguration();
Map<String, Object> properties = new HashMap<>();
properties.put("driverClassName", "com.mysql.jdbc.Driver");
properties.put("url", "jdbc:mysql://localhost:3306/demo?characterEncoding=UTF-8");
properties.put("username", "root");
properties.put("password", "123456");
generatorConfiguration.addDatasource(properties);
ModelFileConfiguration modelFileConfiguration = FileConfigurationManager.newInstance(ModelFileConfiguration.class, "org.daijie.model", "src/test/java", true, true, false, true);
generatorConfiguration.addFileConfiguration(modelFileConfiguration);
MapperFileConfiguration mapperFileConfiguration = FileConfigurationManager.newInstance(MapperFileConfiguration.class, "org.daijie.mapper", "src/test/java", "org.daijie.model");
mapperFileConfiguration.addInterfacePackages("org.daijie.jdbc.session.SessionMapper");
generatorConfiguration.addFileConfiguration(mapperFileConfiguration);
new FileGenerator(generatorConfiguration).execute();
```
## 生成HTML数据结构文档
```java
AbstractHtmlFileConfiguration htmlFileConfiguration = FileConfigurationManager.newInstance(AbstractHtmlFileConfiguration.class, "C:\\Users\\daijie\\Desktop", "数据结构");
generatorConfiguration.addFileConfiguration(htmlFileConfiguration);
new FileGenerator(generatorConfiguration).execute();
```
