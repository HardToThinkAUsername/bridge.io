package com.dlu.ghh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlu.ghh.bean.Floor;
import com.dlu.ghh.bean.Post;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.mapper.CommentMapper;
import com.dlu.ghh.mapper.FloorMapper;
import com.dlu.ghh.mapper.PostMapper;

@Service
public class FloorService {

	@Autowired
	private FloorMapper floorMapper;
	
	@Autowired
	private PostMapper postMapper;
	
	@Transactional
	public List<Floor> getByPostId(Integer id) {
		return floorMapper.selectByPostId(id);
	}

	@Transactional
	public Floor userComment(String comment, Integer postId,User user) {
		Floor floor = new Floor();
		floor.setContent(comment);
		floor.setDate(new Date());
		Post post = postMapper.selectByPrimaryKey(postId);
		if(post != null) 
			floor.setPost(post);
		else 
			return null;
		floor.setUser(user);
		floor.setPraisedTimes(0);
		floor.setReplyedTimes(0);
		if(floorMapper.insert(floor) > 0) {
			return floor;
		}
		return null;
	}
	
	@Autowired
	private CommentMapper commentMapper;
	@Transactional
	public boolean deleteById(Integer floorId) {
		commentMapper.deleteByFloorId(floorId);
		return floorMapper.deleteByPrimaryKey(floorId) > 0;
	}

	public Floor getById(Integer floorId) {
		return floorMapper.selectByPrimaryKey(floorId);
	}

	public boolean updateByIdSelective(Floor floor) {
		return floorMapper.updateByPrimaryKeySelective(floor)>0?true:false;
	}
	
}
