package com.dlu.ghh.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dlu.ghh.bean.Floor;
import com.dlu.ghh.bean.KeyWord;
import com.dlu.ghh.bean.Permission;
import com.dlu.ghh.bean.Post;
import com.dlu.ghh.bean.PostKeyword;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.service.FloorService;
import com.dlu.ghh.service.PostKeywordService;
import com.dlu.ghh.service.PostService;
import com.dlu.ghh.service.RolePermissionService;
import com.dlu.ghh.utils.IntegerUtils;
import com.dlu.ghh.utils.ResponseMsg;
import com.dlu.ghh.utils.SessionUtils;
import com.dlu.ghh.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/post")
public class PostingController {
	
	private Logger logger = Logger.getLogger(PostingController.class);
	
	@Autowired
	private PostService postService;
	@Autowired
	private FloorService floorService;
	
	@Autowired
	private PostKeywordService postKeywordService;
	/**
	 * 发帖
	 * @param postType
	 * @param postTitle
	 * @param postBody
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="releasePost",method=RequestMethod.POST)
	public ResponseMsg releasePost(@RequestParam(value="postType") Integer postType,
			@RequestParam(value="postTitle") String postTitle,
			@RequestParam(value="postBody") String postBody,
			@RequestParam(value="keywords") String keywords,
			@RequestParam(value="century") Integer century,
			HttpSession session) {
		//获取session中的user
		User user = null;
		Object object = session.getAttribute("user");
		if(StringUtil.isEmpty(keywords)) {//未选关键词
			return ResponseMsg.fail("请选择关键词!");
		}
		if(object != null && object instanceof User) {
			user = (User)object;
			if(user.getShutup() == 1) {//禁言
				return ResponseMsg.fail("你已经被禁言,无法发帖!");
			}
			String newPostBody = postBody.replace("\"", "'");//把帖子中的双引号替换为单引号
			Post post = new Post();
			post.setPostBody(newPostBody);
			post.setDate(new Date());
			post.setPostTitle(postTitle);
			post.setUser(user);
			post.setType(postType);
			post.setCentury(century);
			boolean b = postService.insertPost(post);//向数据库中插入记录  返回boolean类型
			if(b == true) {//如果插入成功
				logger.info("发布了帖子");
				//添加帖子关键词
				String[] strings = keywords.split(",");
				PostKeyword postKeyword = null;
				for(String s:strings) {
					postKeyword = new PostKeyword();
					if(!StringUtil.isEmpty(s)) {//s不为空
						try {
							int keyword = Integer.parseInt(s);
							postKeyword.setKeyword(keyword);
							postKeyword.setPost(post);
							postKeywordService.insert(postKeyword);
						} catch (NumberFormatException e) {
							return ResponseMsg.fail("发帖失败!");
						}
					}
				}
				return ResponseMsg.success("发帖成功!");
			}
		}else {
			return ResponseMsg.fail("请先登录!");
		}
		return ResponseMsg.fail("发帖失败!");
	}
	
	/**
	 * 获取首页初始化 热帖
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value="getHotPostInit",method=RequestMethod.GET)
	public ResponseMsg getHotPost(@RequestParam(value="pageSize",required=false,defaultValue="3")Integer pageSize,
			@RequestParam(value="pageNum",required=false,defaultValue="0")Integer pageNum) {
		PageHelper.startPage(pageNum, pageSize);//开启分页查询
		List<Post> posts = postService.getHotPost();//获取热门帖子  根据赞的数量排名
		PageInfo<Post> pageInfo = new PageInfo<>(posts);
		if(posts != null) {//如果查到了帖子
			Map<String, Post> map = resolvePostList(posts);// 因为帖子的展示顺序根据被赞的次数递减排序, 所以这里用有序map  LinkedHashMap
			return ResponseMsg.success("").add("posts", map).add("pageInfo", pageInfo);
		}
		return ResponseMsg.fail("加载数据失败, 请刷新页面重试!");
	}
	
	/**
	 * 获取首页初始化 求助类帖子
	 */
	@ResponseBody
	@RequestMapping(value="getHelpPostInit",method=RequestMethod.GET)
	public ResponseMsg getHelpPost(@RequestParam(value="pageSize",required=false,defaultValue="3")Integer pageSize,
			@RequestParam(value="pageNum",required=false,defaultValue="0")Integer pageNum) {
		PageHelper.startPage(pageNum, pageSize);//开启分页查询
		List<Post> helpPost = postService.getHelpPost();//获取求助类帖子 根据赞的数量排名
		PageInfo<Post> pageInfo = new PageInfo<>(helpPost);
		if(helpPost != null) {
			Map<String,Post> map = resolvePostList(helpPost);// 因为帖子的展示顺序根据被赞的次数递减排序, 所以这里用有序map  LinkedHashMap
			return ResponseMsg.success("").add("helpPost", map).add("pageInfo", pageInfo);
		}
		return ResponseMsg.fail("加载数据失败, 请刷新页面重试!");
	}
	
