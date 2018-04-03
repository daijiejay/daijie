package org.daijie.core.util.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * FTP工具类
 * @author daijie_jay
 * @since 2018年1月2日
 */
public class FTPUtil {

	
	/**
	 * 获取ftp链接
	 * @param server 服务地址
	 * @param username 用户名
	 * @param password 密码
	 * @return FTPClient tfp客户端
	 * @throws IOException 抛出异常
	 */
    public static FTPClient getFTPClient(String server, String username, String password) throws IOException{
    	FTPClient ftpClient = null;
        try {
        	ftpClient = new FTPClient();
        	ftpClient.setControlEncoding("UTF-8");
            ftpClient.connect(server);
            ftpClient.login(username, password);
            
        } catch (SocketException e) {
            throw new SocketException(e.getMessage());
        } catch (IOException e) {
        	e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return ftpClient;
    }
     
    /**
     *  关闭ftpClient链接
     * 
     *  @param ftpClient 要关闭的ftpClient对象
     * 
     * */
    public static void closeFTPClient(FTPClient ftpClient){
        try {
            try{
                ftpClient.logout();
            }finally{
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	 /**
     * 上传文件
     *
     * @param ftpClient ftp客户端
     * @param f 要上传的文件
     * @param uploadDir 上传文件的根路径
     * @return boolean 上传结果
	 * @throws IOException 抛出异常
     * */
    public static boolean putFile(FTPClient ftpClient, File f,String uploadDir) throws IOException {
        InputStream instream = null;
        boolean result = false;
        try{
            try{
            	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
            	if(StringUtils.isNotEmpty(uploadDir)){
            		ftpClient.makeDirectory(uploadDir);
            		ftpClient.changeWorkingDirectory(uploadDir);
            	}
            	
                instream = new BufferedInputStream(new FileInputStream(f));
                ftpClient.enterLocalPassiveMode();
                result = ftpClient.storeFile(f.getName(), instream);
            }finally{
                if(instream!=null){
                    instream.close();
                }
            }
        }catch(IOException e){
            throw new IOException(e.getMessage());
        }
         
        return result;
    }
    
    /**
     * 删除文件
     * @param ftpClient ftp客户端
     * @param filepath filepath是带有上传目录的path, 后台系统上传的文件, 在数据库中已经存有上传目录, 所以就不用再加.
     * @return boolean
     * @throws IOException 抛出异常
     */
    public static boolean deleteFile(FTPClient ftpClient, String filepath) throws IOException{
    	boolean result = false;
    	try {
			result = ftpClient.deleteFile(filepath);
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
    	return result;
    }
    
    /**
     * 上传文件lxl
     * 参数：
     * @param ftpClient 已获得连接的ftpClient
     * @param uploadDir ftp服务器保存目录，如果是根目录则为/
     * @param fileName 上传到ftp服务器上的文件名
     * @param instream 本地文件输入流
     * @return boolean
     * @throws IOException 抛出异常
     */
    public static boolean uploadFile(FTPClient ftpClient, String uploadDir, String fileName, InputStream instream) throws IOException{
        boolean result = false;
        try{
            try{
            	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
            	if(StringUtils.isNotEmpty(uploadDir)){
            		ftpClient.makeDirectory(uploadDir);
            		ftpClient.changeWorkingDirectory(uploadDir);
            	}
                instream = new BufferedInputStream(instream);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setBufferSize(1024 * 1024 * 4);//设置缓冲大小为50M.解决上传速度慢的问题
                result = ftpClient.storeFile(fileName, instream);
            }finally{
                if(instream!=null){
                    instream.close();
                }
            }
        }catch(IOException e){
            throw new IOException(e.getMessage());
        }
        return result;
    }

}
