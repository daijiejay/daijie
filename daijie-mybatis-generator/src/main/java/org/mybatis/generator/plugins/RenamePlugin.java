package org.mybatis.generator.plugins;

import java.util.List;

import org.apache.log4j.Logger;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * 重命名插件
 * 
 * <pre>
 * 例子为：
 * 1.mybatis中的setCountByExample修改为setCountByCriteria，其中Criteria可定制
 * 2.持久层的UserMapper修改为UserDao，其中Dao可定制
 * 3.查询条件对象UserExample修改UserCriteria，其中Criteria可定制
 * </pre>
 * 
 * @author Patrick
 *
 */
public class RenamePlugin extends PluginAdapterEnhance {

	private Logger log = Logger.getLogger(this.getClass());

	// mybatis中的部分方法的后缀
	private String mybatisMethodSuffix;
	// 持久层对象的后缀
	private String persistenceObjectSuffix;
	// mybaits查询对象的后缀
	private String queryObjectSuffix;

	// mybatis中的部分方法的默认后缀
	private final String mybatisMethodDefaultSuffix = "Example";
	// 持久层对象的默认后缀
	private final String persistenceObjectDefaultSuffix = "Mapper";
	// mybaits查询对象的默认后缀
	private String queryObjectDefaultSuffix = "Example";

	public RenamePlugin() {
		log.debug("initialized");
	}

	@Override
	public boolean validate(List<String> warnings) {
		if (super.validate(warnings) == false)
			return false;

		mybatisMethodSuffix = properties.getProperty("mybatisMethodSuffix");
		persistenceObjectSuffix = properties.getProperty("persistenceObjectSuffix");
		queryObjectSuffix = properties.getProperty("queryObjectSuffix");

		log.info("mybatisMethodSuffix = " + mybatisMethodSuffix);
		log.info("persistenceObjectSuffix = " + persistenceObjectSuffix);
		log.info("queryObjectSuffix = " + queryObjectSuffix);

		return true;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		if (StringUtility.stringHasValue(mybatisMethodSuffix)) {
			introspectedTable.setCountByExampleStatementId(introspectedTable.getCountByExampleStatementId().replace(mybatisMethodDefaultSuffix, mybatisMethodSuffix));
			introspectedTable.setDeleteByExampleStatementId(introspectedTable.getDeleteByExampleStatementId().replace(mybatisMethodDefaultSuffix, mybatisMethodSuffix));
			introspectedTable.setSelectByExampleStatementId(introspectedTable.getSelectByExampleStatementId().replace(mybatisMethodDefaultSuffix, mybatisMethodSuffix));
			introspectedTable.setSelectByExampleWithBLOBsStatementId(introspectedTable.getSelectByExampleWithBLOBsStatementId().replace(mybatisMethodDefaultSuffix, mybatisMethodSuffix));
			introspectedTable.setUpdateByExampleStatementId(introspectedTable.getUpdateByExampleStatementId().replace(mybatisMethodDefaultSuffix, mybatisMethodSuffix));
			introspectedTable.setUpdateByExampleSelectiveStatementId(introspectedTable.getUpdateByExampleSelectiveStatementId().replace(mybatisMethodDefaultSuffix, mybatisMethodSuffix));
			introspectedTable.setUpdateByExampleWithBLOBsStatementId(introspectedTable.getUpdateByExampleWithBLOBsStatementId().replace(mybatisMethodDefaultSuffix, mybatisMethodSuffix));
			introspectedTable.setExampleWhereClauseId(introspectedTable.getExampleWhereClauseId().replace(mybatisMethodDefaultSuffix, mybatisMethodSuffix));
			introspectedTable.setMyBatis3UpdateByExampleWhereClauseId(introspectedTable.getMyBatis3UpdateByExampleWhereClauseId().replace(mybatisMethodDefaultSuffix, mybatisMethodSuffix));
		}

		if (StringUtility.stringHasValue(persistenceObjectSuffix)) {
			String myBatis3JavaMapperType = introspectedTable.getMyBatis3JavaMapperType();
			introspectedTable.setMyBatis3JavaMapperType(myBatis3JavaMapperType.replace(persistenceObjectDefaultSuffix, persistenceObjectSuffix));
		}

		if (StringUtility.stringHasValue(queryObjectSuffix)) {
			String exampleType = introspectedTable.getExampleType();
			introspectedTable.setExampleType(exampleType.replace(queryObjectDefaultSuffix, queryObjectSuffix));
		}

		super.initialized(introspectedTable);
	}

}