	/**
	 * 获取首页初始化 介绍类帖子
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getIntroPostInit",method=RequestMethod.GET)
	public ResponseMsg getIntroPost(@RequestParam(value="pageSize",required=false,defaultValue="3")Integer pageSize,
			@RequestParam(value="pageNum",required=false,defaultValue="0")Integer pageNum) {
		PageHelper.startPage(pageNum, pageSize);//开启分页查询
		List<Post> introPost = postService.getIntroPost();//获取求助类帖子 根据赞的数量排名
		PageInfo<Post> pageInfo = new PageInfo<>(introPost);
		if(introPost != null) {
			Map<String,Post> map = resolvePostList(introPost);// 因为帖子的展示顺序根据被赞的次数递减排序, 所以这里用有序map  LinkedHashMap
			return ResponseMsg.success("").add("introPost", map).add("pageInfo", pageInfo);
		}
		return ResponseMsg.fail("加载数据失败, 请刷新页面重试!");
	}

	/**
	 * 将post集合封装成map集合   key: 图片的地址  value: post
	 */
	private Map<String, Post> resolvePostList(List<Post> list){
		Map<String, Post> map = new LinkedHashMap<>();
		int i = 0;
		for(Post p:list) {
			if("".equals(StringUtil.getImgSrc(p.getPostBody()))) {//如果没有图片
				i++;
				map.put(""+i, p);//key为任意值
			}else
				map.put(StringUtil.getImgSrc(p.getPostBody()), p);//把帖子中的图片作为帖子的大头像
		}
		for(Map.Entry<String, Post> entry : map.entrySet()) {//处理map值Post的postBody
			entry.getValue().setPostBody(StringUtil.getTextFromHtml(entry.getValue().getPostBody()));
		}
		return map;
	}
	
	
	/**
	 * 获取所有介绍类帖子
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getIntroPostAll",method=RequestMethod.GET)
	public ResponseMsg getIntroPostAll(@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNum",required=false,defaultValue="0")Integer pageNum) {
		PageHelper.startPage(pageNum, pageSize);
		List<Post> introPost = postService.getIntroPost();
		PageInfo<Post> pageInfo = new PageInfo<>(introPost);
		if(introPost != null) {
			for(Post p:introPost) {
				p.setPostBody(StringUtil.getTextFromHtml(p.getPostBody()));//提取标签中的内容, 去掉所有的格式
			}
			pageInfo.setList(introPost);//重置pageInfo中的list
			return ResponseMsg.success("").add("pageInfo", pageInfo);
		}
		return ResponseMsg.fail("加载数据失败, 请刷新页面重试!");
	}
	
	/**
	 * 获取所有求助类帖子
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getHelpPostAll",method=RequestMethod.GET)
	public ResponseMsg getHelpPostAll(@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNum",required=false,defaultValue="0")Integer pageNum) {
		PageHelper.startPage(pageNum, pageSize);
		List<Post> helpPost = postService.getHelpPost();
		PageInfo<Post> pageInfo = new PageInfo<>(helpPost);
		if(helpPost != null) {
			for(Post p:helpPost) {
				p.setPostBody(StringUtil.getTextFromHtml(p.getPostBody()));//提取标签中的内容, 去掉所有的格式
			}
			pageInfo.setList(helpPost);//重置pageInfo中的list
			return ResponseMsg.success("").add("pageInfo", pageInfo);
		}
		return ResponseMsg.fail("加载数据失败, 请刷新页面重试!");
	}
	
	/**
	 * 获取所有热帖
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getHotPostAll",method=RequestMethod.GET)
	public ResponseMsg getHotPostAll(@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNum",required=false,defaultValue="0")Integer pageNum) {
		PageHelper.startPage(pageNum, pageSize);
		List<Post> hotPost = postService.getHotPost();
		PageInfo<Post> pageInfo = new PageInfo<>(hotPost);
		if(hotPost != null) {
			for(Post p:hotPost) {
				p.setPostBody(StringUtil.getTextFromHtml(p.getPostBody()));//提取标签中的内容, 去掉所有的格式
			}
			pageInfo.setList(hotPost);//重置pageInfo中的list
			return ResponseMsg.success("").add("pageInfo", pageInfo);
		}
		return ResponseMsg.fail("加载数据失败, 请刷新页面重试!");
	}
	
	
	/**
	 *   模糊查询
	 * @param pageSize 
	 * @param pageNum
	 * @param content 
	 * @param type 0: 查询全部类型的帖子  1: 介绍类   2:查资料(此函数中不存在) 3: 求助类帖子  4:热帖  5:根据帖子类型(关键字)查询 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="fuzzyQuery",method=RequestMethod.GET)
	public ResponseMsg fuzzyQuery(@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNum",required=false,defaultValue="0")Integer pageNum,
			@RequestParam(value="content",required=false,defaultValue="")String content,
			@RequestParam(value="type")Integer type) {
//		PageInfo<Post> pageInfo = getPageInfoFuzzyQuery(pageNum, pageSize, type, content);
		StringBuffer stringBuffer = new StringBuffer("%");
		stringBuffer.append(content).append("%");
		List<Post> list = null;
		PageInfo<Post> pageInfo = null;
		if(type == 0) {//全部类型的
			PageHelper.startPage(pageNum, pageSize);
			list = postService.getAllTypePostFuzzy(stringBuffer.toString());
			pageInfo = new PageInfo<>(list);
		}else if(type == 1) {//介绍类帖子
			PageHelper.startPage(pageNum, pageSize);
			list = postService.getIntroPostFuzzy(stringBuffer.toString());
			pageInfo = new PageInfo<>(list);
		}else if(type == 3) {//求助类帖子
			PageHelper.startPage(pageNum, pageSize);
			list = postService.getHelpPostFuzzy(stringBuffer.toString());
			pageInfo = new PageInfo<>(list);
		}else if(type == 4) {//热帖
			PageHelper.startPage(pageNum, pageSize);
			list = postService.getHotPostFuzzy(stringBuffer.toString());
			pageInfo = new PageInfo<>(list);
		}
		if(list != null) {
			for(Post p:list) {
				p.setPostBody(StringUtil.getTextFromHtml(p.getPostBody()));
			}
			pageInfo.setList(list);
		}
		if(pageInfo != null) {
			return ResponseMsg.success("").add("pageInfo", pageInfo);
		}
		return ResponseMsg.fail("加载数据失败, 请刷新页面重试!");
	}
	
	private List<Post> listFuzzyQuery(Integer type,String content){
		StringBuffer stringBuffer = new StringBuffer("%");
		stringBuffer.append(content).append("%");
		List<Post> list = null;
		if(type == 0) {
			list = postService.getAllTypePostFuzzy(stringBuffer.toString());
		}else if(type == 1) {//介绍类帖子
			list = postService.getIntroPostFuzzy(stringBuffer.toString());
		}else if(type == 3) {//求助类帖子
			list = postService.getHelpPostFuzzy(stringBuffer.toString());
		}else if(type == 4) {//热帖
			list = postService.getHotPostFuzzy(stringBuffer.toString());
		}
		if(list != null) {
			for(Post p:list) {
				p.setPostBody(StringUtil.getTextFromHtml(p.getPostBody()));
			}
		}
		return list;
	}
	
	/**
	 * 根据用户id查询帖子的数量
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getPostCountByUserId",method=RequestMethod.GET)
	public ResponseMsg getPostCountByUserId(@RequestParam(value="id",required=false)Integer id) {
		if(id != null && id instanceof Integer) {//id有效
			Integer countByUserId = postService.getCountByUserId(id);
			return ResponseMsg.success("").add("count", countByUserId);
		}
		return ResponseMsg.fail("");
	}
	
	/**
	 * 根据用户id查询post
	 * @param id
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getPostByUserId",method=RequestMethod.GET)
	public ResponseMsg getPostByUserId(@RequestParam(value="id",required=false)Integer id,
			@RequestParam(value="pageNum",required=false)Integer pageNum,
			@RequestParam(value="pageSize",required=false)Integer pageSize) {
		if(id != null && id instanceof Integer) {//id有效
			if(pageNum != null && pageSize != null) {
				PageHelper.startPage(pageNum, pageSize);
				List<Post> list = postService.getPostByUserId(id);
				PageInfo<Post> pageInfo = new PageInfo<>(list);
				for(Post p:list) {
					p.setPostBody(StringUtil.getTextFromHtml(p.getPostBody()));
				}
				pageInfo.setList(list);
				return ResponseMsg.success("").add("pageInfo", pageInfo);
			}else {
				List<Post> list = postService.getPostByUserId(id);
				for(Post p:list) {
					p.setPostBody(StringUtil.getTextFromHtml(p.getPostBody()));
				}
				logger.info("查看了id为"+id+"用户的帖子");
				return ResponseMsg.success("").add("list", list);
			}
		}
		return ResponseMsg.fail("");
	}
	
	/**
	 * 删除帖子
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteById",method=RequestMethod.POST)
	public ResponseMsg deletePostById(@RequestParam(value="id",required=false)Integer id) {
		if(id != null && id instanceof Integer) {
			if(postService.deleteById(id)) {//如果删除成功
				logger.info("id为"+id+"的帖子被删除");
				return ResponseMsg.success("删除成功!");
			}
		}
		return ResponseMsg.fail("删除失败!");
	}
	
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="userRead",method=RequestMethod.GET)
	public ResponseMsg getById(@RequestParam(value="id",required=false)Integer id,
			HttpSession session) {
		if(id != null && id instanceof Integer) {
			Post post = postService.userRead(id);
			List<Floor> floors = floorService.getByPostId(id);
			if(!SessionUtils.sessionValidate(session)) {
				return ResponseMsg.success("").add("post", post).add("floors", floors).add("sessionUser", "null");
			}else {
				User user = (User)session.getAttribute("user");
				return ResponseMsg.success("").add("post", post).add("floors", floors).add("sessionUser", user);
			}
		}
		return ResponseMsg.fail("加载帖子失败, 请刷新重试!");
	}
	
	/**
	 * 查看别人的帖子
	 */
	@ResponseBody
	@RequestMapping(value="queryUserPost",method=RequestMethod.GET)
	public ResponseMsg queryUserPost(@RequestParam(value="id",required=false)Integer userId) {
		List<Post> list = postService.getPostByUserId(userId);
		if(list != null) {
			for(Post p : list) {
				String text = StringUtil.getTextFromHtml(p.getPostBody());
				if(text.length() > 100) {
					text = text.substring(0, 100)+"...";
				}
				p.setPostBody(text);
			}
			logger.info("查看了id为"+userId+"用户的帖子");
			return ResponseMsg.success("").add("posts", list);
		}
		return ResponseMsg.fail("");
	}
	
