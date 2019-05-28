package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.Data;
import com.dlu.ghh.bean.Post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface DataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Data record);

    Data selectByPrimaryKey(Integer id);

    List<Data> selectAll();

    int updateByPrimaryKey(Data record);

	Integer selectCountByUserId(Integer id);

	List<Post> selectFuzzy(String content);

	List<Data> selectByUserId(Integer id);

	int updateByIdSelective(Data data);
	
	List<Data> fuzzySelectSelective(Data data);

	List<Data> selectNotReview(Data data);

}