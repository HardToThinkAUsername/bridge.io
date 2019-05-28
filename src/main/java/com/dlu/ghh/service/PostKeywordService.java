package com.dlu.ghh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlu.ghh.bean.PostKeyword;
import com.dlu.ghh.mapper.PostKeywordMapper;

@Service
public class PostKeywordService {

	@Autowired
	private PostKeywordMapper postKeywordMapper;
	@Transactional
	public boolean insert(PostKeyword postKeyword) {
		return postKeywordMapper.insert(postKeyword)>0?true:false;
	}
	public boolean deleteByPostId(Integer postId) {
		return postKeywordMapper.deleteByPostId(postId)>0?true:false;
	}
	public List<PostKeyword> getByKeywordId(Integer keyWordId) {
		return postKeywordMapper.selectByKeywordId(keyWordId);
	}

	
}
