package org.daijie.core.kisso;

/**
 * http由kisso管理的安全控制
 * @author daijie_jay
 * @since 2018年7月3日
 */
public interface KissoSecurity {

	/**
	 * 是否开启Kisso管理cookie
	 * @return boolean
	 */
	public boolean isKissoEnable();

	/**
	 * 获取cookie名称
	 * @return String
	 */
	public String getCookieName();
}
