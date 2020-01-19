# 工程简介
* 提供类似于Mybatis使用方式的ORM框架，通用Mapper方法，注解方式的SQL配置，以及多数据源配置、多数据源事务和零SQL多表关联查询。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-jdbc</artifactId>
	<version>2.0.0</version>
</dependency>
```
## 代码说明
### 生成JAVA文件
* 生成JAVA文件可以使用当前自带的插件，也可以使用其它类似mybatis-generator的任何插件，详情请看[daijie-jdbc-generator](https://github.com/daijiejay/daijie/tree/master/daijie-project/daijie-jdbc-generator "daijie-jdbc-generator")

Model类
```java
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "salt")
    private String salt;
    @Column(name = "remark")
    private String remark;
    @Column(name = "create_date")
    private Date createDate;
}
```
Mapper类
```java
public interface UserMapper extends SessionMapper<User> { 
}
```
### 创建数据库会话
```java
Map<String, Object> properties = new HashMap<>();
properties.put("driverClassName", "com.mysql.jdbc.Driver");
properties.put("url", "jdbc:mysql://localhost:3306/demo?characterEncoding=UTF-8");
properties.put("username", "root");
properties.put("password", "123456");
DataSource druidDataSource = DataSourceUtil.getDataSource(DruidDataSource.class, properties);
DataSourceManage.setDataSource(new SimpleDataSource(druidDataSource));
UserMapper userMapper = SessionMapperManager.createSessionMapper(UserMapper.class);
```
### 数据库增删改查操作
#### 普通操作
* 根据表主键查询
```java
userMapper.selectById(1);
```
* 根据映射对象参数查询一条数据
```java
userMapper.selectOne(user);
```
* 根据映射对象参数查询多条数据
```java
userMapper.selectList(user);
```
* 查询所有数据
```java
userMapper.selectAll();
```
* 根据映射对象参数查询数据的总条数
```java
userMapper.selectCount(user);
```
* 根据主键更新
```java
userMapper.updateById(1);
```
* 根据主键更新（只更新数据不等于null的数据）
```java
userMapper.updateSelectiveById(1);
```
* 插入单条数据
```java
userMapper.insert(user);
```
* 插入单条数据（只插入数据不等于null的数据）
```java
userMapper.insertSelective(user);
```
* 插入多条数据
```java
userMapper.insert(users);
```
* 根据主键删除
```java
userMapper.deleteById(id);
```
#### 条件包使用
* =
```java
Wrapper.newWrapper().andEqualTo("userId", 1));
Wrapper.newWrapper().orEqualTo("userId", 1));
```
* !=
```java
Wrapper.newWrapper().andNotEqualTo("userId", 1));
Wrapper.newWrapper().orNotEqualTo("userId", 1));
```
* \>
```java
Wrapper.newWrapper().andGreaterThan("createDate", "2019-06-29 10:19:02"));
Wrapper.newWrapper().orGreaterThan("createDate", "2019-06-29 10:19:02"));
```
* \>=
```java
Wrapper.newWrapper().andGreaterThanOrEqualTo("createDate", "2019-06-29 10:19:02"));
Wrapper.newWrapper().orGreaterThanOrEqualTo("createDate", "2019-06-29 10:19:02"));
```
* <
```java
Wrapper.newWrapper().andLessThan("createDate", "2019-06-29 10:19:02"));
Wrapper.newWrapper().orLessThan("createDate", "2019-06-29 10:19:02"));
```
* <=
```java
Wrapper.newWrapper().andLessThanOrEqualTo("createDate", "2019-06-29 10:19:02"));
Wrapper.newWrapper().orLessThanOrEqualTo("createDate", "2019-06-29 10:19:02"));
```
* in
```java
Wrapper.newWrapper().andIn("userId", Arrays.asList(new String[]{"1"})));
Wrapper.newWrapper().orIn("userId", Arrays.asList(new String[]{"1"})));
```
* not in
```java
Wrapper.newWrapper().andNotIn("userId", Arrays.asList(new String[]{"1"})));
Wrapper.newWrapper().orNotIn("userId", Arrays.asList(new String[]{"1"})));
```
* like
```java
Wrapper.newWrapper().andLike("remark", "%t%"));
Wrapper.newWrapper().orLike("remark", "%t%"));
```
* not like
```java
Wrapper.newWrapper().andNotLike("remark", "%t%"));
Wrapper.newWrapper().orNotLike("remark", "%t%"));
```
* regexp
```java
Wrapper.newWrapper().andRegexp("remark", "^t"));
Wrapper.newWrapper().orRegexp("remark", "^t"));
```
* between
```java
Wrapper.newWrapper().andBetween("createDate", "2019-06-29 10:19:02", "2019-06-29 10:19:03"));
Wrapper.newWrapper().orBetween("createDate", "2019-06-29 10:19:02", "2019-06-29 10:19:03"));
```
* not between
```java
Wrapper.newWrapper().andNotBetween("createDate", "2019-06-29 10:19:02", "2019-06-29 10:19:03"));
Wrapper.newWrapper().orNotBetween("createDate", "2019-06-29 10:19:02", "2019-06-29 10:19:03"));
```
* exists
```java
Wrapper.newWrapper().andExists(Wrapper.newWrapper(), UserInfo.class, new String[][]{{"userId","userId"},{"remark", "mobile"}}));
Wrapper.newWrapper().orExists(Wrapper.newWrapper(), UserInfo.class, new String[][]{{"userId","userId"},{"remark", "mobile"}}));
```
* not exists
```java
Wrapper.newWrapper().andNotExists(Wrapper.newWrapper(), UserInfo.class, new String[][]{{"userId","userId"},{"remark", "mobile"}}));
Wrapper.newWrapper().orNotExists(Wrapper.newWrapper(), UserInfo.class, new String[][]{{"userId","userId"},{"remark", "mobile"}}));
```
* group by
```java
Wrapper.newWrapper().groupBy("userName");
```
* order by
```java
Wrapper.newWrapper().orderByAsc("userName");
Wrapper.newWrapper().orderByDesc("userName");
```
* limit
```java
Wrapper.newWrapper().page(1, 1));
```
#### 条件包操作
* 根据条件查询多条数据
```java
userMapper.selectByWrapper(wrapper);
```
* 根据条件分页查询数据
```java
userMapper.selectPageByWrapper(wrapper);
```
* 根据条件参数查询数据的总条数
```java
userMapper.selectCountByWrapper(wrapper);
```
* 根据条件更新数据
```java
userMapper.updateByWrapper(user, wrapper);
```
* 根据条件更新数据（只更新数据不等于null的数据）
```java
userMapper.updateSelectiveByWrapper(user, wrapper);
```
* 根据条件删除数据
```java
userMapper.deleteByWrapper(wrapper);
```
#### 多表条件包使用
* 多表普通查询
```java
MultiWrapper multiWrapper = MultiWrapper.newWrapper(User.class, null)
                .andJoin(UserInfo.class, Wrapper.newWrapper())
                .andJoin(UserLinkman.class, Wrapper.newWrapper())
                .andEqual(User.class, UserInfo.class, "userId", "userId")
                .andEqual(User.class, UserLinkman.class, "userId", "userId")
                .end();
