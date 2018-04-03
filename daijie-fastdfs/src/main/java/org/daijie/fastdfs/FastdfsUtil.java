package org.daijie.fastdfs;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

@Component
public class FastdfsUtil {
	
	public static FastFileStorageClient storageClient;

	@Autowired
	public void setStorageClient(FastFileStorageClient storageClient) {
		FastdfsUtil.storageClient = storageClient;
	}

	public static String upload(MultipartFile file) throws IOException{
		StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
        return storePath.getFullPath();
	}
	
	public static void remove(String filePath) {
		storageClient.deleteFile(filePath);
	}
}
