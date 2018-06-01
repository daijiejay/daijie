# 工程简介
* hadoop dfs实现的文件上传工具类。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-hadoop-spring-boot-starter</artifactId>
	<version>1.1.0-RELEASE</version>
</dependency>
```
## 文件上传
* 通过`@EnableHdfs`注解开启hdfs服务配置。
```java
@EnableHdfs
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}
}
```
* 工具类使用
```java
@RestController
public class UploadController {
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadFile(MultipartFile file) throws IOException {
		return HdfsUtil.upload(file);
    }
}
```
* properties相关配置
```
hadoop.dfs.serverUrl=127.0.0.1:9000
hadoop.dfs.downloadUrl=hdfs/download/
hadoop.dfs.dirPath=folder
hadoop.dfs.filePath=file
```