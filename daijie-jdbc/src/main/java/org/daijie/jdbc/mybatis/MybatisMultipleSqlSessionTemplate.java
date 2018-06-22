package org.daijie.jdbc.mybatis;

import static java.lang.reflect.Proxy.newProxyInstance;
import static org.apache.ibatis.reflection.ExceptionUtil.unwrapThrowable;
import static org.mybatis.spring.SqlSessionUtils.closeSqlSession;
import static org.mybatis.spring.SqlSessionUtils.getSqlSession;
import static org.mybatis.spring.SqlSessionUtils.isSqlSessionTransactional;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.daijie.jdbc.DbContextHolder;
import org.daijie.jdbc.annotation.SelectDataSource;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.PersistenceExceptionTranslator;

/**
 * 重写SqlSessionTemplate，实现多个数据源动态切换
 * @author daijie_jay
 * @since 2018年5月15日
 */
@SuppressWarnings("rawtypes") 
public class MybatisMultipleSqlSessionTemplate extends SqlSessionTemplate {
	 
	private final SqlSessionFactory sqlSessionFactory;
	
    private final ExecutorType executorType;
    
    private final SqlSession sqlSessionProxy;
    
    private final PersistenceExceptionTranslator exceptionTranslator;

    private Map<Object, SqlSessionFactory> targetSqlSessionFactorys;
    
    private SqlSessionFactory defaultTargetSqlSessionFactory;

    public void setTargetSqlSessionFactorys(Map<Object, SqlSessionFactory> targetSqlSessionFactorys) {
        this.targetSqlSessionFactorys = targetSqlSessionFactorys;
    }

    public void setDefaultTargetSqlSessionFactory(SqlSessionFactory defaultTargetSqlSessionFactory) {
        this.defaultTargetSqlSessionFactory = defaultTargetSqlSessionFactory;
    }

	public SqlSessionFactory getDefaultTargetSqlSessionFactory() {
		return this.defaultTargetSqlSessionFactory;
	}

