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

import com.dlu.ghh.bean.User;
import com.dlu.ghh.bean.UserConcern;
import com.dlu.ghh.service.UserConcernService;
import com.dlu.ghh.utils.IntegerUtils;
import com.dlu.ghh.utils.ResponseMsg;
import com.dlu.ghh.utils.SessionUtils;

@Controller
@RequestMapping("userConcern")
public class UserConcernContrller {
	private Logger logger = Logger.getLogger(UserConcernContrller.class);
	
	@Autowired
	private UserConcernService userConcernService;
	
	/**
	 * 关注别人 
	 * @param concernUserId
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="concernOther",method=RequestMethod.POST)
	public ResponseMsg concernOther(@RequestParam(value="id",required=false)Integer concernUserId,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("登录后才能关注!");
		}
		User user = (User) session.getAttribute("user");
		if(user.getId().intValue() == concernUserId.intValue()) {
			return ResponseMsg.fail("不可以关注自己哦!");
		}
		if(!IntegerUtils.isEmpty(concernUserId) && userConcernService.concernOther(user.getId(),concernUserId)) {
			logger.info("关注了id为"+concernUserId+"的用户");
			return ResponseMsg.success("");
		}
		return ResponseMsg.fail("关注失败!");
	}
	/**
	 * 取消关注
	 */
	
	@ResponseBody
	@RequestMapping(value="cancelConcern",method=RequestMethod.POST)
	public ResponseMsg cancelConcern(@RequestParam(value="id",required=false)Integer concernUserId,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("登录后才能取消关注!");
		}
		User user = (User) session.getAttribute("user");
		if(!IntegerUtils.isEmpty(concernUserId) && userConcernService.cancelConcern(user.getId(),concernUserId)) {
			logger.info("取关了id为"+concernUserId+"的用户");
			return ResponseMsg.success("");
		}
		return ResponseMsg.fail("取消关注失败!");
	}
	
	/**
	 * 查看自己关注的人
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getMyConcern",method=RequestMethod.GET)
	public ResponseMsg getMyConcern(@RequestParam(value="userId",required=false)Integer userId) {
		if(IntegerUtils.isEmpty(userId)) {
			return ResponseMsg.fail("");
		}
		List<UserConcern> concerns = userConcernService.getByUserId(userId);
		return ResponseMsg.success("").add("concerns", concerns);
	}
	
	
}
