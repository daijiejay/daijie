/*
 *  Copyright 2009 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class UpdateByConditionsSelectiveElementGenerator extends AbstractXmlElementGenerator {

	public UpdateByConditionsSelectiveElementGenerator() {
		super();
	}

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

		answer.addAttribute(new Attribute("id", introspectedTable.getUpdateByConditionsSelectiveStatementId())); //$NON-NLS-1$

		answer.addAttribute(new Attribute("parameterType", "map")); //$NON-NLS-1$ //$NON-NLS-2$

		context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();
		sb.append("update "); //$NON-NLS-1$
		sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		XmlElement dynamicElement = new XmlElement("set"); //$NON-NLS-1$
		answer.addElement(dynamicElement);

		for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append(introspectedColumn.getJavaProperty("record.")); //$NON-NLS-1$
			sb.append(" != null"); //$NON-NLS-1$
			isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
			dynamicElement.addElement(isNotNullElement);

			sb.setLength(0);
			sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record.")); //$NON-NLS-1$
			sb.append(',');

			isNotNullElement.addElement(new TextElement(sb.toString()));
		}

		//answer.addElement(getUpdateByExampleIncludeElement());

//		XmlElement ifElement = new XmlElement("if"); //$NON-NLS-1$
//		ifElement.addAttribute(new Attribute("test", "_parameter != null")); //$NON-NLS-1$ //$NON-NLS-2$
//
////		XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
////		includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
////				introspectedTable.getMyBatis3UpdateByExampleWhereClauseId()));
////		ifElement.addElement(includeElement);
//
//		sb.setLength(0);
//		sb.append("${_parameter}");
//		ifElement.addElement(new TextElement(sb.toString()));
//
//		answer.addElement(ifElement);

		sb.setLength(0);
		sb.append("where 1=1");
		answer.addElement(new TextElement(sb.toString()));

		sb.setLength(0);
		sb.append("${conditions}");
		answer.addElement(new TextElement(sb.toString()));

//		sb.setLength(0);sb.setLength(0);
//		sb.append("${_parameter}");
//		answer.addElement(new TextElement(sb.toString()));
//		sb.append("${_parameter}");
//		answer.addElement(new TextElement(sb.toString()));

		if (context.getPlugins().sqlMapUpdateByExampleSelectiveElementGenerated(answer, introspectedTable)) {
			parentElement.addElement(answer);
		}
	}
}
