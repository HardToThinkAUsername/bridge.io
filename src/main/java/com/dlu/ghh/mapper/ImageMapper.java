package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.Image;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface ImageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Image record);

    Image selectByPrimaryKey(Integer id);

    List<Image> selectAll();

    int updateByPrimaryKey(Image record);

	List<Image> selectByPath(String path);
}