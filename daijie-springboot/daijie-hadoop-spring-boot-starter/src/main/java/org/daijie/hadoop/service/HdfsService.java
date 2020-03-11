package org.daijie.hadoop.service;

import org.apache.hadoop.conf.Configuration;
import org.daijie.hadoop.HdfsProperties;
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
	 */
	String upload(MultipartFile file);
	
	/**
	 * 删除文件
	 * @param fileName 文件名
	 */
	void remove(String fileName);
	
	/**
	 * 下载文件
	 * @param fileName 文件名
	 */
	void download(String fileName);
}
