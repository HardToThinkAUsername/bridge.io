package com.dlu.ghh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlu.ghh.bean.Post;
import com.dlu.ghh.bean.PostUser;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.mapper.PostMapper;
import com.dlu.ghh.mapper.PostUserMapper;
import com.dlu.ghh.mapper.UserMapper;

@Service
public class PostUserService {

	@Autowired
	private PostUserMapper postUserMapper;
	
	@Autowired
	private PostMapper postMapper;
	@Autowired
	private UserMapper userMapper;

	/*
	 * 会员点赞
	 */
	@Transactional
	public int prasingPost(Integer postId, Integer userId) {
		PostUser postUser = postUserMapper.selectByPostIdAndUserId(postId,userId);
		if(postUser != null) {//存在对应记录
			return 1;
		}
		postUser = new PostUser();
		Post post = postMapper.selectByPrimaryKey(postId);
		User user = userMapper.selectByPrimaryKey(userId);
		if(post != null) {//如果帖子不为空
			post.setPraisedTimes(post.getPraisedTimes()+1);//该帖子被赞的次数加一
			postMapper.updateByPrimaryKey(post);//更新帖子记录
			if(user != null) {//如果user也不为空 ( 用户登录)
				postUser.setPost(post);
				postUser.setUser(user);
				postUser.setPraised(1);
				return  postUserMapper.insert(postUser)>0?2:0;//插入一条用户点赞的记录
			}
		}
		return 0;
	}

	/*
	 * 游客点赞
	 */
	public boolean prasingPost(Integer postId) {
		Post post = postMapper.selectByPrimaryKey(postId);
		if(post != null) {
			post.setPraisedTimes(post.getPraisedTimes()+1);
			return postMapper.updateByPrimaryKeySelective(post)>0?true:false;
		}
		return false;
	}

}
