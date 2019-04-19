package org.daijie.jdbc.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.persistence.spi.ClassTransformer;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * 此类为同名重写，私有类改为公共类
 * @author daijie_jay
 * @since 2018年5月28日
 */
public class ClassFileTransformerAdapter implements ClassFileTransformer {

	private static final Log logger = LogFactory.getLog(ClassFileTransformerAdapter.class);


	private final ClassTransformer classTransformer;

	private boolean currentlyTransforming = false;


	public ClassFileTransformerAdapter(ClassTransformer classTransformer) {
		Assert.notNull(classTransformer, "ClassTransformer must not be null");
		this.classTransformer = classTransformer;
	}


	@Override
	@Nullable
	public byte[] transform(
			ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) {
		synchronized (this) {
			if (this.currentlyTransforming) {
				return null;
			}
			this.currentlyTransforming = true;
			try {
				byte[] transformed = this.classTransformer.transform(
						loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
				if (transformed != null && logger.isDebugEnabled()) {
					logger.debug("Transformer of class [" + this.classTransformer.getClass().getName() +
							"] transformed class [" + className + "]; bytes in=" +
							classfileBuffer.length + "; bytes out=" + transformed.length);
				}
				return transformed;
			} catch (ClassCircularityError ex) {
				if (logger.isErrorEnabled()) {
					logger.error("Circularity error while weaving class [" + className + "] with " +
							"transformer of class [" + this.classTransformer.getClass().getName() + "]", ex);
				}
				throw new IllegalStateException("Failed to weave class [" + className + "]", ex);
			} catch (Throwable ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Error weaving class [" + className + "] with transformer of class [" +
							this.classTransformer.getClass().getName() + "]", ex);
				}
				throw new IllegalStateException("Could not weave class [" + className + "]", ex);
			} finally {
				this.currentlyTransforming = false;
			}
		}
	}


	@Override
	public String toString() {
		return "Standard ClassFileTransformer wrapping JPA transformer: " + this.classTransformer;
	}

}
