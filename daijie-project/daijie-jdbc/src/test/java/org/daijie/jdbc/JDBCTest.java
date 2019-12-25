package org.daijie.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import org.daijie.jdbc.datasource.DataSourceManage;
import org.daijie.jdbc.datasource.DataSourceUtil;
import org.daijie.jdbc.datasource.SimpleDataSource;
import org.daijie.jdbc.session.SessionMapperManager;
import org.daijie.jdbc.transaction.TransactionManager;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author daijie
 * @since 2019/5/30
 */
public class JDBCTest {
    private UserMapper userMapper;
    private UserService userService;

    @Before
    public void init() throws ReflectiveOperationException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("driverClassName", "com.mysql.jdbc.Driver");
        properties.put("url", "jdbc:mysql://localhost:3306/demo?characterEncoding=UTF-8");
        properties.put("username", "root");
        properties.put("password", "123456");
        DataSource druidDataSource = DataSourceUtil.getDataSource(DruidDataSource.class, properties);
        DataSourceManage.setDataSource(new SimpleDataSource(druidDataSource));
        this.userMapper = SessionMapperManager.createSessionMapper(UserMapper.class);
    }

    /**
     * 测试没有事务的增删改查
     */
    @Test
    public void testSRUDNotTransaction() {
        this.userService = new UserService();
        this.userService.setUserMapper(userMapper);
        this.userService.testInsert();
        this.userService.testSelect();
        this.userService.testUpdate();
        this.userService.testDelete();
        this.userService.testSelectWrapper();
        this.userService.testDeleteWrapper();
        this.userService.testCostomize();
        this.userService.testCostomizeMultiWrapper();
    }

    /**
     * 测试CGLib代理有事务的增删改查
     */
    @Test
    public void testCGLibProxySRUDTransaction() {
        this.userService = TransactionManager.registerTransactionManageTarget(UserService.class);
        this.userService.setUserMapper(userMapper);
        this.userService.testCommitTransaction();
        this.userService.testCallbackTransaction();
    }

    /**
     * 测试JDK代理有事务的增删改查
     */
    @Test
    public void testJDKProxySRUDTransaction() {
        IUserService iUserService = TransactionManager.registerTransactionManageTarget(JDKProxyUserService.class);
        iUserService.setUserMapper(userMapper);
        iUserService.testCommitTransaction();
        iUserService.testCallbackTransaction();
    }
}
