package org.daijie.jdbc;

import org.daijie.jdbc.scripting.MultiWrapper;
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
        Assert.assertNotNull(userMapper.selectByWrapper(Wrapper.newWrapper().andRegexp("remark", "^t")));
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

    public void testCostomizeMultiWrapper() {
        //select user_info.email,user_linkman.mobile,user.user_name,user.user_id,user_info.mobile,user_linkman.user_id,user_linkman.id,user_linkman.realname,user.create_date,user.remark from user left join user_info on user.user_id = user_info.user_id left join user_linkman on user.user_id = user_linkman.user_id
        MultiWrapper multiWrapper = MultiWrapper.newWrapper(User.class, null)
                .andLeftJoin(UserInfo.class, Wrapper.newWrapper())
                .onEqual(User.class, "userId", "userId")
                .endWrapper()
                .andLeftJoin(UserLinkman.class, Wrapper.newWrapper())
                .onEqual(User.class, "userId", "userId")
                .endWrapper()
                .end();
        Assert.assertNotNull(userMapper.selectUserDetail(multiWrapper));
    }

    public void testCommitTransaction() {
        List<User> users = userMapper.selectAll();
        User user = new User();
        user.setUserId(users.get(users.size() - 1).getUserId());
        user.setSalt("333");
        userMapper.updateById(user);

        users = userMapper.selectAll();
        Assert.assertEquals(users.get(users.size() - 1).getSalt(), "333");

        user.setSalt("444");
        userMapper.updateById(user);

        users = userMapper.selectAll();
        Assert.assertEquals(users.get(users.size() - 1).getSalt(), "444");
    }

    public void testCallbackTransaction() {
        try {
            callbackTransaction();
        }catch (Exception e) {
            e.printStackTrace();
        }
        List<User> users = userMapper.selectAll();
        Assert.assertNotEquals(users.get(users.size() - 1).getSalt(), "555");
        Assert.assertNotEquals(users.get(users.size() - 1).getSalt(), "666");

        User user = new User();
        user.setUserId(users.get(users.size() - 1).getUserId());
        user.setSalt("222");
        userMapper.updateById(user);

        users = userMapper.selectAll();
        Assert.assertEquals(users.get(users.size() - 1).getSalt(), "222");
    }

    public void callbackTransaction() {
        List<User> users = userMapper.selectAll();
        User user = new User();
        user.setUserId(users.get(users.size() - 1).getUserId());
        user.setSalt("555");
        userMapper.updateById(user);

        users = userMapper.selectAll();
        Assert.assertEquals(users.get(users.size() - 1).getSalt(), "555");

        int a = 1/0;

        user.setSalt("888");
        userMapper.updateById(user);

        users = userMapper.selectAll();
        Assert.assertEquals(users.get(users.size() - 1).getSalt(), "888");
    }
}
