package org.daijie.hadoop.dfs;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * hadoop dfs配置属性类
 * @author daijie_jay
 * @since 2018年4月25日
 */
@ConfigurationProperties("hadoop.dfs")
public class HdfsProperties {

	private String serverUrl;
	
	private String dirPath;
	
	private String filePath;
	
	private String downloadUrl;

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
}
