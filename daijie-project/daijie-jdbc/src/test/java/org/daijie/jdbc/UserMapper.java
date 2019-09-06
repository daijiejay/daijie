package org.daijie.jdbc;

import org.daijie.jdbc.result.PageResult;
import org.daijie.jdbc.scripting.MultiWrapper;
import org.daijie.jdbc.scripting.Wrapper;
import org.daijie.jdbc.session.SessionMapper;

import java.util.List;

public interface UserMapper extends SessionMapper<User> {

    List<UserVo> selectByWrapper2(Wrapper wrapper);

    PageResult<UserVo> selectPageByWrapper2(Wrapper wrapper);

    List<UserDetailVo> selectUserDetail(MultiWrapper multiWrapper);
}
