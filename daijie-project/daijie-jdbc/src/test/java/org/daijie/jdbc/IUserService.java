package org.daijie.jdbc;

public interface IUserService {

    void setUserMapper(UserMapper userMapper);

    public void testInsert();

    public void testSelect();

    public void testUpdate();

    public void testDelete();

    public void testSelectWrapper();

    public void testCostomize();

    public void testCommitTransaction();

    public void testCallbackTransaction();
}
