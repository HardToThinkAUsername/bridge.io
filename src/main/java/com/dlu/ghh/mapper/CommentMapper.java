package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.Comment;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface CommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    Comment selectByPrimaryKey(Long id);

    List<Comment> selectAll();

    int updateByPrimaryKey(Comment record);

	List<Comment> selectByFloorId(Integer floorId);

	int deleteByFloorId(Integer floorId);
}