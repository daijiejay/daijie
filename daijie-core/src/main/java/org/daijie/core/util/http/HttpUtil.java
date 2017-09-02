package org.daijie.core.util.http;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @author daijie
 * @date 2017年6月5日
 * http请求接口工具类
 * 
 */
@SuppressWarnings({ "deprecation", "resource" })
public class HttpUtil {

	private static final String POST = "POST";
	
	private static final String GET = "GET";
	
	private static final String PUT = "PUT";
	
	private static final String DELETE = "DELETE";
	
	private static final String TYPE_STRING = "string";

	private static final String TYPE_BYTEARRAY = "byte[]";

	/**
	 * 请求http服务 只支持GET请求 
	 * @param urlStr 请求路径
	 * @return
	 */
	public static String requestURLGet(String urlStr) {               
		URL url = null;                        
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

	/**  
	 * 请求http服务  
	 * @param urlStr 请求路径
	 * @param params 请求参数  json字符串
	 * @param method 请求方法
	 * @return  
	 * @throws Exception  
	 */  
	public static String requestURL(String urlStr,String params,String method) throws Exception { 
		StringBuilder sb = new StringBuilder();   
		byte[] data = params.getBytes("UTF-8");
		URL url=new URL(urlStr);  
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
		conn.setRequestMethod(method);  
		conn.setDoOutput(true);  
		conn.setDoInput(true); 
		conn.setRequestProperty(HttpConversationUtil.TOKEN_NAME, HttpConversationUtil.getToken());
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
	 * 用于get、post、put、delete请求，请求、接收参数都为对象
	 * @param urlStr 请求RUL
	 * @param params 请求参数对象
	 * @param responseType 返回数据类型
	 * @param beanClass 返回数据对象
	 * @return
	 * @throws Exception
	 */
	public static Object requestMethod(String method, String urlStr, Object params, String responseType, Class<?> resultBeanClass) throws Exception{
		if(HttpUtil.POST.equals(method.toUpperCase())){
			return requestPost(urlStr, params, responseType, resultBeanClass);
		} else if(HttpUtil.GET.equals(method.toUpperCase())){
			return requestGet(urlStr, params, responseType, resultBeanClass);
		} else if(HttpUtil.PUT.equals(method.toUpperCase())){
			return requestPut(urlStr, params, responseType, resultBeanClass);
		} else if(HttpUtil.DELETE.equals(method.toUpperCase())){
			return requestDelete(urlStr, params, responseType, resultBeanClass);
		} else {
			throw new HttpException("mothod is error!");
		}
	}
	
	/**
	 * 用于post请求，请求、接收参数都为对象
	 * @param urlStr 请求RUL
	 * @param params 请求参数对象
	 * @param responseType 返回数据类型
	 * @param beanClass 返回数据对象
	 * @return
	 * @throws Exception
	 */
	private static Object requestPost(String urlStr, Object params, String responseType, Class<?> resultBeanClass) throws Exception {
		PostMethod method = new PostMethod(urlStr);
		method.addParameters(getNameValuePair(params));
		return requestExecute(method, responseType, resultBeanClass);
	}

	/**
	 * 用于get请求，请求、接收参数都为对象
	 * @param urlStr 请求RUL
	 * @param params 请求参数对象
	 * @param responseType 返回数据类型
	 * @param beanClass 返回数据对象
	 * @return
	 * @throws Exception
	 */
	private static Object requestGet(String urlStr, Object params, String responseType, Class<?> resultBeanClass) throws Exception {
		GetMethod method = new GetMethod(getHttpParams(urlStr, params, "UTF-8"));
		return requestExecute(method, responseType, resultBeanClass);
	}
	
	/**
	 * 用于put请求，请求、接收参数都为对象
	 * @param urlStr 请求RUL
	 * @param params 请求参数对象
	 * @param responseType 返回数据类型
	 * @param beanClass 返回数据对象
	 * @return
	 * @throws Exception
	 */
	private static Object requestPut(String urlStr, Object params, String responseType, Class<?> resultBeanClass) throws Exception {
		PutMethod method = new PutMethod(getHttpParams(urlStr, params, "UTF-8"));
		return requestExecute(method, responseType, resultBeanClass);
	}
	
	/**
	 * 用于delete请求，请求、接收参数都为对象
	 * @param urlStr 请求RUL
	 * @param params 请求参数对象
	 * @param responseType 返回数据类型
	 * @param beanClass 返回数据对象
	 * @return
	 * @throws Exception
	 */
	private static Object requestDelete(String urlStr, Object params, String responseType, Class<?> resultBeanClass) throws Exception {
		DeleteMethod method = new DeleteMethod(getHttpParams(urlStr, params, "UTF-8"));
		return requestExecute(method, responseType, resultBeanClass);
	}
	
	/**
	 * http请求
	 * @param method 请求方法
	 * @param responseType 返回数据类型
	 * @param resultBeanClass 返回数据对象
	 * @return
	 */
	private static Object requestExecute(HttpMethod method, String responseType, Class<?> resultBeanClass){
		org.apache.commons.httpclient.HttpClient client = null;
		SimpleHttpConnectionManager connectionManager = null;
		Object responseResult = null;
		try {
			responseResult = resultBeanClass.newInstance();
			connectionManager = new SimpleHttpConnectionManager(true);
			// 连接超时,单位毫秒
			connectionManager.getParams().setConnectionTimeout(3000);
			// 读取超时,单位毫秒
			connectionManager.getParams().setSoTimeout(60000);
			// 设置获取内容编码
			connectionManager.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			client = new org.apache.commons.httpclient.HttpClient(new HttpClientParams(), connectionManager);
			// 设置请求参数的编码
			method.getParams().setContentCharset( "UTF-8");
			// 服务端完成返回后，主动关闭链接
			method.setRequestHeader("Connection", "close");
			int sendStatus = client.executeMethod(method);
			if (sendStatus == HttpStatus.SC_OK) {
				if (StringUtils.equals(TYPE_STRING, responseType)) {
					responseResult = method.getResponseBodyAsString();
				} else if (StringUtils.equals(TYPE_BYTEARRAY, responseType)) {
					responseResult = method.getResponseBody();
				} 
			} else {
				responseResult = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
			if (connectionManager != null) {
				connectionManager.shutdown();
			}
		}
		return responseResult;
	}
	
	/**
	 * 请求获取图片， 只支持get请求
	 * @param urlStr
	 * @param request
	 * @return
	 */
	public static BufferedImage downloadImage(String urlStr){
		URL url = null;              
		HttpURLConnection httpConn = null;  
		BufferedImage image = null;
		try{     
			url = new URL(urlStr);  
			httpConn = (HttpURLConnection) url.openConnection();
			InputStream inputStream = httpConn.getInputStream();
			image = ImageIO.read(inputStream);	
		} catch (Exception ex) {   

		} finally{
			if(httpConn != null){
				httpConn.disconnect();
			}
		}       
		return image;
	}

	/** 
	 * 下载文件保存到本地 
	 * @param path文件保存位置 
	 * @param url 文件地址 
	 * @throws IOException 
	 */  
	public static void downloadFile(String path, String url) throws IOException {
		HttpClient client = null;
		try {  
			// 创建HttpClient对象  
			client = new DefaultHttpClient();  
			// 获得HttpGet对象  
			HttpGet httpGet = new HttpGet(getHttpParams(url, null, null));  
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
			} else {
				
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
     * @param url 请求地址 
     * @param params 请求参数 
     * @param encode 编码方式 
     * @return HttpGet对象 
     */  
    @SuppressWarnings("unchecked")
	private static String getHttpParams(String url, Object params,  
            String encode) {  
        StringBuffer buf = new StringBuffer(url);  
        if (params != null) {  
			if(!(params instanceof Map)){
				params = new BeanMap(params);
			}
            // 地址增加?或者&  
            String flag = (url.indexOf('?') == -1) ? "?" : "&";  
            // 添加参数  
            for (String name : ((Map<String, Object>)params).keySet()) {  
                buf.append(flag);  
                buf.append(name);  
                buf.append("=");  
                try {  
                    String param = ((Map<String, Object>)params).get(name).toString();  
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
        return buf.toString();  
    } 
	
    /**
     * 将请求参数对象转换为param数组
     * @param params 请求参数对象
     * @return
     */
	@SuppressWarnings("unchecked")
	private static NameValuePair[] getNameValuePair(Object params) {
		NameValuePair[] nameValuePairArr = null;
		if (params != null) {
			if(!(params instanceof Map)){
				params = new BeanMap(params);
			}
			nameValuePairArr = new NameValuePair[((Map<String, Object>)params).size()];
			Set<String> key = ((Map<String, Object>)params).keySet();
			Iterator<String> keyIte = key.iterator();
			int index = 0;
			while (keyIte.hasNext()) {
				Object paramName = keyIte.next();
				Object paramValue = ((Map<String, Object>)params).get(paramName);
				if (paramName instanceof String && paramValue instanceof String) {
					NameValuePair pair = new NameValuePair((String) paramName, (String) paramValue);
					nameValuePairArr[index] = pair;
					index++;
				}
			}
		}
		return nameValuePairArr;
	}
}
