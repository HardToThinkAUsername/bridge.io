package com.dlu.ghh.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dlu.ghh.bean.ParamConfiguration;
import com.dlu.ghh.bean.Post;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.service.PostService;
import com.dlu.ghh.service.UserConcernService;
import com.dlu.ghh.service.UserService;
import com.dlu.ghh.utils.IOUtils;
import com.dlu.ghh.utils.IntegerUtils;
import com.dlu.ghh.utils.ResponseMsg;
import com.dlu.ghh.utils.SessionUtils;
import com.dlu.ghh.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping
public class IndexController {
	
	@RequestMapping("index")
	public ModelAndView toIndex(HttpSession session) {
		Object object = session.getAttribute("user");
		ModelAndView modelAndView = new ModelAndView();
		if(object != null && object instanceof User) {
			User user = (User)object;
			session.setAttribute("user", user);//刷新user
		}
		modelAndView.setViewName("index");
		return modelAndView;
	}
	
	@RequestMapping(value="foot")
	public ModelAndView getFoot() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("foot.html");
		return modelAndView;
	}
	
	@RequestMapping(value="header")
	public ModelAndView getHeader() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("header.html");
		return modelAndView;
	}
	
	@Autowired
	private ParamConfiguration pc;
	@ResponseBody
	@RequestMapping(value="index/getUserAggrement",method=RequestMethod.GET)
	public ResponseMsg getUserAggrement() {
		String text = IOUtils.readFile(new File(pc.getAgreementAddress()));
		return ResponseMsg.success("").add("msg", text);
	}
	
	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;
	@Autowired
	private UserConcernService userConcernService;
	
	@RequestMapping(value="index/toUserPage")
	public ModelAndView toUserPage(@RequestParam(value="id",required=false)Integer userId,
			HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		if(!IntegerUtils.isEmpty(userId)) {
			modelAndView.setViewName("user");
			User user = userService.getById(userId);
			List<Post> list = postService.getPostByUserId(userId);
			for(Post p : list) {
				String text = StringUtil.getTextFromHtml(p.getPostBody());
				if(text.length() > 100) {
					text = text.substring(0,100)+"...";
				}
				p.setPostBody(text);
			}
			Integer count = 0;
			if(list != null) {
				count = list.size();
			}
			//如果用户已登录  加入sessionUser 否则为null
			if(SessionUtils.sessionValidate(session)) {
				//判断是否已经关注
				User sessionUser = (User) session.getAttribute("user");
				boolean b = userConcernService.alreadyConcern(sessionUser.getId(),userId);
				modelAndView.addObject("alreadyConcern", b);
			}else {
				modelAndView.addObject("alreadyConcern", false);
			}
			modelAndView.addObject("user", user).addObject("postCount", count).addObject("posts", list);
			return modelAndView;
		}
		modelAndView.setViewName("error");
		return modelAndView; 
	}
	
	/**
	 * 跳转到管理中心
	 */
	@RequestMapping(value="/index/toManagePage")
	public ModelAndView toManagePage(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		if(!SessionUtils.sessionValidate(session)) {
			modelAndView.setViewName("redirect:/index");
			return modelAndView;
		}
		try {
			User user = (User)session.getAttribute("user");
			PageHelper.startPage(1, 10);
			List<Post> list = postService.getNormalAndSealed();
			PageInfo<Post> pageInfo = new PageInfo<>(list);
			modelAndView.addObject("user", user).addObject("pageInfo", pageInfo);
			modelAndView.setViewName("/manage");
		} catch (Exception e) {
			modelAndView.setViewName("redirect:/error");
		}
		return modelAndView;
	}
	
}
