package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.Permission;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    Permission selectByPrimaryKey(Integer id);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);
}