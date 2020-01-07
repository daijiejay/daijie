package org.daijie.jdbc;

import org.daijie.jdbc.annotation.Param;
import org.daijie.jdbc.annotation.Select;
import org.daijie.jdbc.result.PageResult;
import org.daijie.jdbc.scripting.MultiWrapper;
import org.daijie.jdbc.scripting.Wrapper;
import org.daijie.jdbc.session.SessionMapper;

import java.util.List;

public interface UserMapper extends SessionMapper<User> {

    List<UserVo> selectByWrapper2(Wrapper wrapper);

    PageResult<UserVo> selectPageByWrapper2(Wrapper wrapper);

    List<UserDetailVo> selectUserDetail(MultiWrapper multiWrapper);

    @Select(value = "select * from user where 1=1 <if test = 'userId != null'>and user_id = #{userId}</if><if test = 'userName != null'>and user_name = #{userName}</if>")
    List<User> selectUserForAnnotation1(User user);

    @Select(value = "select * from user where 1=1 <choose><when test = 'userId != null'>and user_id = #{userId}</when><otherwise>and user_name = 'test'</otherwise></choose>")
    List<User> selectUserForAnnotation2(User user);

    @Select(value = "select * from user where 1=1 and user_id in<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'><if test='index == 1'>#{item}</if></foreach>")
    List<User> selectUserForAnnotation3(@Param("list") List<Integer> list);
}
