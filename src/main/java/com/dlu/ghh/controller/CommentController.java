package com.dlu.ghh.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dlu.ghh.bean.Comment;
import com.dlu.ghh.bean.Floor;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.mapper.FloorMapper;
import com.dlu.ghh.service.CommentService;
import com.dlu.ghh.utils.IntegerUtils;
import com.dlu.ghh.utils.ResponseMsg;
import com.dlu.ghh.utils.SessionUtils;
import com.dlu.ghh.utils.StringUtil;

@Controller
@RequestMapping("/comment")
public class CommentController {
	private Logger logger = Logger.getLogger(CommentController.class);
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private FloorMapper floorMapper;
	
	@ResponseBody
	@RequestMapping(value="commentFloor",method=RequestMethod.POST)
	public ResponseMsg commentFloor(@RequestParam(value="comment",required=false)String comment,
			@RequestParam(value="floorId",required=false)Integer floorId,
			HttpSession session) {
		if(SessionUtils.sessionValidate(session)) {
			if(!StringUtil.isEmpty(comment)) {
				if(comment.length() < 100) {
					if(!IntegerUtils.isEmpty(floorId)) {
						User user = (User)session.getAttribute("user");
						Floor floor = floorMapper.selectByPrimaryKey(floorId);
						floor.setReplyedTimes(floor.getReplyedTimes()+1);
						boolean b = floorMapper.updateByPrimaryKeySelective(floor)>0?true:false;
						if(commentService.commentFloor(comment,floorId,user) && b) {
							logger.info("用户: "+user.getUsername()+"评论了id为: " + floorId+"的楼层");
							return ResponseMsg.success("评论成功!");
						}
					}
				}else
					return ResponseMsg.fail("评论长度不能超过100!");
			}else
				return ResponseMsg.fail("评论不能为空!");
		}else
			return ResponseMsg.fail("登录后才能评论!");
		return ResponseMsg.fail("评论失败!");
	}
	
	@ResponseBody
	@RequestMapping(value="getCommentByFloorId",method=RequestMethod.GET)
	public ResponseMsg getCommentByFloorId(@RequestParam(value="floorId")Integer floorId,
			HttpSession session) {
		if(!IntegerUtils.isEmpty(floorId)) {
			List<Comment> list = commentService.getAllByFloorId(floorId);
			if(list != null) {
				if(SessionUtils.sessionValidate(session)) {
					User user = (User)session.getAttribute("user");
					logger.info("用户:"+user.getUsername()+"查看了id为:"+floorId+"的楼层的评论");
					return ResponseMsg.success("").add("comments", list).add("sessionUser", user);
				}else
					return ResponseMsg.success("").add("comments", list).add("sessionUser", "null");
			}
		}
		return ResponseMsg.fail("查询出错,请刷新重试!");
	}
	
	@ResponseBody
	@RequestMapping(value="deleteById",method=RequestMethod.POST)
	public ResponseMsg deleteById(@RequestParam(value="commentId",required=false)Integer commentId,
			@RequestParam(value="floorId",required=false)Integer floorId) {
		if(!IntegerUtils.isEmpty(commentId) && !IntegerUtils.isEmpty(floorId)) {
			if(commentService.deleteReply(commentId,floorId)) {
				logger.info("id为"+commentId+"的评论被作者自己删除");
				return ResponseMsg.success("删除成功!");
			}
		}
		return ResponseMsg.fail("删除失败!");
	}
}
