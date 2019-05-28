package com.dlu.ghh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlu.ghh.mapper.RolePermissionMapper;

@Service
public class RolePermissionService {

	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	
	@Transactional
	public boolean isHasThePermission(Integer permissionId, Integer roleId) {
		return rolePermissionMapper.selectByPerIdAndRoleId(permissionId,roleId)>0?true:false;
	}
}
