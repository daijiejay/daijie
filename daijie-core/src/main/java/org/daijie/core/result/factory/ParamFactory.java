package org.daijie.core.result.factory;

import java.lang.reflect.Field;

import org.daijie.core.factory.Factory;

import com.xiaoleilu.hutool.bean.BeanUtil;

public abstract class ParamFactory implements Factory {

	public static <T> T newInstance(Class<T> className) {
		try {
			return className.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> T newInstance(Class<T> className, Object source) {
		try {
			T newObj= className.newInstance();
			BeanUtil.copyProperties(source, newObj);
			return newObj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> T newInstance(Class<T> className, Object... sources) {
		try {
			Field[] fields = className.getDeclaredFields();
			if (sources.length > fields.length) {
				return null;
			}
			T newObj= className.newInstance();
			for (int i = 0; i < sources.length; i++) {
				if (fields[i].getType().equals(sources[i].getClass())) {
					fields[i].setAccessible(true);
					fields[i].set(newObj, sources[i]);
				}
			}
			return newObj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
