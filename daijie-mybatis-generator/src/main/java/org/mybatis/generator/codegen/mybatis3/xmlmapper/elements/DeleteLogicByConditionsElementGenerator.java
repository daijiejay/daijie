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

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class DeleteLogicByConditionsElementGenerator extends AbstractXmlElementGenerator {

	public DeleteLogicByConditionsElementGenerator() {
		super();
	}

	@Override
	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

		answer.addAttribute(new Attribute("id", introspectedTable.getDeleteLogicByConditionsStatementId())); //$NON-NLS-1$
		answer.addAttribute(new Attribute("parameterType", FullyQualifiedJavaType.getStringInstance().toString())); //$NON-NLS-1$
		//answer.addAttribute(new Attribute("resultType", "java.lang.Integer")); //$NON-NLS-1$ //$NON-NLS-2$

		context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();
		sb.append("update "); //$NON-NLS-1$
		sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		sb.setLength(0);
		sb.append("set status=0 ");
		answer.addElement(new TextElement(sb.toString()));

		sb.setLength(0);
		sb.append("where 1=1");
		answer.addElement(new TextElement(sb.toString()));

		sb.setLength(0);
		sb.append("${_parameter} ");
		answer.addElement(new TextElement(sb.toString()));

		if (context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, introspectedTable)) {
			parentElement.addElement(answer);
		}
	}
}