	/**
	 * 帖子管理
	 * */
	@ResponseBody
	@RequestMapping(value="getAllForManage",method=RequestMethod.GET)
	public ResponseMsg getAll(@RequestParam(value="pageNum",required=false,defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="keywords",required=false)String keywords,
			@RequestParam(value="type",required=false)Integer type) {
		if(IntegerUtils.isEmpty(type)) {
			return ResponseMsg.fail("加载数据失败! 请刷新重试!");
		}
		if(type == 0) {//没有条件
			PageHelper.startPage(pageNum, pageSize);
			List<Post> all = postService.getNormalAndSealed();
			PageInfo<Post> pageInfo = new PageInfo<>(all);
			return ResponseMsg.success("").add("pageInfo", pageInfo);
		}
		if(StringUtil.isEmpty(keywords)) {
			return ResponseMsg.fail("关键词输入不能为空!");
		}
		Post post = new Post();
		if(type == 1) {//根据帖子id
			int id;
			try {
				id = Integer.parseInt(keywords);
			} catch (NumberFormatException e) {
				return ResponseMsg.fail("帖子的id应为整型字符, 请输入有效的关键词!");
			}
			post.setId(id);//设置帖子id
			
		}else if(type == 2) {//根据标题
			post.setPostTitle(keywords);
		}else if(type == 3) {//根据作者名
			User user = new User();
			user.setUsername(keywords);
			post.setUser(user);
		}
		PageHelper.startPage(pageNum, pageSize);
		List<Post> list = postService.getAllSelectiveFuzzy(post);
		PageInfo<Post> pageInfo = new PageInfo<>(list);
		return ResponseMsg.success("").add("pageInfo", pageInfo);
	}
	
