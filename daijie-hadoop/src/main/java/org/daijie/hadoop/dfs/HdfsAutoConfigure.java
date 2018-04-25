package org.daijie.hadoop.dfs;

import org.apache.commons.lang3.StringUtils;
import org.daijie.hadoop.dfs.service.FileHdfsService;
import org.daijie.hadoop.dfs.service.HdfsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * hadoop dfs自动配置类
 * @author daijie_jay
 * @since 2018年4月25日
 */
@Configuration
@EnableConfigurationProperties({HdfsProperties.class})
public class HdfsAutoConfigure {
	
	private static final Logger logger = LoggerFactory.getLogger(HdfsAutoConfigure.class);
	
	@Bean
	public HdfsService hdfsService(org.apache.hadoop.conf.Configuration configuration, HdfsProperties hdfsProperties){
		HdfsService hdfsService = new FileHdfsService();
		hdfsService.setConfiguration(configuration);
		hdfsService.setHdfsProperties(hdfsProperties);
		return hdfsService;
	}

	@Bean
	public org.apache.hadoop.conf.Configuration configuration(HdfsProperties hdfsProperties){
		try{
			org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
			String serverUrl = hdfsProperties.getServerUrl();
			if(StringUtils.isNotBlank(serverUrl)){
				conf.set("fs.default.name", "hdfs://" + hdfsProperties.getServerUrl());
			}else{
				conf.set("fs.default.name", "hdfs://localhost:9000");
			}
			conf.set("hadoop.job.ugi","cluster");
			conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
			conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
			return conf;
		}catch(Exception e){
			logger.error("hdfs配置错误", e);
			return null;
		}
	}
}
