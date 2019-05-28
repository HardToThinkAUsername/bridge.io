package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.User;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    User selectByPrimaryKey(Integer userId);

    List<User> selectAll();

    int updateByPrimaryKey(User record);
    
    int insertIntoUser(User user);
    
    int insertIntoUserLogin(User user);
    
    User selectByUsername(String username);
    
    int updateByPrimaryKeySelective(User user);

	int updateProfilePhoto(User user);
	
	List<User> fuzzySelectSelective(User user);
}