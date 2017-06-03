package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 给dao增加注解增加注解
 * 
 * @author QQ:34847009
 * @date 2010-10-21 下午09:33:48
 */
public class AnnotationPlugin extends PluginAdapter {
	private FullyQualifiedJavaType repository;
	private FullyQualifiedJavaType autowired;
	private FullyQualifiedJavaType sqlMapClient;

	public AnnotationPlugin() {
		super();
		autowired = new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"); //$NON-NLS-1$
		repository = new FullyQualifiedJavaType("org.springframework.stereotype.Repository"); //$NON-NLS-1$
		sqlMapClient = new FullyQualifiedJavaType("com.ibatis.sqlmap.client.SqlMapClient"); //$NON-NLS-1$
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		Method method = topLevelClass.getMethods().get(0);
		addAnnotation(topLevelClass, method);
		method.addParameter(new Parameter(sqlMapClient, "sqlMapClient"));
		method.removeBodyLine(0);
		method.addBodyLine("super.setSqlMapClient(sqlMapClient);");
		return true;
	}

	/**
	 * 添加注解
	 * 
	 * @param topLevelClass
	 * @param method
	 */
	protected void addAnnotation(TopLevelClass topLevelClass, Method method) {
		topLevelClass.addImportedType(sqlMapClient);
		topLevelClass.addImportedType(autowired);
		topLevelClass.addImportedType(repository);
		method.addAnnotation("@Autowired");
		topLevelClass.addAnnotation("@Repository");
	}

}
