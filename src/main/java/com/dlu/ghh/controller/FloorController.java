package com.dlu.ghh.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dlu.ghh.bean.Floor;
import com.dlu.ghh.bean.Post;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.service.FloorService;
import com.dlu.ghh.service.PostService;
import com.dlu.ghh.utils.IntegerUtils;
import com.dlu.ghh.utils.ResponseMsg;
import com.dlu.ghh.utils.SessionUtils;
import com.dlu.ghh.utils.StringUtil;

@Controller
@RequestMapping("/floor")
public class FloorController {
	private Logger logger = Logger.getLogger(FloorController.class);

	@Autowired
	private FloorService floorService;
	
	@Autowired
	private PostService postService;
	
	@ResponseBody
	@RequestMapping(value="commentPost",method=RequestMethod.POST)
	public ResponseMsg commentPost(@RequestParam(value="id",required=false)Integer postId,
			@RequestParam(value="comment",required=false)String comment,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {//未登录
			return ResponseMsg.fail("登录后才能评论!");
		}else {//登录
			if(comment.length() > 100) 
				return ResponseMsg.fail("评论不能超过100个字!");
			User user = (User)session.getAttribute("user");
			if(postId != null && postId instanceof Integer && !StringUtil.isEmpty(comment)) {
				Floor floor = floorService.userComment(comment,postId,user);
				if(floor != null) {
					Post post = postService.getById(postId);
					if(post != null) 
						post.setFloors(post.getFloors()+1);
					else
						return ResponseMsg.fail("评论失败!");
					if(postService.updateByIdSelective(post)) {
						logger.info("用户"+user.getUsername()+"评论了id为"+postId+"的帖子");
						return ResponseMsg.success("评论成功").add("floor", floor).add("floors", post.getFloors()).add("sessionUser", user);
					}
				}
			}
		}
		return ResponseMsg.fail("评论失败!");
	}
	
	/**
	 * 删除楼层
	 * @param floorId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteById",method=RequestMethod.POST)
	public ResponseMsg deleteById(@RequestParam(value="floorId",required=false)Integer floorId,
			@RequestParam(value="postId",required=false)Integer postId) {
		if(!IntegerUtils.isEmpty(floorId) && !IntegerUtils.isEmpty(postId)) {
			Post post = postService.getById(postId);
			post.setFloors(post.getFloors()-1);
			if(floorService.deleteById(floorId) && postService.updateByIdSelective(post)) {
				logger.info("id为"+floorId+"的楼层被作者删除");
				return ResponseMsg.success("删除成功!");
			}
		}
		return ResponseMsg.fail("删除失败!");
	}
	
	@ResponseBody
	@RequestMapping(value="prasiedFloor",method=RequestMethod.POST)
	public ResponseMsg prasiedFloor(@RequestParam(value="floorId",required=false)Integer floorId) {
		if(!IntegerUtils.isEmpty(floorId)) {
			Floor floor = floorService.getById(floorId);
			if(floor != null) {
				floor.setPraisedTimes(floor.getPraisedTimes()+1);
				if(floorService.updateByIdSelective(floor)) {
					logger.info("id为"+floorId+"的楼层被赞了");
					return ResponseMsg.success("").add("times", floor.getPraisedTimes());
				}
			}
		}
		return ResponseMsg.fail("");
	}
	
}
