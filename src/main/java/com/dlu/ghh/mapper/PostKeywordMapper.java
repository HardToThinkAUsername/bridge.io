package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.PostKeyword;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface PostKeywordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostKeyword record);

    PostKeyword selectByPrimaryKey(Integer id);

    List<PostKeyword> selectAll();

    int updateByPrimaryKey(PostKeyword record);

	int deleteByPostId(Integer postId);

	List<PostKeyword> selectByKeywordId(Integer keywordId);
}