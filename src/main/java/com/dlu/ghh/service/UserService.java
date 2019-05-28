package com.dlu.ghh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlu.ghh.bean.Image;
import com.dlu.ghh.bean.Role;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.mapper.UserMapper;
import com.dlu.ghh.utils.StringUtil;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 注册用户
	 * @param username
	 * @param password
	 * @param email
	 * @param address
	 * @param phoneNum
	 * @param date
	 * @return
	 */
	@Transactional
	public int userRegister(String username,String password,String email,String address,String phoneNum,Date date,int gender) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setAddress(address);
		user.setEmail(email);
		user.setPhoneNum(phoneNum);
		user.setDisable(0);
		user.setRole(new Role(0));
		user.setRegisterDate(date);
		user.setUserImage(new Image(1));
		user.setUserExperience(0);
		user.setGender(gender);
		user.setShutup(0);
		user.setUserExperience(100);
		user.setPraiseTimes(0);
		user.setReplyTimes(0);
		user.setContinuousSigninTimes(0);
		user.setLastSigninDate(new Date(new Date().getTime()-24*60*60*1000));
		return userMapper.insert(user);
	}

	@Transactional
	public User selectByUsername(String username) {
		return userMapper.selectByUsername(username);
	}
	
	@Transactional
	public boolean userUpdate(String id, String username, String password, String email,
			String address, String phoneNum,int disable,int role,int gender) {
		User user = new User();
		if(!StringUtil.isEmpty(id)) {
			Integer idInteger = Integer.parseInt(id);
			user.setId(idInteger);
		}
		user.setAddress(address);
		user.setEmail(email);
		user.setPassword(password);
		user.setPhoneNum(phoneNum);
		user.setUsername(username);
		user.setRole(new Role(role));
		user.setDisable(disable);
		user.setGender(gender);
		return userMapper.updateByPrimaryKeySelective(user)>0?true:false;
	}
	@Transactional
	public boolean updateUserSelectiveByPrimary(User user) {
		return userMapper.updateByPrimaryKeySelective(user)>0?true:false;
	}
	@Transactional
	public User getById(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}
	@Transactional
	public List<User> getAll(){
		return userMapper.selectAll();
	}
	@Transactional
	public List<User> selectSelectiveFuzzy(User user) {
		return userMapper.fuzzySelectSelective(user);
	}
	@Transactional
	public boolean deleteById(Integer userId) {
		return userMapper.deleteByPrimaryKey(userId)>0?true:false;
	}

	@Transactional
	public boolean deleteByIds(String userIds) {
		String[] strings = userIds.split(",");
		for(String string : strings) {
			if(!"".equals(string.trim())) {
				try {
					int i = Integer.parseInt(string);
					if(!deleteById(i)) {
						return false;
					}
				} catch (NumberFormatException e) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean shutupMultiple(String userIds) {
		String[] strings = userIds.split(",");
		for(String string : strings) {
			if(!"".equals(string.trim())) {
				try {
					int i = Integer.parseInt(string);
					User user = new User();
					user.setId(i);
					user.setShutup(1);
					if(userMapper.updateByPrimaryKeySelective(user) <= 0) {
						return false;
					}
				} catch (NumberFormatException e) {
					return false;
				}
			}
		}
		return true;
	}
}
