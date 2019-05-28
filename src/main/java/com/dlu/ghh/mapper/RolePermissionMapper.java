package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.RolePermission;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface RolePermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RolePermission record);

    RolePermission selectByPrimaryKey(Integer id);

    List<RolePermission> selectAll();

    int updateByPrimaryKey(RolePermission record);

	int selectByPerIdAndRoleId(@Param("permissionId")Integer permissionId, @Param("roleId")Integer roleId);
}