	public MybatisMultipleSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        this(sqlSessionFactory, sqlSessionFactory.getConfiguration().getDefaultExecutorType());
    }

    public MybatisMultipleSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
        this(sqlSessionFactory, executorType, new MyBatisExceptionTranslator(sqlSessionFactory.getConfiguration()
                .getEnvironment().getDataSource(), true));
    }

    public MybatisMultipleSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType,
            PersistenceExceptionTranslator exceptionTranslator) {
        super(sqlSessionFactory, executorType, exceptionTranslator);
        this.sqlSessionFactory = sqlSessionFactory;
        this.executorType = executorType;
        this.exceptionTranslator = exceptionTranslator;
        this.sqlSessionProxy = (SqlSession) newProxyInstance(
                SqlSessionFactory.class.getClassLoader(),
                new Class[] { SqlSession.class }, 
                new SqlSessionInterceptor());
        this.defaultTargetSqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public SqlSessionFactory getSqlSessionFactory() {
        return this.sqlSessionFactory;
    }

    public List<Configuration> getAllConfigurations() {
        List<Configuration> list = new ArrayList<Configuration>();
        for(Map.Entry<Object, SqlSessionFactory> entry : targetSqlSessionFactorys.entrySet()) {
            SqlSessionFactory sqlSessionFactory = entry.getValue();
            list.add(sqlSessionFactory.getConfiguration());
        }
        return list;
    }

    @Override
    public Configuration getConfiguration() {
        return this.getSqlSessionFactory().getConfiguration();
    }

    @Override
    public ExecutorType getExecutorType() {
        return this.executorType;
    }

    @Override
    public PersistenceExceptionTranslator getPersistenceExceptionTranslator() {
        return this.exceptionTranslator;
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.sqlSessionProxy.<T> selectOne(statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return this.sqlSessionProxy.<T> selectOne(statement, parameter);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return this.sqlSessionProxy.<K, V> selectMap(statement, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return this.sqlSessionProxy.<K, V> selectMap(statement, parameter, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return this.sqlSessionProxy.<K, V> selectMap(statement, parameter, mapKey, rowBounds);
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return this.sqlSessionProxy.<E> selectList(statement);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return this.sqlSessionProxy.<E> selectList(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return this.sqlSessionProxy.<E> selectList(statement, parameter, rowBounds);
    }

    @Override
    public void select(String statement, ResultHandler handler) {
        this.sqlSessionProxy.select(statement, handler);
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {
        this.sqlSessionProxy.select(statement, parameter, handler);
    }

    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
    }

    @Override
    public int insert(String statement) {
        return this.sqlSessionProxy.insert(statement);
    }

    @Override
    public int insert(String statement, Object parameter) {
        return this.sqlSessionProxy.insert(statement, parameter);
    }

    @Override
    public int update(String statement) {
        return this.sqlSessionProxy.update(statement);
    }

    @Override
    public int update(String statement, Object parameter) {
        return this.sqlSessionProxy.update(statement, parameter);
    }

    @Override
    public int delete(String statement) {
        return this.sqlSessionProxy.delete(statement);
    }

    @Override
    public int delete(String statement, Object parameter) {
        return this.sqlSessionProxy.delete(statement, parameter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return getConfiguration().getMapper(type, this);
    }

    @Override
    public void commit() {
    	super.commit();
    }

    @Override
    public void commit(boolean force) {
    	super.commit(force);
    }

    @Override
    public void rollback() {
    	super.rollback();
    }

    @Override
    public void rollback(boolean force) {
    	super.rollback(force);
    }

    @Override
    public void close() {
        super.close();;
    }

    @Override
    public void clearCache() {
        super.clearCache();
    }

    @Override
    public Connection getConnection() {
        return super.getConnection();
    }

    @Override
    public List<BatchResult> flushStatements() {
        return super.flushStatements();
    }

    /**
     * 重写数据源拦截器，动态切换数据源
     * @author daijie_jay
     * @since 2018年5月15日
     */
    private class SqlSessionInterceptor implements InvocationHandler {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final SqlSession sqlSession = getSqlSession(
                    MybatisMultipleSqlSessionTemplate.this.getSqlSessionFactory(),
                    MybatisMultipleSqlSessionTemplate.this.executorType, 
                    MybatisMultipleSqlSessionTemplate.this.exceptionTranslator);
            try {
            	selectDataSource(args);
                Object result = method.invoke(sqlSession, args);
                sqlSession.rollback();
                if (!isSqlSessionTransactional(sqlSession, MybatisMultipleSqlSessionTemplate.this.getSqlSessionFactory())) {
                    sqlSession.commit(true);
                }
                return result;
            } catch (Throwable t) {
                Throwable unwrapped = unwrapThrowable(t);
                if (MybatisMultipleSqlSessionTemplate.this.exceptionTranslator != null && unwrapped instanceof PersistenceException) {
                    Throwable translated = MybatisMultipleSqlSessionTemplate.this.exceptionTranslator
                        .translateExceptionIfPossible((PersistenceException) unwrapped);
                    if (translated != null) {
                        unwrapped = translated;
                    }
                }
                throw unwrapped;
            } finally {
                closeSqlSession(sqlSession, MybatisMultipleSqlSessionTemplate.this.getSqlSessionFactory());
            }
        }
    }
    
    /**
     * 动态切换数据源
     * @param args 调用的方法参数
     * @throws ClassNotFoundException
     */
    private void selectDataSource(Object[] args) throws ClassNotFoundException {
    	Class<?> targetClass = Class.forName(args[0].toString().substring(0, args[0].toString().lastIndexOf(".")));
    	Method targetMethod = null;
		try {
			targetMethod = targetClass.getMethod(args[0].toString().substring(args[0].toString().lastIndexOf(".") + 1), args[1].getClass());
		} catch (Exception e) {
			
		}
        if (targetMethod != null && targetMethod.isAnnotationPresent(SelectDataSource.class)) {
        	SelectDataSource selectDataSource = targetMethod.getAnnotation(SelectDataSource.class);
        	DbContextHolder.setDataSourceName(selectDataSource.name());
        }else if(targetClass.isAnnotationPresent(SelectDataSource.class)){
        	SelectDataSource selectDataSource = (SelectDataSource) targetClass.getAnnotation(SelectDataSource.class);
        	DbContextHolder.setDataSourceName(selectDataSource.name());
        }
    }
}