	@Autowired
	private RolePermissionService rolePermissionService;
	/**
	 * 删除帖子
	 * @param postId
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="removePost",method=RequestMethod.POST)
	public ResponseMsg removePost(@RequestParam(value="id",required=false)Integer postId,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.POST_DELETE, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(postService.deleteById(postId)) {
			logger.info("删除id为"+postId+"帖子!");
			return ResponseMsg.success("删除成功!");
		}
		return ResponseMsg.fail("删除失败!");
	}
	
	/**
	 * 查封帖子
	 */
	@ResponseBody
	@RequestMapping(value="sealPost",method=RequestMethod.POST)
	public ResponseMsg sealPost(@RequestParam(value="id",required=false)Integer postId,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.POST_SEAL, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(postService.sealById(postId)) {
			logger.info("查封id为"+postId+"的帖子!");
			return ResponseMsg.success("");
		}
		return ResponseMsg.fail("查封失败!");
	}
	
	/**
	 * 解封帖子
	 */
	@ResponseBody
	@RequestMapping(value="unSealPost",method=RequestMethod.POST)
	public ResponseMsg unSealPost(@RequestParam(value="id",required=false)Integer postId,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.POST_SEAL, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(postService.unSealById(postId)) {
			logger.info("解封id为"+postId+"的帖子!");
			return ResponseMsg.success("");
		}
		return ResponseMsg.fail("查封失败!");
	}
	
