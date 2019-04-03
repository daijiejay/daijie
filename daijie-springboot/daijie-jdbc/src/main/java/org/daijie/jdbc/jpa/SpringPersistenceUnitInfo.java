package org.daijie.jdbc.jpa;

import javax.persistence.spi.ClassTransformer;

import org.springframework.core.DecoratingClassLoader;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.instrument.classloading.SimpleThrowawayClassLoader;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.util.Assert;

/**
 * 此类为同名重写，私有类改为公共类
 * @author daijie_jay
 * @since 2018年5月28日
 */
public class SpringPersistenceUnitInfo extends MutablePersistenceUnitInfo {

	@Nullable
	private LoadTimeWeaver loadTimeWeaver;

	@Nullable
	private ClassLoader classLoader;

	public void init(LoadTimeWeaver loadTimeWeaver) {
		Assert.notNull(loadTimeWeaver, "LoadTimeWeaver must not be null");
		this.loadTimeWeaver = loadTimeWeaver;
		this.classLoader = loadTimeWeaver.getInstrumentableClassLoader();
	}

	public void init(@Nullable ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	@Nullable
	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

	@Override
	public void addTransformer(ClassTransformer classTransformer) {
		if (this.loadTimeWeaver == null) {
			throw new IllegalStateException("Cannot apply class transformer without LoadTimeWeaver specified");
		}
		this.loadTimeWeaver.addTransformer(new ClassFileTransformerAdapter(classTransformer));
	}

	@Override
	public ClassLoader getNewTempClassLoader() {
		ClassLoader tcl = (this.loadTimeWeaver != null ? this.loadTimeWeaver.getThrowawayClassLoader() :
				new SimpleThrowawayClassLoader(this.classLoader));
		String packageToExclude = getPersistenceProviderPackageName();
		if (packageToExclude != null && tcl instanceof DecoratingClassLoader) {
			((DecoratingClassLoader) tcl).excludePackage(packageToExclude);
		}
		return tcl;
	}

}
