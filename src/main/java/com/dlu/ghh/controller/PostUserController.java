package com.dlu.ghh.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.service.PostUserService;
import com.dlu.ghh.utils.ResponseMsg;
import com.dlu.ghh.utils.SessionUtils;

@Controller
@RequestMapping("/postUser")
public class PostUserController {
	private Logger logger = Logger.getLogger(PostUserController.class);
	@Autowired
	private PostUserService postUserService;

	/**
	 * 点赞帖子
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="prasingPost",method=RequestMethod.POST)
	public ResponseMsg prasingPost(HttpSession session,
			@RequestParam(value="id",required=false)Integer postId) {
		if(postId != null && postId instanceof Integer) {//id有效
			if(SessionUtils.sessionValidate(session)) {//session有效
				User user = (User)session.getAttribute("user");
				int i = postUserService.prasingPost(postId,user.getId());
				if(i == 1 || i == 2) {//会员点赞成功
					logger.info("点赞了id为"+postId+"的帖子");
					return ResponseMsg.success("").add("num", i);
				}
			}else {//session无效(未登录)
				if(postUserService.prasingPost(postId)) {//游客点赞成功
					return ResponseMsg.success("");
				}
			}
		}
		return ResponseMsg.fail("");
	}
}