```
* 多表内连接查询
```java
MultiWrapper.newWrapper(User.class, null)
                .andInnerJoin(UserInfo.class, Wrapper.newWrapper())
                .onEqual(User.class, "userId", "userId")
                .endWrapper()
                .andInnerJoin(UserLinkman.class, Wrapper.newWrapper())
                .onEqual(User.class, "userId", "userId")
                .endWrapper()
                .end();
```
* 多表左连接查询
```java
MultiWrapper.newWrapper(User.class, null)
                .andLeftJoin(UserInfo.class, Wrapper.newWrapper())
                .onEqual(User.class, "userId", "userId")
                .endWrapper()
                .andLeftJoin(UserLinkman.class, Wrapper.newWrapper())
                .onEqual(User.class, "userId", "userId")
                .endWrapper()
                .end();
```
* 多表右连接查询
```java
MultiWrapper.newWrapper(User.class, null)
                .andRightJoin(UserInfo.class, Wrapper.newWrapper())
                .onEqual(User.class, "userId", "userId")
                .endWrapper()
                .andRightJoin(UserLinkman.class, Wrapper.newWrapper())
                .onEqual(User.class, "userId", "userId")
                .endWrapper()
                .end();
```
#### 多表查询自定义方法
* 自定义映射实体类
```java
@Data
public class UserDetailVo {

    @Column(table = "user", name = "user_id")
    private Integer userId;

    @Column(table = "user", name = "user_name")
    private String userName;

    @Column(table = "user", name = "remark")
    private String remark;

    @Column(table = "user", name = "create_date")
    private Date createDate;

    @Column(table = "user_info", name = "mobile")
    private String mobile;

    @Column(table = "user_info", name = "email")
    private String email;

    @Column(table = "user_linkman")
    private List<UserLinkman> userLinkmens;
}
```
* 自定义mapper方法
```java
@Data
public class UserMapper {
    List<UserDetailVo> selectUserDetail(MultiWrapper multiWrapper);
}
```
#### 动态SQL条件包使用
```java
Wrapper.newWrapper().andEqualTo("userId", 1).and(true, wrapper -> wrapper.andLike("remark", "%t%")))
```
#### 注解方式动态SQL自定义方法
* if
```java
public class UserMapper {
    @Select(value = "select * from user where 1=1 <if test = 'userId != null'>and user_id = #{userId}</if><if test = 'userName != null'>and user_name = #{userName}</if>")
    List<User> selectUserForAnnotation(User user);
}
```
* choose
```java
public class UserMapper {
    @Select(value = "select * from user where 1=1 <choose><when test = 'userId != null'>and user_id = #{userId}</when><otherwise>and user_name = 'test'</otherwise></choose>")
    List<User> selectUserForAnnotation(User user);
}
```
* foreach
```java
public class UserMapper {
    @Select(value = "select * from user where 1=1 and user_id in<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'><if test='index == 1'>#{item}</if></foreach>")
    List<User> selectUserForAnnotation(User user);
}
```
#### 函数或公式实体映射
```java
@Column(table = "user", name = "count(name)")
private long count;
@Column(table = "user", name = "name+':'+remark")
private String remark;
```
### 导入SQL脚本
#### 本地SQL文件
```java
//创建数据库会话
Map<String, Object> properties = new HashMap<>();
properties.put("driverClassName", "com.mysql.jdbc.Driver");
properties.put("url", "jdbc:mysql://localhost:3306/demo?characterEncoding=UTF-8&serverTimezone=UTC");
properties.put("username", "root");
properties.put("password", "123456");
DataSource druidDataSource = DataSourceUtil.getDataSource(DruidDataSource.class, properties);
DataSourceManage.setDataSource(new SimpleDataSource(druidDataSource));
new SqlExecutor("D:/user.sql").execute();
```
#### 项目resourcesSQL文件
```java
//创建数据库会话
... 
new SqlExecutor("classpath:user.sql").execute();
```
