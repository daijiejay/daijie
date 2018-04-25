package org.daijie.hadoop.dfs;

import java.io.IOException;

import org.daijie.hadoop.dfs.service.HdfsService;
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
	 * @throws IOException
	 */
	public static String upload(MultipartFile file) throws IOException{
        return hdfsService.upload(file);
	}
	
	/**
	 * 删除文件
	 * @param fileName 文件名
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static void remove(String fileName) throws IllegalArgumentException, IOException {
		hdfsService.remove(fileName);
	}
	
	/**
	 * 下载文件
	 * @param fileName 文件名
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static void download(String fileName) throws IllegalArgumentException, IOException {
		hdfsService.download(fileName);
	}
}
