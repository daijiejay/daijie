package org.mybatis.generator.plugins;

import org.apache.log4j.Logger;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 针对MySQL的分页插件
 * 
 * <pre>
 * 结果为：
 * 1. 在生成的*.xml文件添加limit
 * 2. 在生成的查询条件类(默认的后缀后*Example.java)添加offset与limitCount字段
 * </pre>
 * 
 * @author Patrick
 *
 */
public class MysqlPaginationPlugin extends PluginAdapterEnhance {

	private Logger log = Logger.getLogger(this.getClass());

	public MysqlPaginationPlugin() {
		log.debug("initialized");
	}

	/**
	 * 在查询条件类中添加offset与limitCount字段
	 */
	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		Field offsetField = new Field("offset", PrimitiveTypeWrapper.getIntegerInstance());
		offsetField.setInitializationString("-1");
		offsetField.setVisibility(JavaVisibility.PROTECTED);
		addField(topLevelClass, introspectedTable, offsetField);

		Field limitCountField = new Field("limitCount", PrimitiveTypeWrapper.getIntegerInstance());
		limitCountField.setInitializationString("-1");
		limitCountField.setVisibility(JavaVisibility.PROTECTED);
		addField(topLevelClass, introspectedTable, limitCountField);

		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	/**
	 * 在xml的SelectByExample的SQL语句添加limit
	 */
	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		element.getElements().add(createPaginationXmlElement());
		return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);
	}

	/**
	 * 在xml的SelectByExampleWithoutBLOBs的SQL语句添加limit
	 */
	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		element.getElements().add(createPaginationXmlElement());
		return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
	}

	/**
	 * 创建limit的xmlElement
	 * 
	 * @return
	 */
	private XmlElement createPaginationXmlElement() {
		XmlElement xmlElement = new XmlElement("if");
		xmlElement.addAttribute(new Attribute("test", "offset >= 0"));
		xmlElement.addElement(new TextElement("limit #{offset} , #{limitCount}"));
		return xmlElement;
	}
}
