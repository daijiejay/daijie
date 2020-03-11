package org.daijie.hadoop;

import org.daijie.hadoop.service.HdfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * hadoop hds工具类
 * @author daijie_jay
 * @since 2018年4月25日
 */
@Component
public class HdfsUtil {
	
	private static HdfsService hdfsService;

	@Autowired
	public void setHdfsService(HdfsService hdfsService) {
		HdfsUtil.hdfsService = hdfsService;
	}
	
	/**
	 * 上传文件
	 * @param file 文件流
	 * @return String 文件访问路径
	 */
	public static String upload(MultipartFile file) {
        return hdfsService.upload(file);
	}
	
	/**
	 * 删除文件
	 * @param fileName 文件名
	 */
	public static void remove(String fileName) {
		hdfsService.remove(fileName);
	}
	
	/**
	 * 下载文件
	 * @param fileName 文件名
	 */
	public static void download(String fileName) {
		hdfsService.download(fileName);
	}
}
