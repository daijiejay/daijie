package org.daijie.core.kisso;

import org.daijie.core.factory.InitialFactory;

/**
 * kisso权限工厂
 * @author daijie_jay
 * @date 2017年11月17日
 */
public interface KissoSecurityFactory extends InitialFactory {
	
	public void setKissoEnable(Boolean kissoEnable);
}
