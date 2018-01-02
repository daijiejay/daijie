package org.daijie.social.login;

/**
 * 三方登录服务接入
 * @author daijie_jay
 * @since 2017年11月29日
 */
public interface LoginService {
	
	public <T extends LoginProperties> T getProperties();

	/**
	 * 加载第三方二维码
	 * @return String
	 */
	public String loadQrcode();

	/**
	 * 加载第三方二维码
	 * @param state 数据声明
	 * @return String
	 */
	public String loadQrcode(String state);

	/**
	 * 加载第三方授权页
	 * @return String
	 */
	public String loadAuthPage();

	/**
	 * 加载第三方授权页
	 * @param state 数据声明
	 * @return String
	 */
	public String loadAuthPage(String state);
	
	/**
	 * 获取第三方用户个人信息
	 * @param voucher 凭证
	 * @return LoginResult
	 */
	public LoginResult getUserInfo(String voucher);
}
