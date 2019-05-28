package com.dlu.ghh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlu.ghh.bean.Comment;
import com.dlu.ghh.bean.Floor;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.mapper.CommentMapper;
import com.dlu.ghh.mapper.FloorMapper;

@Service
public class CommentService {

	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	private FloorMapper floorMapper;
	
	public boolean commentFloor(String comment, Integer floorId, User user) {
		Comment record = new Comment();
		record.setContent(comment);
		record.setDate(new Date());
		Floor floor = floorMapper.selectByPrimaryKey(floorId);
		if(floor == null) {
			return false;
		}
		record.setFloor(floor);
		record.setUser(user);
		return commentMapper.insert(record)>0?true:false;
	}

	public List<Comment> getAllByFloorId(Integer floorId) {
		return commentMapper.selectByFloorId(floorId);
	}

	@Transactional
	public boolean deleteReply(Integer commentId, Integer floorId) {
		long l = Long.parseLong(commentId.toString());
		int i = commentMapper.deleteByPrimaryKey(l);
		if(i > 0) {
			Floor floor = floorMapper.selectByPrimaryKey(floorId);
			floor.setReplyedTimes(floor.getReplyedTimes()-1);
			int j = floorMapper.updateByPrimaryKeySelective(floor);
			if(j > 0) {
				return true;
			}
		}
		return false;
	}
	
	
}