	/**
	 * 查封多个帖子
	 */
	@ResponseBody
	@RequestMapping(value="sealPostMultiple",method=RequestMethod.POST)
	public ResponseMsg sealPostMultiple(@RequestParam(value="ids",required=false)String postIds,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.POST_SEAL, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(postService.sealByIds(postIds)) {
			logger.info("查封id为["+postIds+"]的所有帖子!");
			return ResponseMsg.success("");
		}
		return ResponseMsg.fail("查封失败!");
	}
	
	/**
	 * 删除多个帖子
	 */
	@ResponseBody
	@RequestMapping(value="removePostMultiple",method=RequestMethod.POST)
	public ResponseMsg removePostMultiple(@RequestParam(value="ids",required=false)String postIds,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.POST_DELETE, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(postService.deleteByIds(postIds)) {
			logger.info("删除id为["+postIds+"]的所有帖子!");
			return ResponseMsg.success("");
		}
		return ResponseMsg.fail("删除失败!");
	}
	
	/**
	 * 审阅帖子
	 * @param postId
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="reviewPost",method=RequestMethod.POST)
	public ResponseMsg reviewPost(@RequestParam(value="id",required=false)Integer postId,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.POST_REVIEW, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(postService.review(postId)) {
			logger.info("审阅并通过了id为["+postId+"]的帖子!");
			return ResponseMsg.success("");
		}
		return ResponseMsg.fail("审阅失败!");
	}
	
	/**
	 * 查看帖子详情
	 */
	@ResponseBody
	@RequestMapping(value="getPostManage",method=RequestMethod.GET)
	public ResponseMsg getPostManage(@RequestParam(value="id",required=false)Integer postId,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		Post post = postService.getById(postId);
		if(post != null) {
			//post.setPostBody(StringUtil.getTextFromHtml(post.getPostBody()));
			return ResponseMsg.success("").add("post", post);
		}else
			return ResponseMsg.fail("");
	}
	
