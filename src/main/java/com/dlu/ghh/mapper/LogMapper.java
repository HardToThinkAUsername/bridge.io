package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.Log;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface LogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Log record);

    Log selectByPrimaryKey(Integer id);

    List<Log> selectAll();

    int updateByPrimaryKey(Log record);
}