package com.dlu.ghh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlu.ghh.bean.SecretMsg;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.mapper.SecretMsgMapper;
import com.dlu.ghh.mapper.UserMapper;

@Service
public class SecretService {

	@Autowired
	private SecretMsgMapper secretMsgMapper;
	@Autowired
	private UserMapper userMapper;
	@Transactional
	public boolean sendSecretMsg(Integer receiverId, String msg, User sender) {
		SecretMsg secretMsg = new SecretMsg();
		secretMsg.setMsg(msg);
		secretMsg.setReceiver(userMapper.selectByPrimaryKey(receiverId));
		secretMsg.setSender(sender);
		secretMsg.setDate(new Date());
		return secretMsgMapper.insert(secretMsg)>0?true:false;
	}
	public List<SecretMsg> getByUserId(Integer userId) {
		return secretMsgMapper.selectByUserId(userId);
	}

}