	/**
	 * 根据桥梁类型查询
	 */
	/*
	 * pageNum:1,bridgeType:bridgeType,content:content,postType:postType*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value="getPostByKeyWord",method=RequestMethod.GET)
	public ResponseMsg getPostByKeyWord(@RequestParam(value="bridgeType",required=false)String bridgeType,
			@RequestParam(value="pageNum",required=false)Integer pageNum,
			@RequestParam(value="content",required=false)String content,
			@RequestParam(value="postType",required=false)Integer postType) {
		if(!StringUtil.isEmpty(bridgeType)) {
			Integer id = KeyWord.getKeyWordId(bridgeType);
			if(id == 0) {//桥梁类型未选择
				return ResponseMsg.fail("请选择桥梁类型!");
			}
			//PageInfo<Post> pageInfo4PostType = getPageInfo(pageNum, 10, postType, content);
			PageHelper.startPage(pageNum, 10);
			List<PostKeyword> list = postKeywordService.getByKeywordId(id);
			PageInfo pageInfo = new PageInfo<>(list);
			List<Post> posts = new ArrayList<>();
			for(PostKeyword postKeyword : list) {
				Post post = postKeyword.getPost();
				post.setPostBody(StringUtil.getTextFromHtml(post.getPostBody()));
				posts.add(post);
			}
			pageInfo.setList(posts);
			return ResponseMsg.success("").add("pageInfo", pageInfo).add("bridgeType", bridgeType);
		}
		return ResponseMsg.fail("查询出错,请刷新重试!");
	}
	
	private List<Post> listByKeyWord(String bridgeType) {
		if(!StringUtil.isEmpty(bridgeType)) {
			Integer id = KeyWord.getKeyWordId(bridgeType);
			if(id == 0) {//桥梁类型未选择
				return null;
			}
			List<PostKeyword> list = postKeywordService.getByKeywordId(id);
			List<Post> posts = new ArrayList<>();
			for(PostKeyword postKeyword : list) {
				Post post = postKeyword.getPost();
				post.setPostBody(StringUtil.getTextFromHtml(post.getPostBody()));
				posts.add(post);
			}
			return posts;
		}
		return null;
	}
	
	/**
	 * 查询待审阅帖子
	 */
	@ResponseBody
	@RequestMapping(value="getAllForReview",method=RequestMethod.GET)
	public ResponseMsg getAllForReview(@RequestParam(value="pageNum",required=false,defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="keywords",required=false)String keywords,
			@RequestParam(value="type",required=false)Integer type) {
		if(IntegerUtils.isEmpty(type)) {
			return ResponseMsg.fail("加载数据失败! 请刷新重试!");
		}
		if(type != 0 && StringUtil.isEmpty(keywords)) {
			return ResponseMsg.fail("关键词输入不能为空!");
		}
		Post post = new Post();
		if(type == 1) {//根据帖子id
			int id;
			try {
				id = Integer.parseInt(keywords);
			} catch (NumberFormatException e) {
				return ResponseMsg.fail("帖子的id应为整型字符, 请输入有效的关键词!");
			}
			post.setId(id);//设置帖子id
			
		}else if(type == 2) {//根据标题
			post.setPostTitle(keywords);
		}else if(type == 3) {//根据作者名
			User user = new User();
			user.setUsername(keywords);
			post.setUser(user);
		}
		PageHelper.startPage(pageNum, pageSize);
		List<Post> list = postService.getNotReview(post);
		PageInfo<Post> pageInfo = new PageInfo<>(list);
		return ResponseMsg.success("").add("pageInfo", pageInfo);
	}
	
