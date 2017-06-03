package org.mybatis.generator.plugins;

import org.apache.log4j.Logger;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 领域模型层添加序列化字段
 * 
 * @author Patrick
 *
 */
public class ModelSerializableFieldPlugin extends PluginAdapterEnhance {

	private Logger log = Logger.getLogger(this.getClass());
	
	public ModelSerializableFieldPlugin() {
		log.debug("initialized");
	}
	
	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		Field serializableField = new Field();
		serializableField.setVisibility(JavaVisibility.PRIVATE);
		serializableField.setFinal(true);
		serializableField.setStatic(true);
		serializableField.setType(new FullyQualifiedJavaType("long"));
		serializableField.setName("serialVersionUID");
		serializableField.setInitializationString("1L");

		context.getCommentGenerator().addFieldComment(serializableField, introspectedTable);

		topLevelClass.addField(serializableField);
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}
}
