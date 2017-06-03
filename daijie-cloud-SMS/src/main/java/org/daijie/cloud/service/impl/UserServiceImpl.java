package org.daijie.cloud.service.impl;

import java.util.List;

import org.daijie.api.mapper.UserMapper;
import org.daijie.api.model.User;
import org.daijie.cloud.service.UserService;
import org.daijie.core.factory.specific.ApiResultInitialFactory.Result;
import org.daijie.web.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends BasicService<UserMapper> implements UserService {
	
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
