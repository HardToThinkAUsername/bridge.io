package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.UserConcern;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UserConcernMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserConcern record);

    UserConcern selectByPrimaryKey(Integer id);

    List<UserConcern> selectAll();

    int updateByPrimaryKey(UserConcern record);

	UserConcern selectByUserIdAndConcernUserId(@Param("userId")Integer userId, @Param("concernUserId")Integer concernUserId);

	List<UserConcern> selectByUserId(Integer userId);
}