	private List<Post> listByCentury(Integer century){
		if(century == 0) {
			return null;
		}
		List<Post> posts = postService.getByCentury(century);
		if(posts == null) {
			return null;
		}
		for(Post p:posts) {
			p.setPostBody(StringUtil.getTextFromHtml(p.getPostBody()));
		}
		return posts;
	}
	
	@ResponseBody
	@RequestMapping(value="getPostByCentury",method=RequestMethod.GET)
	public ResponseMsg getPostByCentury(@RequestParam(value="bridgeType",required=false)String bridgeType,
			@RequestParam(value="pageNum",required=false)Integer pageNum,
			@RequestParam(value="content",required=false)String content,
			@RequestParam(value="postType",required=false)Integer postType,
			@RequestParam(value="century",required=false)Integer century) {
		if(postType == 2) {//资料
			return ResponseMsg.fail("该搜索功能只支持帖子查询!");
		}
		List<Post> listFuzzyQuery = listFuzzyQuery(postType, content);
		List<Post> listByKeyWord = listByKeyWord(bridgeType);
		List<Post> listByCentury = listByCentury(century);
		
		PageInfo<Post> pageInfo = new PageInfo<>();
		List<Post> posts = new ArrayList<>();
		Integer total = 0;
		Integer effectiveNum = 0;
		Map<Post, Integer> map = new HashMap<>();
		if(listByKeyWord != null) {
			effectiveNum++;
			for(Post p:listByKeyWord) {
				map.put(p, 1);
			}
		}
		if(listFuzzyQuery != null) {
			effectiveNum++;
			for(Post p:listFuzzyQuery) {
				Integer num = map.get(p);
				if(num != null) {
					num++;
					map.put(p, num);
				}else {
					map.put(p, 1);
				}
			}
		}
		if(listByCentury != null) {
			effectiveNum++;
			for(Post p:listByCentury) {
				Integer num = map.get(p);
				if(num != null) {
					num++;
					map.put(p, num);
				}else {
					map.put(p, 1);
				}
			}
		}
		for(Entry<Post, Integer> entry : map.entrySet()) {
			if(entry != null && entry.getValue() == effectiveNum) {
				if(total >= (pageNum-1)*10 && total < pageNum*10) {
					posts.add(entry.getKey());
				}
				total++;
			}
		}
		pageInfo.setNavigatepageNums(IntegerUtils.getNavigates(total));
		pageInfo.setPageNum(pageNum);
		pageInfo.setPages(IntegerUtils.getPages(total));
		pageInfo.setPageSize(10);
		pageInfo.setList(posts);
		return ResponseMsg.success("").add("pageInfo", pageInfo).add("bridgeType", bridgeType).add("century", century);
	}
	
}
