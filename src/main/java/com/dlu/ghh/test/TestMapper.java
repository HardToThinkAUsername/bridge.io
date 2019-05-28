package com.dlu.ghh.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dlu.ghh.BridgeApplication;
import com.dlu.ghh.bean.Image;
import com.dlu.ghh.bean.Post;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.mapper.PostMapper;
import com.dlu.ghh.mapper.PostUserMapper;
import com.dlu.ghh.mapper.UserMapper;
import com.github.pagehelper.PageHelper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BridgeApplication.class)
public class TestMapper {
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private PostMapper postMapper;
	@Autowired
	private PostUserMapper postUserMapper;
	@Test
	public void test() {
		User user = new User();
		user.setPassword("66666");
		user.setUsername("123456");
		user.setId(1);
		user.setDisable(0);
		Image image = new Image();
		image.setId(1);
		user.setUserExperience(100);
		user.setUserImage(image);
		
		int insert = userMapper.insert(user);
		System.out.println(insert);
	}
	@Test
	public void test5() {
		System.out.println(postUserMapper.selectByPostIdAndUserId(1, 1));
	}
	
	@Test
	public void test2() {
		System.out.println(userMapper.selectByUsername("石增辉"));
	}
	
	@Test 
	public void testPageHelper() {
		PageHelper.startPage(0,3);
		List<Post> selectByUserId = postMapper.selectByUserId(11);
		for(Post p:selectByUserId) {
			System.out.println(p.getId());
		}
	}
}
