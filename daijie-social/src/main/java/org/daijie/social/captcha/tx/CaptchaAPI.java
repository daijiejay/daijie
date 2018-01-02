package org.daijie.social.captcha.tx;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * 腾迅验证码接口封装
 * @author daijie_jay
 * @since 2017年12月4日
 */
public class CaptchaAPI extends BaseAPI {

    private volatile static CaptchaAPI instance = null;

    private CaptchaAPI() {
        super();
    }
    
    private CaptchaAPI(String url, String id, String key) {
    	super();
    	this.URL = url;
    	this.secretId = id;
    	this.secretKey = key;
    }

    public static CaptchaAPI getInstance() {
        if (instance==null) {
            synchronized (CaptchaAPI.class) {
                if (instance == null) {
                    instance = new CaptchaAPI();
                }
            }
        }
        return instance;
    }
    
    public static CaptchaAPI getInstance(String url, String id, String key) {
    	if (instance==null) {
    		synchronized (CaptchaAPI.class) {
    			if (instance == null) {
    				instance = new CaptchaAPI(url, id, key);
    			}
    		}
    	}
    	return instance;
    }

    /**
     * 验证验证码票据
     * IFrame接入请使用该方法校验用户输入的验证码
     * @param args 用户输入票据
     * @return ApiResponse
     * @throws InvalidKeyException 抛出异常
     * @throws NoSuchAlgorithmException 抛出异常
     * @throws UnsupportedEncodingException 抛出异常
     */
    public ApiResponse check(Map<String, String> args) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String url = this.makeURL("GET", "CaptchaCheck", "gz", args, "utf-8");
        return ApiRequest.sendGet(url, "");
    }

    /**
     * 获取JS Url
     * IFrame接入请使用该方法拉取js url
     * @param args 用户输入票据
     * @return ApiResponse
     * @throws InvalidKeyException 抛出异常
     * @throws NoSuchAlgorithmException 抛出异常
     * @throws UnsupportedEncodingException 抛出异常
     */
    public ApiResponse getJsUrl(Map<String, String> args) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException  {
        String url = this.makeURL("GET", "CaptchaIframeQuery", "gz", args, "utf-8");
//        ApiResponse resp = ApiRequest.sendGet(url, "");
//        $jsUrl = resp.getBody();
        return ApiRequest.sendGet(url, "");
    }
}
