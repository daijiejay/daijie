package org.daijie.social.captcha.tx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Http 请求工具类
 */
public class ApiRequest {
    /**
     * Get 请求
     *
     * @param url    http请求地址
     * @param params http请求参数
     * @return String
     */
    public static ApiResponse sendGet(String url, String params) {
        ApiResponse response = new ApiResponse();
        try {
            String urlNameString = url;
            if(0 != params.length()) {
                String linkOperator = urlNameString.contains("?") ? "&" : "?";
                urlNameString += linkOperator + params;
            }

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection)new URL(urlNameString).openConnection();

            // 设置请求头
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.connect();

            // 获取请求头字段
            Map<String, List<String>> header = connection.getHeaderFields();

            // 获取URL的输入流，读取请求响应
            String body = readString(connection.getInputStream());
            response.setHeader(header);
            response.setBody(body);

        } catch (Exception e) {
            System.out.println("发送GET请求出现异常!" + e);
            e.printStackTrace();
        }

        return response;
    }

    /**
     * POST 请求
     *
     * @param url    http请求地址
     * @param params http请求参数
     * @return String
     */
    public static ApiResponse sendPost(String url, String params) {
        PrintWriter pw = null;
        ApiResponse response = new ApiResponse();
        try {
            HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();

            // 设置请求头
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 设置 POST
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // Post 请求不能使用缓存
            connection.setUseCaches(false);

            connection.setConnectTimeout(5000);

            // 获取URL的输出流, 发送请求参数
            pw = new PrintWriter(connection.getOutputStream());
            pw.print(params);
            pw.flush();

            // 获取请求头字段
            Map<String, List<String>> header = connection.getHeaderFields();

            // 获取URL的输入流，读取请求响应
            String body = readString(connection.getInputStream());

            response.setHeader(header);
            response.setBody(body);

        } catch (Exception e) {
            System.out.println("发送POST请求出现异常!" + e);
            e.printStackTrace();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }


    private static String readString(InputStream is) {
        BufferedReader br = null;
        String content = "";
        try {
            br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                content += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }
}

