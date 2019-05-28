package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.Role;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    Role selectByPrimaryKey(Integer id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);
}