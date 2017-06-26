package org.daijie.mybatis.cloud.service.impl;

import java.util.List;

import org.daijie.core.factory.specific.ApiResultInitialFactory.Result;
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
	public Object getUser() {
		List<User> users = userMapper.selectAll();
		return Result.addData("users", users).build();
	}

	@Override
	public Object updateUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object addUser() {
		// TODO Auto-generated method stub
		return null;
	}

}
