package org.daijie.mybatis.cloud.service.impl;

import org.daijie.mybatis.cloud.service.UserService;
import org.daijie.mybatis.mapper.UserMapper;
import org.daijie.mybatis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public User getUser(Integer userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		return user;
	}

	@Override
	public boolean updateUser(User user) {
		return userMapper.updateByPrimaryKey(user) > 0;
	}

	@Override
	public boolean addUser(User user) {
		return userMapper.insert(user) > 0;
	}

	@Override
	public User getUserByUserName(String userName) {
		User user = new User();
		user.setUserName(userName);
		return userMapper.selectOne(user);
	}

}
