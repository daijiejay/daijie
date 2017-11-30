package org.daijie.social.login;

/**
 * 三方登录服务接入
 * @author daijie_jay
 * @date 2017年11月29日
 */
public interface LoginService {

	/**
	 * 加载第三方二维码
	 * @return
	 */
	public String loadQrcode();

	/**
	 * 加载第三方二维码
	 * @return
	 */
	public String loadQrcode(String state);

	/**
	 * 加载第三方授权页
	 * @return
	 */
	public String loadAuthPage();

	/**
	 * 加载第三方授权页
	 * @return
	 */
	public String loadAuthPage(String state);
	
	/**
	 * 获取第三方用户个人信息
	 * @param voucher 凭证
	 * @return
	 */
	public LoginResult getUserInfo(String voucher);
}
