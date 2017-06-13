package com.gwy.recodecenter.mapper;

import com.gwy.recodecenter.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> selectByConditions(String conditions);

    int selectCountByConditions(String conditions);

    int deleteLogicByConditions(String conditions);

    int deleteLogicByPrimaryKey(Integer id);

    int updateByConditionsSelective(@Param("record") User record, @Param("conditions") String conditions);
}