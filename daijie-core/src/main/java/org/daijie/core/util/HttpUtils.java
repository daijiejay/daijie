package org.daijie.core.util;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

/**
 * 
 * @author daijie
 * @date 2017年6月5日
 * http请求接口工具类
 * 
 */
@SuppressWarnings({ "deprecation", "resource", "restriction" })
public class HttpUtils {

	/**   
	 * 程序中访问http数据接口   
	 */    
	public static String getURLContent(String urlStr) {               
		/** 网络的url地址 */        
		URL url = null;                        
		/**//** 输入流 */   
		BufferedReader in = null;   
		StringBuffer sb = new StringBuffer();   
		try{     
			url = new URL(urlStr);     
			in = new BufferedReader( new InputStreamReader(url.openStream(),"UTF-8") );   
			String str = null;    
			while((str = in.readLine()) != null) {    
				sb.append( str );     
			}     
		} catch (Exception ex) {   

		} finally{    
			try{             
				if(in!=null) {  
					in.close();     
				}     
			}catch(IOException ex) {      
			}     
		}     
		String result =sb.toString(); 
		return result;    
	}    

	public static String getURLToData(String urlStr,String params,String method, HttpServletRequest request) throws Exception {  
		HttpSession session = request.getSession(); 
		StringBuilder sb = new StringBuilder();   
		byte[] data = params.getBytes("UTF-8");
		URL url=new URL(urlStr);  
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
		conn.setRequestMethod(method);  
		conn.setDoOutput(true);  
		conn.setDoInput(true); 
		conn.setRequestProperty("token", (String) session.getAttribute("token"));
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();        
		BufferedReader in = null;    
		try{     
			in = new BufferedReader( new InputStreamReader(conn.getInputStream(),"UTF-8") );
			String str = null;    
			while((str = in.readLine()) != null) {    
				sb.append( str );     
			}
		} catch (Exception ex) { 
			ex.printStackTrace();
		} finally{    
			try{   
				conn.disconnect();  
				if(in!=null){  
					in.close();  
				}  
				if(outStream!=null){  
					outStream.close();  
				}  
			}catch(IOException ex) {     
				ex.printStackTrace();  
			}     
		}     
		return sb.toString();
	}

	/**  
	 * 请求http服务  
	 * @param urlStr  
	 * @param params   name=yxd&age=25  
	 * @param request 
	 * @return  
	 * @throws Exception  
	 */  
	public static String getURLPost(String urlStr,String params,String method, HttpServletRequest request) throws Exception { 
		StringBuilder sb = new StringBuilder();   
		byte[] data = params.getBytes("UTF-8");
		URL url=new URL(urlStr);  
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
		conn.setRequestMethod(method);  
		conn.setDoOutput(true);  
		conn.setDoInput(true); 
		conn.setRequestProperty("token", (String)request.getAttribute("token"));
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();        
		BufferedReader in = null;    
		try{     
			in = new BufferedReader( new InputStreamReader(conn.getInputStream(),"UTF-8") );
			String str = null;    
			while((str = in.readLine()) != null) {    
				sb.append( str );     
			} 
		} catch (Exception ex) { 
			ex.printStackTrace();
		} finally{    
			try{   
				conn.disconnect();  
				if(in!=null){  
					in.close();  
				}  
				if(outStream!=null){  
					outStream.close();  
				}  
			}catch(IOException ex) {     
				ex.printStackTrace();  
			}     
		}     
		return sb.toString();   
	}
	
	public static String getURLPost2(String urlStr,String params){
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(params);
            out.flush();
            out.close();
             
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
             while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sbf.append(lines);
                }
                reader.close();
                // 断开连接
                connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sbf.toString();
    }
	
	/**
	 * 请求获取图片
	 * @param urlStr
	 * @param request
	 * @return
	 */
	public static BufferedImage downloadImage(String urlStr, HttpServletRequest request){
		/** 网络的url地址 */        
		URL url = null;              
		/** http连接 */    
		HttpURLConnection httpConn = null;  
		BufferedImage image = null;
		try{     
			url = new URL(urlStr);  
			httpConn = (HttpURLConnection) url.openConnection();  
			JPEGImageDecoder decoderFile = JPEGCodec.createJPEGDecoder(httpConn.getInputStream());
			image = decoderFile.decodeAsBufferedImage();
		} catch (Exception ex) {   

		} finally{   
			httpConn.disconnect();
		}       
		return image;
	}
	

	/** 
	 * 下载文件保存到本地 
	 *  
	 * @param path 
	 *            文件保存位置 
	 * @param url 
	 *            文件地址 
	 * @throws IOException 
	 */  
	public static void downloadFile(String path, String url) throws IOException {
		HttpClient client = null;
		try {  
			// 创建HttpClient对象  
			client = new DefaultHttpClient();  
			// 获得HttpGet对象  
			HttpGet httpGet = getHttpGet(url, null, null);  
			// 发送请求获得返回结果  
			HttpResponse response = client.execute(httpGet);  
			// 如果成功  
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
				byte[] result = EntityUtils.toByteArray(response.getEntity());  
				if(path == null || "".equals(path)){
					BufferedOutputStream bw = null;  
					try {  
						// 创建文件对象  
						File f = new File(path);  
						// 创建文件路径  
						if (!f.getParentFile().exists())  
							f.getParentFile().mkdirs();  
						// 写入文件  
						bw = new BufferedOutputStream(new FileOutputStream(path));  
						bw.write(result);  
					} catch (Exception e) {  
						e.printStackTrace(); 
					} finally {  
						try {  
							if (bw != null)  
								bw.close();  
						} catch (Exception e) {  
							e.printStackTrace();
						}  
					}
				}
			}  
			// 如果失败  
			else {
				
			}  
		} catch (ClientProtocolException e) {   
			throw e;  
		} catch (IOException e) {   
			throw e;  
		} finally {  
			try {  
				client.getConnectionManager().shutdown();  
			} catch (Exception e) {   
			}  
		}  
	}
	
	/** 
     * 获得HttpGet对象 
     *  
     * @param url 
     *            请求地址 
     * @param params 
     *            请求参数 
     * @param encode 
     *            编码方式 
     * @return HttpGet对象 
     */  
    private static HttpGet getHttpGet(String url, Map<String, String> params,  
            String encode) {  
        StringBuffer buf = new StringBuffer(url);  
        if (params != null) {  
            // 地址增加?或者&  
            String flag = (url.indexOf('?') == -1) ? "?" : "&";  
            // 添加参数  
            for (String name : params.keySet()) {  
                buf.append(flag);  
                buf.append(name);  
                buf.append("=");  
                try {  
                    String param = params.get(name);  
                    if (param == null) {  
                        param = "";  
                    }  
                    buf.append(URLEncoder.encode(param, encode));  
                } catch (UnsupportedEncodingException e) {  
                    e.printStackTrace();
                }  
                flag = "&";  
            }  
        }  
        HttpGet httpGet = new HttpGet(buf.toString());  
        return httpGet;  
    } 
}
