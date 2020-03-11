package org.daijie.common.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * http请求相关信息获取工具
 *
 * @author daijie
 * @since 2020年02月25日
 */
public class SpringHttpUtil {

    private static Logger logger = LoggerFactory.getLogger(SpringHttpUtil.class);

    /**
     * 获取当前请求会话
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取当前响应会话
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取当前请求会话IP
     * @return String
     */
    public static String getIP(){
        String ip = getRequest().getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = getRequest().getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = getRequest().getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = getRequest().getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }

    /**
     * 获取请求Body转换为json字符串
     *
     * @return String
     */
    public static String getBodyString() {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = getRequest().getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.info("未读取到请求body中数据");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.info("未读取到请求body中数据");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 得到所有的参数的值
     * @return Map
     */
    public static Map<String,Object> handelRequest(){
        Map<String,Object> obj = new HashMap<String,Object>();
        Enumeration<String> coll =  getRequest().getParameterNames();
        while(coll.hasMoreElements()){
            String key = coll.nextElement();
            obj.put(key,  getRequest().getParameter(key)==null?"":getRequest().getParameter(key) );
        }
        if(obj.get("pageSize")!=null || obj.get("pageNumber")!=null){
            int pageSize = Integer.valueOf(obj.get("pageSize").toString());
            int pageNumber = Integer.valueOf(obj.get("pageNumber").toString());
            int startIndex = (pageNumber - 1) * pageSize;
            obj.put("startIndex", startIndex);
            obj.put("pageSize", pageSize);
            obj.put("pageNumber", pageNumber);
        }
        return obj;
    }
}
