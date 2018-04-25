package org.daijie.hadoop.dfs.service;

import java.io.IOException;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;
import org.daijie.core.util.http.HttpConversationUtil;
import org.daijie.hadoop.dfs.HdfsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * hadoop dfs文件管理服务具体实现类
 * @author daijie_jay
 * @since 2018年4月25日
 */
public class FileHdfsService implements HdfsService {
	
	private static final Logger logger = LoggerFactory.getLogger(FileHdfsService.class);
	
	private Configuration configuration;
	
	private HdfsProperties hdfsProperties;

	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void setHdfsProperties(HdfsProperties hdfsProperties) {
		this.hdfsProperties = hdfsProperties;
	}

	@Override
	public String upload(MultipartFile file) throws IOException {
		FileSystem fs;
		try {
			fs = FileSystem.get(configuration);
			createDir(hdfsProperties.getDirPath());
			String name = UUID.randomUUID().toString().replaceAll("-", "") 
					+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			String path = hdfsProperties.getDirPath() + "/" + hdfsProperties.getFilePath() + "/" + name;
			FSDataOutputStream out = fs.create(new Path(path));  
	        IOUtils.copyBytes(file.getInputStream(), out, 1024, true);
	        fs.close();
	        return hdfsProperties.getDownloadUrl() + "/" + name;
		} catch (IOException e) {
			logger.error("上传失败", e);
			return null;
		}
	}

	@Override
	public void remove(String fileName) throws IllegalArgumentException, IOException {
		FileSystem fs;
		try {
			fs = FileSystem.get(configuration);
			String path = hdfsProperties.getDirPath() + "/" + hdfsProperties.getFilePath() + "/" +fileName;
			fs.delete(new Path(path), true);
	        fs.close();
		} catch (IOException e) {
			logger.error("删除失败", e);
		}
	}

	@Override
	public void download(String fileName) throws IllegalArgumentException, IOException {
		FileSystem fs;
		try {
			fs = FileSystem.get(configuration);
			String path = hdfsProperties.getDirPath() + "/" + hdfsProperties.getFilePath() + "/" +fileName;
			FSDataInputStream in = fs.open(new Path(path));  
	        IOUtils.copyBytes(in, HttpConversationUtil.getResponse().getOutputStream(), 1024, true);
	        fs.close();
		} catch (IOException e) {
			logger.error("下载失败", e);
		}
	}

	/**
	 * 创建目录
	 * @param hdfsPath 目录名
	 * @return boolean 是否创建成功
	 */
	public boolean createDir(String hdfsPath){
		FileSystem fs;
		try {
			fs = FileSystem.get(configuration);
			if(!fs.exists(new Path(hdfsProperties.getDirPath()))){
		        fs.mkdirs(new Path(hdfsProperties.getDirPath()),new FsPermission((short)0755));
		        fs.close();
			}
			return true;
		} catch (IOException e) {
			logger.error("创建目录失败", e);
			return false;
		}
	}
}
