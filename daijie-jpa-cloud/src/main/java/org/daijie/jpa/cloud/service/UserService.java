package org.daijie.jpa.cloud.service;

import org.daijie.jpa.cloud.dao.UserSearchRepository;
import org.daijie.jpa.cloud.service.base.BaseSearchService;
import org.daijie.mybatis.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseSearchService<User, Integer, UserSearchRepository> {

	public User getUserByUserName(String userName) {
        return getRepository().getUserByUserName(userName);
    }
}
