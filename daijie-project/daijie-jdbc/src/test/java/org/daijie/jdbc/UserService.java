package org.daijie.jdbc;

import org.daijie.jdbc.scripting.AgileWrapper;
import org.daijie.jdbc.scripting.Wrapper;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author daijie
 * @since 2019/6/9
 */
public class UserService /*implements IUserService*/ {

    private UserMapper userMapper;

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void testInsert() {
        User user = new User();
        user.setSalt("222");
        user.setUserName("test");
        user.setPassword("test");
        //测试insert
        Assert.assertTrue(userMapper.insert(user));
        //测试insertSelective
        Assert.assertTrue(userMapper.insertSelective(user));
        List<User> list = new ArrayList<>();
        User user1 = new User();
        user1.setUserName("test1");
        user1.setPassword("test1");
        list.add(user1);
        User user2 = new User();
        user2.setUserName("test2");
        user2.setPassword("test2");
        list.add(user2);
        //测试insert
        Assert.assertTrue(userMapper.insert(list));
    }

    public void testSelect() {
        User user = new User();
        user.setUserId(1);
        //测试selectById
        Assert.assertTrue(userMapper.selectById(1) != null);
        //测试selectList
        Assert.assertTrue(userMapper.selectList(user).size() > 0);
        //测试selectOne
        Assert.assertTrue(userMapper.selectOne(user) != null);
        //测试selectAll
        Assert.assertTrue(userMapper.selectAll().size() > 0);
        //测试selectCount
        Assert.assertTrue(userMapper.selectCount(user) > 0);

    }

    public void testUpdate() {
        List<User> users = userMapper.selectAll();
        User user = new User();
        user.setUserId(users.get(users.size() - 1).getUserId());
        //测试updateById
        user.setSalt("222");
        Assert.assertTrue(userMapper.updateById(user));
    }

    public void testDelete() {
        List<User> users = userMapper.selectAll();
        //测试deleteById
        Assert.assertTrue(userMapper.deleteById(users.get(users.size() - 1).getUserId()));
    }

    public void testSelectWrapper() {
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andEqualTo("userId", 1)));
        Assert.assertNotNull(userMapper.selectCountByWrapper(Wrapper.newWrapper().andEqualTo("userId", 1)));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andNotEqualTo("userId", 0)));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andIn("userId", Arrays.asList(new String[]{"1"}))));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andNotIn("userId", Arrays.asList(new String[]{"1"}))));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andLike("remark", "%t%")));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andNotLike("remark", "%t%")));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andGreaterThan("createDate", "2019-06-29 10:19:02")));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andGreaterThanOrEqualTo("createDate", "2019-06-29 10:19:02")));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andLessThan("createDate", "2019-06-29 10:19:02")));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andLessThanOrEqualTo("createDate", "2019-06-29 10:19:02")));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andBetween("createDate", "2019-06-29 10:19:02", "2019-06-29 10:19:03")));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andBracket(Wrapper.newWrapper().andEqualTo("userId", 1))));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andExists(Wrapper.newWrapper(), UserInfo.class, new String[][]{{"userId","userId"},{"remark", "mobile"}})));
//        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().groupBy("userName")));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().orderByAsc("userName")));
        Assert.assertNotNull(userMapper.selectPageByWrapper(Wrapper.newWrapper()));
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().page(1, 1)));
        Assert.assertNotNull(userMapper.selectPageByWrapper(Wrapper.newWrapper().page(1, 2)));
        Assert.assertNotNull(userMapper.selectCountByWrapper(Wrapper.newWrapper().andEqualTo("userName", "test")));
    }

    public void testDeleteWrapper() {
        Assert.assertNotNull(userMapper.deleteByWrapper(Wrapper.newWrapper().andLike("userName", "%test%")));
    }

    public void testCostomize() {
        Assert.assertNotNull(userMapper.selectByWrapper2(Wrapper.newWrapper().andEqualTo("userId", 1)));
        Assert.assertNotNull(userMapper.selectPageByWrapper2(Wrapper.newWrapper().page(1, 1)));
    }

    public void testCostomizeAgileWrapper() {
        AgileWrapper agileWrapper = AgileWrapper.newWrapper(User.class, null)
                .and(UserInfo.class, Wrapper.newWrapper()).leftJoin().on(User.class, "id", "userId").end()
                .and(UserLinkman.class, Wrapper.newWrapper()).leftJoin().on(User.class, "id", "userId").end();
    }
}
