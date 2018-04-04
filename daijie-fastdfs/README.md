# 工程简介
* 文件上传工具类，依赖的com.github.tobato包实现。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-fastdfs-spring-boot-starter</artifactId>
	<version>1.0.4-RELEASE</version>
</dependency>
```
## 文件上传
* 通过`@EnableFastdfs`注解开启fastdfs服务配置。
```
@EnableFastdfs
@SpringBootApplication
public class BootApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(BootApplication.class).web(true).run(args);
	}
}
```
* 工具类使用
```
@RestController
public class UploadController {
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadFile(MultipartFile file) throws IOException {
		return FastdfsUtil.upload(file);
    }
}
```
* properties相关配置
```
fdfs.soTimeout=1500
fdfs.connectTimeout=600
fdfs.thumbImage.width=150
fdfs.thumbImage.height=150
fdfs.trackerList[0]=127.0.0.1:22122
```