package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.SecretMsg;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface SecretMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SecretMsg record);

    SecretMsg selectByPrimaryKey(Integer id);

    List<SecretMsg> selectAll();

    int updateByPrimaryKey(SecretMsg record);

	List<SecretMsg> selectByUserId(Integer userId);
	
}