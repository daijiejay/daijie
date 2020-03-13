package org.daijie.jdbc;

import org.daijie.jdbc.annotation.Mapper;
import org.daijie.jdbc.annotation.Select;

import java.util.List;

/**
 * TODO
 *
 * @author daijie
 * @since 2020年03月13日
 */
@Mapper
public interface AnnotationMapper {

    @Select("select user_info.email,user_linkman.mobile,user.user_name,user.user_id,user_info.mobile,user_linkman.user_id,user_linkman.id,user_linkman.realname,user.create_date,user.remark " +
            "from user left join user_info on user.user_id = user_info.user_id left join user_linkman on user.user_id = user_linkman.user_id")
    List<UserDetailVo> selectUserDetail();

    @Select("select count(*) " +
            "from user left join user_info on user.user_id = user_info.user_id left join user_linkman on user.user_id = user_linkman.user_id")
    long selectCount();
}
