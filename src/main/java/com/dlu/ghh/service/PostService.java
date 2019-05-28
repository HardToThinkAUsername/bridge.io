package com.dlu.ghh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlu.ghh.bean.Floor;
import com.dlu.ghh.bean.Post;
import com.dlu.ghh.bean.Statu;
import com.dlu.ghh.mapper.PostKeywordMapper;
import com.dlu.ghh.mapper.PostMapper;

@Service
public class PostService {

	@Autowired
	private PostMapper postMapper;
	@Autowired
	private PostKeywordMapper postKeywordMapper;
	
	@Transactional
	public boolean insertPost(Post post) {
		post.setFloors(0);
		post.setReadedTimes(0);
		post.setStatus(Statu.TOREVIEW);
		post.setPraisedTimes(0);
		post.setLatestReplyUser(null);
		return postMapper.insert(post)>0?true:false;
	}

	@Transactional
	public List<Post> getHotPost() {
		return postMapper.selectAll();
	}
	@Transactional
	public List<Post> getHelpPost() {
		return postMapper.selectHelpPost();
	}
	@Transactional
	public List<Post> getIntroPost() {
		return postMapper.selectIntroPost();
	}
	@Transactional
	public List<Post> getAllTypePostFuzzy(String content) {
		return postMapper.selectAllTypePostFuzzy(content);
	}
	@Transactional
	public List<Post> getHotPostFuzzy(String content) {
		return postMapper.selectHotPostFuzzy(content);
	}
	@Transactional
	public List<Post> getIntroPostFuzzy(String content) {
		return postMapper.selectIntroPostFuzzy(content);
	}
	@Transactional
	public List<Post> getHelpPostFuzzy(String content) {
		return postMapper.selectHelpPostFuzzy(content);
	}
	@Transactional
	public Integer getCountByUserId(Integer id) {
		return postMapper.selectCountByUserId(id);
	}

	@Transactional
	public List<Post> getPostByUserId(Integer id) {
		return postMapper.selectByUserId(id);
	}
	
	@Autowired
	private FloorService floorService;
	@Transactional
	public boolean deleteById(Integer id) {
		postKeywordMapper.deleteByPostId(id);
		List<Floor> floors = floorService.getByPostId(id);
		for(Floor floor : floors) {
			floorService.deleteById(floor.getId());
		}
		return postMapper.deleteByPrimaryKey(id)>0;
	}
	@Transactional
	public Post userRead(Integer id) {
		Post post = postMapper.selectByPrimaryKey(id);
		post.setReadedTimes(post.getReadedTimes()+1);
		postMapper.updateByPrimaryKey(post);
		return post;
	}
	@Transactional
	public Post getById(Integer postId) {
		return postMapper.selectByPrimaryKey(postId);
	}
	@Transactional
	public boolean updateByIdSelective(Post post) {
		return postMapper.updateByPrimaryKeySelective(post)>0?true:false;
		
	}
	@Transactional
	public List<Post> getNormalAndSealed() {
		return postMapper.selectNormalAndSealed();
	}
	@Transactional
	public List<Post> getAllSelectiveFuzzy(Post post) {
		return postMapper.fuzzySelectSelective(post);
	}

	@Transactional
	public boolean sealById(Integer postId) {
		return postMapper.sealById(postId)>0?true:false;
	}
	@Transactional
	public boolean unSealById(Integer postId) {
		return postMapper.unSealById(postId)>0?true:false;
	}
	@Transactional
	public boolean sealByIds(String postIds) {
		String[] strings = postIds.split(",");
		for(String string : strings) {
			if(!"".equals(string.trim())) {
				try {
					int i = Integer.parseInt(string);
					if(!sealById(i)) {
						return false;
					}
				} catch (NumberFormatException e) {
					return false;
				}
			}
		}
		return true;
	}
	@Transactional
	public boolean deleteByIds(String postIds) {
		String[] strings = postIds.split(",");
		for(String string : strings) {
			if(!"".equals(string.trim())) {
				try {
					int i = Integer.parseInt(string);
					if(deleteById(i)) {
						postKeywordMapper.deleteByPostId(i);
					}
				} catch (NumberFormatException e) {
					return false;
				}
			}
		}
		return false;
	}

	public List<Post> getByKeyword(String string) {
		return postMapper.selectByKeyword(string);
	}

	public boolean review(Integer postId) {
		Post post = getById(postId);
		post.setStatus(0);
		return postMapper.updateByPrimaryKeySelective(post)>0?true:false;
	}

	public List<Post> getNotReview(Post post) {
		return postMapper.selectNotReview(post);
	}

	public List<Post> getByCentury(Integer century) {
		return postMapper.selectByCentury(century);
	}
	
}
