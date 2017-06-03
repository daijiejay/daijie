package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * PluginAdapter的扩展类
 * 
 * <pre>
 * 主要包括了:
 * -获取系统分割符
 * -添加字段同时也添加get,set方法
 * </pre>
 * 
 * @author Patrick
 *
 */
public abstract class PluginAdapterEnhance extends PluginAdapter {

	/**
	 * 取消验证
	 */
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	/**
	 * 获取系统分隔符
	 * 
	 * @return
	 */
	protected String getSeparator() {
		return System.getProperty("line.separator");
	}

	/**
	 * 添加字段，同时也添加get,set方法
	 * 
	 * @param topLevelClass
	 * @param introspectedTable
	 * @param field
	 */
	protected void addField(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, Field field) {

		CommentGenerator commentGenerator = context.getCommentGenerator();

		// 添加Java字段
		commentGenerator.addFieldComment(field, introspectedTable);
		topLevelClass.addField(field);

		String fieldName = field.getName();

		// 生成Set方法
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName(generateSetMethodName(fieldName));
		method.addParameter(new Parameter(field.getType(), fieldName));
		method.addBodyLine("this." + fieldName + "=" + fieldName + ";");
		commentGenerator.addGeneralMethodComment(method, introspectedTable);
		topLevelClass.addMethod(method);

		// 生成Get方法
		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(field.getType());
		method.setName(generateGetMethodName(fieldName));
		method.addBodyLine("return " + fieldName + ";");
		commentGenerator.addGeneralMethodComment(method, introspectedTable);
		topLevelClass.addMethod(method);
	}

	protected static String generateGetMethodName(String fieldName) {
		return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}

	protected static String generateSetMethodName(String fieldName) {
		return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}

}
