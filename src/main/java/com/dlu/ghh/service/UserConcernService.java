package com.dlu.ghh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlu.ghh.bean.User;
import com.dlu.ghh.bean.UserConcern;
import com.dlu.ghh.mapper.UserConcernMapper;

@Service
public class UserConcernService {

	@Autowired
	private UserConcernMapper userConcernMapper;
	
	@Transactional
	public boolean alreadyConcern(Integer userId, Integer concernUserId) {
		UserConcern userConcern = userConcernMapper.selectByUserIdAndConcernUserId(userId,concernUserId);
		if(userConcern != null) {
			return true;
		}
		return false;
	}

	@Transactional
	public boolean concernOther(Integer userId, Integer concernUserId) {
		UserConcern userConcern = new UserConcern();
		User user = new User();
		user.setId(userId);
		User concernUser = new User();
		concernUser.setId(concernUserId);
		userConcern.setUser(user);
		userConcern.setConcernUser(concernUser);
		return userConcernMapper.insert(userConcern)>0?true:false;
	}

	public List<UserConcern> getByUserId(Integer userId) {
		return userConcernMapper.selectByUserId(userId);
	}

	public boolean cancelConcern(Integer userId, Integer concernUserId) {
		UserConcern userConcern = userConcernMapper.selectByUserIdAndConcernUserId(userId, concernUserId);
		if(userConcern != null) {
			if(userConcernMapper.deleteByPrimaryKey(userConcern.getId()) > 0) {
				return true;
			}
		}
		return false;
	}
}
