package com.dlu.ghh.mapper;

import com.dlu.ghh.bean.Post;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface PostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Post record);

    Post selectByPrimaryKey(Integer id);

    List<Post> selectAll();

    int updateByPrimaryKey(Post record);

	List<Post> selectHelpPost();

	List<Post> selectIntroPost();

	List<Post> selectAllTypePostFuzzy(String content);
	
	List<Post> selectIntroPostFuzzy(String content);
	
	List<Post> selectHotPostFuzzy(String content);
	
	List<Post> selectHelpPostFuzzy(String content);

	Integer selectCountByUserId(Integer id);

	List<Post> selectByUserId(Integer id);
	
	int updateByPrimaryKeySelective(Post post);
	
	List<Post> selectNormalAndSealed();
	
	List<Post> fuzzySelectSelective(Post post);

	int sealById(Integer id);

	int unSealById(Integer postId);

	List<Post> selectByKeyword(String keyword);

	List<Post> selectNotReview(Post post);

	List<Post> selectByCentury(Integer century);
	
	
}