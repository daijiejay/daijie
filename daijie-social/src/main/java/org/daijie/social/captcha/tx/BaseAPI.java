package org.daijie.social.captcha.tx;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

// JDK 1.7及以下使用第三方Jar包, JDK 1.8 可以使用JDK集成的 java.util.Base64类 进行编码
// import java.util.Base64;

/**
 * URL 生成器
 */
public class BaseAPI {

    protected String URL = "csec.api.qcloud.com/v2/index.php";
    protected String secretId = "AKIDBHBsRqnA3kFdtzpfkfNuGSyNZcdgHrqh";
    protected String secretKey = "rZ4M4d0BBMQL4cODsMo9KQLIFEC4lGZf";

    /**
     * Base64 编码
     *
     * @param bstr
     * @return String
     */
    private String encode(byte[] bstr) {
        String s = System.getProperty("line.separator");
        //return Base64.getEncoder().encodeToString(bstr).replaceAll(s, "");    // JDK 1.8 可使用
        return Base64.encodeBase64String(bstr).replaceAll(s, "");
    }

    /**
     * 签名算法
     * 使用 HMAC-SHA1 算法
     *
     * @param key  密钥
     * @param text 内容
     * @return String
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private String hmacSHA1(String key, String text) throws InvalidKeyException, NoSuchAlgorithmException {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA1"));
        return this.encode(mac.doFinal(text.getBytes()));
    }

    /**
     * 拼接查询字符串
     *
     * @param args    参数
     * @param charset 字符编码
     * @return String
     * @throws UnsupportedEncodingException
     */
    private String makeQueryString(Map<String, String> args, String charset) throws UnsupportedEncodingException {
        String url = "";

        for (Map.Entry<String, String> entry : args.entrySet())
            url += entry.getKey() + "=" + (charset == null ? entry.getValue() : URLEncoder.encode(entry.getValue(), charset)) + "&";

        return url.substring(0, url.length() - 1);
    }

    /**
     * 构造请求url
     *
     * @param method    http请求方法 GET | POST
     * @param action    接口名称
     * @param region    实例所在区域 sz | gz ...
     * @param args      请求参数
     * @param charset   字符编码
     * @return http请求字符串
     * @throws InvalidKeyException 抛出异常
     * @throws NoSuchAlgorithmException 抛出异常
     * @throws UnsupportedEncodingException 抛出异常
     */
    public String makeURL(
            String method,
            String action,
            String region,
            Map<String, String> args,
            String charset)
            throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Map<String, String> arguments = new TreeMap<String, String>();

        /* Sort all parameters, then calculate signature */
        arguments.putAll(args);
        arguments.put("Nonce", String.valueOf((int)(Math.random() * 0x7fffffff)));
        arguments.put("Action", action);
        arguments.put("Region", region);
        arguments.put("SecretId", this.secretId);
        arguments.put("Timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        arguments.put("Signature", hmacSHA1(this.secretKey, String.format("%s%s?%s", method, this.URL, makeQueryString(arguments, null))));

        /* Assemble final request URL */
        return String.format("https://%s?%s", this.URL, makeQueryString(arguments, charset));
    }
}