package org.daijie.social.login.baidu;

import org.daijie.social.login.LoginProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 百度登录属性配置初始化
 * @author daijie_jay
 * @date 2017年11月28日
 */
@ConfigurationProperties("baidu.login")
public class BaiduLoignProperties extends LoginProperties {
	
}
