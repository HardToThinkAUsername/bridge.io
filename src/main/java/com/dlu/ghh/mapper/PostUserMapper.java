package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.PostUser;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface PostUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostUser record);

    PostUser selectByPrimaryKey(Integer id);

    List<PostUser> selectAll();

    int updateByPrimaryKey(PostUser record);

	PostUser selectByPostIdAndUserId(@Param(value="postId")Integer postId, @Param("userId")Integer userId);
}