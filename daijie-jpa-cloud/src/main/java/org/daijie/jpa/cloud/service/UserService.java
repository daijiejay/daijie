package org.daijie.jpa.cloud.service;

import java.util.List;

import org.daijie.api.UserCloud;
import org.daijie.core.result.ModelResult;
import org.daijie.core.factory.specific.ModelResultInitialFactory.Result;
import org.daijie.jpa.cloud.dao.UserSearchRepository;
import org.daijie.jpa.cloud.service.base.BaseSearchService;
import org.daijie.mybatis.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class UserService extends BaseSearchService<User, Integer, UserSearchRepository> implements UserCloud {

	@Override
	public ModelResult<User> getUser(@PathVariable(name = "userId") Integer userId) {
		return Result.build(getRepository().getUserByUserId(userId));
	}

	@Override
	public ModelResult<List<User>> getUserAll() {
		return Result.build(getRepository().getUserAll());
	}

	@Override
	public ModelResult<User> getUser(@PathVariable(name = "userName") String userName) {
		return Result.build(getRepository().getUserByUserName(userName));
	}

	@Override
	public ModelResult<Boolean> updateUser(User user) {
		return Result.build(getRepository().updateUser(user));
	}

	@Override
	public ModelResult<Boolean> addUser(User user) {
		return Result.build(getRepository().addUser(user));
	}
}
