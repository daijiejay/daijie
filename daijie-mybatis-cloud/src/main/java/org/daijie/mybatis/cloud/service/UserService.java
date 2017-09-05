package org.daijie.mybatis.cloud.service;

import org.daijie.mybatis.model.User;


public interface UserService {

	public User getUser(Integer userId);
	
	public User getUserByUserName(String userName);
	
	public boolean updateUser(User user);
	
	public boolean addUser(User user);
}
