package com.dlu.ghh.controller;


import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dlu.ghh.bean.Image;
import com.dlu.ghh.bean.ParamConfiguration;
import com.dlu.ghh.bean.Permission;
import com.dlu.ghh.bean.Role;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.service.ImageService;
import com.dlu.ghh.service.RolePermissionService;
import com.dlu.ghh.service.UserService;
import com.dlu.ghh.utils.DateUtils;
import com.dlu.ghh.utils.ResponseMsg;
import com.dlu.ghh.utils.SessionUtils;
import com.dlu.ghh.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value="/user")
public class UserController {
	private Logger logger = Logger.getLogger(UserController.class);
	private static final long ONEDAY = 24*60*60*1000;//一天的毫秒数
	
	@Autowired
	private UserService userService;
	@Autowired
	private ParamConfiguration paramConfiguration;
	@Autowired 
	private ImageService imageService;
	private final static String PATH = "http://localhost:8080/resources/user";
	/*
	 * 注册,修改用户后端校验
	 */
	@ResponseBody 
	@RequestMapping(value="save", method=RequestMethod.POST)							  
	public ResponseMsg userSave(String confirmPassword,String agree,String username,String password,
			String phoneNum,String address,String email,HttpSession session,String id,String type,
			@RequestParam(defaultValue="0",required=false)int disable,
			@RequestParam(defaultValue="0",required=false)int role,
			@RequestParam(required=false)int gender) {
		String usernameReg = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]+${3,16}";//用户名的正则验证式
		String passwordReg = "^[a-zA-Z0-9_\\.]+${6,30}";//密码的正则验证式
		String phoneNumReg = "^1[3|5|8]{1}[0-9]{9}${11,11}";//手机号码的正则验证式
		String emailReg = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";//邮箱的正则验证式
		if(username == null || password == null ||("register".equals(type) && confirmPassword == null)) {//username,password,confirmPassword有一个为null
			return ResponseMsg.fail("必填项不能为空!");//返回失败
		}else {
			if(username.trim().equals("") || password.trim().equals("") || ("register".equals(type) && confirmPassword.trim().equals(""))) {//username,password,confirmPassword有一个为空字符串
				return ResponseMsg.fail("必填项不能为空!");//返回失败
			}else if("register".equals(type) && !password.equals(confirmPassword)) {//password与confirmPassword不一致
				return ResponseMsg.fail("密码和确认密码不一致!");//返回失败
			}else if(!Pattern.matches(usernameReg, username) || !Pattern.matches(passwordReg, password)) {//username或password不合法
				return ResponseMsg.fail("用户名不合法!");//返回失败
			}
		}
		if(phoneNum != null && !Pattern.matches(phoneNumReg, phoneNum)) {//手机号码不合法
			return ResponseMsg.fail("手机号码不能为空!");//返回失败
		}
		if(email != null && !Pattern.matches(emailReg, email)) {
			return ResponseMsg.fail("邮箱不能为空!");//返回失败
		}
		if("register".equals(type) && agree != null && !agree.equals("on")) {//未勾选同意用户协议 
			return ResponseMsg.fail("请确认已阅读用户协议!");//返回失败
		}
		if("register".equals(type)) {//注册
			logger.info("注册了新用户-用户名为"+username);
			return userService.userRegister(username, password, email, address, phoneNum,new Date(),gender)>0? ResponseMsg.success("注册成功!"):ResponseMsg.fail("注册失败!");
		}else if("update".equals(type)) {//修改
			if(userService.userUpdate(id,username,password,email,address,phoneNum,disable,role,gender)) {//修改成功
				User user = userService.getById(Integer.parseInt(id));
				session.setAttribute("user", user);
				logger.info("修改了个人信息-新的信息为:"+user.toString());
				return ResponseMsg.success("修改成功!");
			}else {
				return ResponseMsg.fail("登录失败!");
			}
		}else 
			return ResponseMsg.fail("请确认用户名和密码是否正确!");
	}
	
	/**
	 * 获取登录用户信息
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getSessionUser",method=RequestMethod.GET)
	public ResponseMsg getSessionUser(HttpSession session) {
		User user = null;
		Object object = session.getAttribute("user");
		if(object != null && object instanceof User) {
			user = (User)object;
			if(user.getLastSigninDate() == null) 
				return ResponseMsg.success("").add("user", user).add("hasSignin", 0);//标志未签到
			if(DateUtils.compare(user.getLastSigninDate(), new Date()) < ONEDAY) //如果上次签到的时间到现在不到一天
				return ResponseMsg.success("").add("user", user).add("hasSignin", 1);//标志已经签到
			else//反之
				return ResponseMsg.success("").add("user", user).add("hasSignin", 0);//标志未签到
		}
		return ResponseMsg.fail("请先登录");
	}
	
	/**
	 * 用户名是否存在
	 * @param username
	 * @param type
	 * @param oldUsername
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="usernameExist",method=RequestMethod.POST)
	public String usernameExist(String username,String type,String oldUsername) {
		JSONObject jsonObject = new JSONObject();
		if("login".equals(type)) {
			boolean b = userService.selectByUsername(username)!=null?true:false;
			try {
				jsonObject.put("valid", b);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if("register".equals(type)) {
			boolean b = userService.selectByUsername(username)==null?true:false;
			try {
				jsonObject.put("valid", b);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if("update".equals(type)) {
			if(!StringUtil.isEmpty(username) && username.equals(oldUsername)) {
				try {
					jsonObject.put("valid", true);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if(!StringUtil.isEmpty(username) && !username.equals(oldUsername)){
				boolean b = userService.selectByUsername(username)==null?true:false;
				try {
					jsonObject.put("valid", b);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonObject.toString();
	}
	
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="login",method=RequestMethod.POST)
	public ResponseMsg userLogin(String username,String password,HttpSession session) {
		User user = userService.selectByUsername(username);//根据用户名查到用户
		if(!StringUtil.isEmpty(username) && !StringUtil.isEmpty(password) && user != null && password.equals(user.getPassword())) {
			session.setAttribute("user", user);//向session中添加user
			user.setUserExperience(user.getUserExperience() + paramConfiguration.getLoginExperience());//用户经验值增加 
			user.setLastLoginDate(new Date());
			userService.updateUserSelectiveByPrimary(user);//修改最后一次登录日期
			logger.info("用户: "+username+"登入系统");
			return ResponseMsg.success("登录成功").add("user", user);
		}
		return ResponseMsg.fail("密码错误");
	}
	
	/**
	 *   跳转到用户中心
	 * @param session
	 * @return
	 */
	@RequestMapping(value="personCenter")
	public ModelAndView personCenter(HttpSession session) {
		Object object = session.getAttribute("user");
		ModelAndView modelAndView = new ModelAndView();
		if(object != null && object instanceof User) {
			modelAndView.setViewName("person-center");
		}else {
			modelAndView.setViewName("index");
		}
		return modelAndView;
	}
	
	/**
	 * 退出登录
	 * @param session
	 * @return
	 */
	@RequestMapping(value="logout")
	public ModelAndView userLogout(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Object object = session.getAttribute("user");
		if(object != null && object instanceof User) {
			logger.info("退出登录");
			session.invalidate();
		}
		modelAndView.setViewName("redirect:/index");
		return modelAndView;
	}
	
	/**
	 * 切换头像
	 * @param file
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="changeProfilePhoto",method=RequestMethod.POST)
	public ResponseMsg changeProfilePhoto(@RequestParam(value="file",required=false) MultipartFile file,HttpSession session) {
		if(!file.isEmpty()){
			User user = null;
			if(SessionUtils.sessionValidate(session)) {//检测session是否有效
				user = (User)session.getAttribute("user");//将session中的属性user强制转化为User类型
			}
			//将用户头像上传到服务器指定磁盘下
			String filePath = paramConfiguration.getUploadFolder()+ "\\user\\"+user.getId();//获取文件在磁盘中存储的路径
			String fileName = file.getOriginalFilename();//获取上传图片名称
			//以上传时间给图片命名（在服务器磁盘上的名称
			File fileFile = new File(filePath,fileName);//获取到文件对象
			if(!fileFile.getParentFile().exists()) {//如果文件的父文件夹不存在
				fileFile.getParentFile().mkdirs();//自动创建缺少的父文件夹
			}
			try {
				//保存图片
				file.transferTo(fileFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//将上传后的图片名称赋值给模型
			Image image = new Image();
			image.setPath(PATH+"/"+user.getId()+"/"+fileName);
			
			List<Image> list = imageService.selectByPath(PATH+"/"+user.getId()+"/"+fileName);
			//如果img_tb中有当前图片
			if(list != null && list.size() > 0) {
				user.setUserImage(list.get(0));//将查到的图片的id赋值给user的user_img_id
			}else {//如果img_tb中午当前图片
				imageService.insert(image);//先插入该图片记录   输出自动回填的主键
				user.setUserImage(image);//将该记录的id赋值给user的user_img_id
			}
			if(userService.updateUserSelectiveByPrimary(user)) {
				logger.info("修改了个人头像");
				return ResponseMsg.success("头像已更改!").add("path", PATH+"/"+user.getId()+"/"+fileName);
			}
		}
		return ResponseMsg.fail("头像更改失败!");
	}
	
	/**
	 * 用户签到
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping(value="signin",method=RequestMethod.POST)
	public ResponseMsg signin(HttpSession session) {
		User user = null;
		Date date = new Date();
		if(SessionUtils.sessionValidate(session)) {
			user = (User)session.getAttribute("user");
		}else {
			return ResponseMsg.fail("请刷新页面并确保已经登录登录!");
		}
		if(user.getLastSigninDate() == null) {//第一次签到
			user.setUserExperience(user.getUserExperience()+10);// 经验值加10
			user.setContinuousSigninTimes(1);//设置连续签到的次数加一
			user.setLastSigninDate(date);//设置上次签到的日期
			userService.updateUserSelectiveByPrimary(user);//更新user
			return ResponseMsg.success("签到成功").add("time", user.getContinuousSigninTimes());
			
		}
		long dis = DateUtils.compare(user.getLastSigninDate(), date);
		if(dis >= ONEDAY && dis < 2*ONEDAY) {// 如果距离上次签到时间超过1天 并且小于两天的 
			Integer times = user.getContinuousSigninTimes();
			if(times < 7 && times >= 1) {// 连续签到次数1 - 7 
				user.setUserExperience(user.getUserExperience()+10);// 经验值加10
			}else if(times < 15 && times >= 7) {// 连续签到次数小于7 - 15 
				user.setUserExperience(user.getUserExperience()+50);// 经验值加50
			}else if(times < 31 && times >= 15) {// 连续签到次数小于15 - 31
				user.setUserExperience(user.getUserExperience()+100);//经验值加100
			}
			user.setContinuousSigninTimes(times+1);//设置连续签到的次数加一
			user.setLastSigninDate(date);//设置上次签到的日期
			userService.updateUserSelectiveByPrimary(user);//更新user
			logger.info("用户签到");
			return ResponseMsg.success("签到成功").add("time", user.getContinuousSigninTimes());
		}else if(dis >= 2*ONEDAY || date.getDay() == 1){// 如果距离上次签到时间超过两天  或者是这个每个月的第一天的
			user.setContinuousSigninTimes(1);
			user.setLastSigninDate(date);
			userService.updateUserSelectiveByPrimary(user);
			return ResponseMsg.success("签到成功!").add("time", 1);
		}else {
			return ResponseMsg.fail("今日已签到!").add("time", user.getContinuousSigninTimes());
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="shutup",method=RequestMethod.POST)
	public ResponseMsg shutup(@RequestParam(value="userId")Integer userId,HttpSession session) {
		//判断是否登录
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先登录!");
		}
		User oprator = (User) session.getAttribute("user");
		//判断有无权限
		if(oprator.getRole().getId() <= 0) {
			return ResponseMsg.fail("你没有此项权限!");
		}
		User shutUper = userService.getById(userId);
		//判断权限够不够
		if(oprator.getRole().getId() <= shutUper.getRole().getId()) {
			return ResponseMsg.fail("你的权限不够!");
		}
		shutUper.setShutup(1);
		if(userService.updateUserSelectiveByPrimary(shutUper)) {
			logger.info("用户"+shutUper.getUsername()+"被"+oprator.getUsername()+"禁言");
			return ResponseMsg.success("");
		}
		return ResponseMsg.fail("禁言失败!");
	}
	
	@ResponseBody
	@RequestMapping(value="getAllForManage",method=RequestMethod.GET)
	public ResponseMsg getAllForManage(@RequestParam(value="pageNum",required=false,defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="keywords",required=false)String keywords,
			@RequestParam(value="type",required=false)Integer type) {
		if(type == 0) {//无条件  查询全部
			PageHelper.startPage(pageNum, pageSize);
			List<User> list = userService.getAll();
			PageInfo<User> pageInfo = new PageInfo<>(list);
			return ResponseMsg.success("").add("pageInfo", pageInfo);
		}
		if(StringUtil.isEmpty(keywords)) {//关键词
			return ResponseMsg.fail("关键词输入不能为空!");
		}
		User user = new User();
		if(type == 1) {//用户id
			int id;
			try {
				id = Integer.parseInt(keywords);
				user.setId(id);
			} catch (NumberFormatException e) {
				return ResponseMsg.fail("非法输入!请输入整型字符!");
			}
		}
		if(type == 2){//用户名字
			user.setUsername(keywords);
		}
		if(type== 3) {//用户地址
			user.setAddress(keywords);
		}
		if(type == 4) {
			Role role = new Role();
			if("吧务".equals(keywords.trim())) {
				role.setId(1);
			}else if("小吧主".equals(keywords.trim())) {
				role.setId(2);
			}else if("吧主".equals(keywords.trim())) {
				role.setId(3);
			}else if("超级管理员".equals(keywords.trim())) {
				role.setId(4);
			}else if("一般用户".equals(keywords.trim())) {
				role.setId(0);
			}
			user.setRole(role);
		}
		PageHelper.startPage(pageNum, pageSize);
		List<User> list = userService.selectSelectiveFuzzy(user);
		PageInfo<User> pageInfo = new PageInfo<>(list);
		return ResponseMsg.success("").add("pageInfo", pageInfo);
	}
	
	@Autowired
	private RolePermissionService rolePermissionService;
	/**
	 * 删除用户
	 */
	@ResponseBody
	@RequestMapping(value="removeUser",method=RequestMethod.POST)
	public ResponseMsg removeUser(@RequestParam(value="id",required=false)Integer userId,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.USER_DELETE, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(userService.deleteById(userId)) {
			logger.info("用户id为"+userId+"被禁言");
			return ResponseMsg.success("删除成功!");
		}
		return ResponseMsg.fail("删除失败!");
	}
	
	/**
	 * 禁言/取消禁言用户
	 * @param userId
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="shutupUser",method=RequestMethod.POST)
	public ResponseMsg shutupUser(@RequestParam(value="id",required=false)Integer userId,
			@RequestParam(value="shutup")Integer shutup,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.USER_SHUT_UP, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		User u = new User();
		u.setId(userId);
		u.setShutup(shutup);
		if(userService.updateUserSelectiveByPrimary(u)) {
			if(shutup == 1) {
				logger.info("id为"+userId+"的用户被禁言");
				return ResponseMsg.success("禁言成功!");
			}else {
				logger.info("id为"+userId+"的用户被取消禁言");
				return ResponseMsg.success("取消禁言成功!");
				
			}
		}
		if(shutup == 1) {
			return ResponseMsg.fail("禁言失败!");
		}else {
			return ResponseMsg.fail("取消禁言失败!");
		}
	}
	
	/**
	 * 禁言多个用户
	 * @param userId
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="shutupUserMultiple",method=RequestMethod.POST)
	public ResponseMsg shutupUserMultiple(@RequestParam(value="ids",required=false)String userIds,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.USER_SHUT_UP, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(userService.shutupMultiple(userIds)) {
			logger.info("id为["+userIds+"]的所有用户被禁言");
			return ResponseMsg.success("禁言成功!");
		}
		return ResponseMsg.fail("禁言失败!");
	}
	
	/**
	 * 删除多个用户
	 */
	@ResponseBody
	@RequestMapping(value="removeUserMultiple",method=RequestMethod.POST)
	public ResponseMsg removeUserMultiple(@RequestParam(value="ids",required=false)String userIds,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.USER_DELETE, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(userService.deleteByIds(userIds)) {
			logger.info("id为["+userIds+"]的所有用户被删除");
			return ResponseMsg.success("");
		}
		return ResponseMsg.fail("删除失败!");
	}
	
	/**
	 * 改变用户角色
	 */
	@ResponseBody
	@RequestMapping(value="changeRole",method=RequestMethod.POST)
	public ResponseMsg changeRole(@RequestParam(value="roleId",required=false)Integer roleId,
			@RequestParam(value="userId")Integer userId,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User oprator = (User)session.getAttribute("user");
		if(oprator.getRole().getId() != 4) {//不是超级管理员
			return ResponseMsg.fail("你不具有该权限!");
		}
		User changer = userService.getById(userId);
		Role role = changer.getRole();
		if(roleId != -1) {
			role.setId(roleId);
			changer.setRole(role);
			System.out.println("==="+roleId);
			if(userService.updateUserSelectiveByPrimary(changer)) {
				return ResponseMsg.success("");
			}
		}
		return ResponseMsg.fail("修改用户角色失败!");
	}
	
}
