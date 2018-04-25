package org.daijie.hadoop.dfs.service;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.daijie.hadoop.dfs.HdfsProperties;
import org.springframework.web.multipart.MultipartFile;

/**
 * hadoop dfs文件管理服务
 * @author daijie_jay
 * @since 2018年4月25日
 */
public interface HdfsService {
	
	/**
	 * hadoop dfs配置
	 * @param configuration 配置类
	 */
	void setConfiguration(Configuration configuration);
	
	/**
	 * hadoop dfs属性
	 * @param hdfsProperties 属性类
	 */
	void setHdfsProperties(HdfsProperties hdfsProperties);

	/**
	 * 上传文件
	 * @param file 文件流
	 * @return String 文件路径
	 * @throws IOException
	 */
	String upload(MultipartFile file) throws IOException;
	
	/**
	 * 删除文件
	 * @param fileName 文件名
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	void remove(String fileName) throws IllegalArgumentException, IOException;
	
	/**
	 * 下载文件
	 * @param fileName 文件名
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	void download(String fileName) throws IllegalArgumentException, IOException;
}
