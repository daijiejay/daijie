package org.mybatis.generator.plugins;

import org.apache.log4j.Logger;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 持久层添加Spring的@Repository注解
 * 
 * @author Patrick
 *
 */
public class PersistenceRepositoryAnnotationPlugin extends PluginAdapterEnhance {

	private Logger log = Logger.getLogger(this.getClass());

	/**
	 * Spring的@Repository注解
	 */
	private final String REPOSITORY_ANNOTATION = "@Repository";

	/**
	 * Spring的@Repository注解的包路径
	 */
	private final String REPOSITORY_PACKAGE = "org.springframework.stereotype.Repository";

	public PersistenceRepositoryAnnotationPlugin() {
		log.debug("initialized");
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		interfaze.addAnnotation(REPOSITORY_ANNOTATION);
		interfaze.addImportedType(new FullyQualifiedJavaType(REPOSITORY_PACKAGE));
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}
}
