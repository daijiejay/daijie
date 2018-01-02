package org.daijie.core.kisso;

import org.daijie.core.factory.InitialFactory;

/**
 * kisso权限工厂
 * @author daijie_jay
 * @since 2017年11月17日
 */
public interface KissoSecurityFactory extends InitialFactory {
	
	/**
	 * 设置是否开启Kisso管理cookie
	 * @param kissoEnable 是否开启Kisso管理cookie
	 */
	public void setKissoEnable(Boolean kissoEnable);
}
