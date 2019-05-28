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

import com.dlu.ghh.bean.SecretMsg;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.service.SecretService;
import com.dlu.ghh.utils.IntegerUtils;
import com.dlu.ghh.utils.ResponseMsg;
import com.dlu.ghh.utils.SessionUtils;
import com.dlu.ghh.utils.StringUtil;

@Controller
@RequestMapping("secret")
public class SecretController {

	private Logger logger = Logger.getLogger(SecretController.class);
	@Autowired
	private SecretService secretService;
	
	@ResponseBody
	@RequestMapping(value="sendSecretMsg",method=RequestMethod.POST)
	public ResponseMsg sendSecretMsg(@RequestParam(value="receverId",required=false)Integer receiverId,
			@RequestParam(value="msg",required=false)String msg,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("登录后才能发送私信!");
		}
		if(!IntegerUtils.isEmpty(receiverId) && !StringUtil.isEmpty(msg)) {
			if(secretService.sendSecretMsg(receiverId,msg,(User) session.getAttribute("user"))) {
				logger.info("发私信给id为"+receiverId+"的用户");
				return ResponseMsg.success("发送成功!");
			}
		}
		return ResponseMsg.fail("发送失败!");
	}
	
	@ResponseBody
	@RequestMapping(value="getMySecretMsg",method=RequestMethod.GET)
	public ResponseMsg getMySecretMsg(@RequestParam(value="userId",required=false)Integer userId) {
		if(IntegerUtils.isEmpty(userId)) {
			return ResponseMsg.fail("");
		}
		List<SecretMsg> secretMsgs = secretService.getByUserId(userId);
		return ResponseMsg.success("").add("secretMsgs", secretMsgs);
	}
}
