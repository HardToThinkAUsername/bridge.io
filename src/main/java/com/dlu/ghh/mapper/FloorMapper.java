package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.Floor;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface FloorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Floor record);

    Floor selectByPrimaryKey(Integer id);

    List<Floor> selectAll();

    int updateByPrimaryKey(Floor record);

	List<Floor> selectByPostId(Integer id);

	int updateByPrimaryKeySelective(Floor floor);

	int deleteByPostId(Integer id);
}