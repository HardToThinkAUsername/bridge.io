package com.dlu.ghh.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dlu.ghh.bean.Data;
import com.dlu.ghh.bean.ParamConfiguration;
import com.dlu.ghh.bean.Permission;
import com.dlu.ghh.bean.Post;
import com.dlu.ghh.bean.User;
import com.dlu.ghh.service.DataService;
import com.dlu.ghh.service.RolePermissionService;
import com.dlu.ghh.utils.IntegerUtils;
import com.dlu.ghh.utils.ResponseMsg;
import com.dlu.ghh.utils.SessionUtils;
import com.dlu.ghh.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/data")
public class DataController {
	private Logger logger = Logger.getLogger(DataController.class);

	@Autowired
	private DataService dataService;
	
	@Autowired
	private ParamConfiguration pc;
	
	@ResponseBody
	@RequestMapping(value="upload",method=RequestMethod.POST)
	public ResponseMsg dataUpload(@RequestParam("data")MultipartFile file,Data data,HttpSession session) {
		if(file == null) {//未选择文件
			return ResponseMsg.fail("请选择文件!");
		}
		if(SessionUtils.sessionValidate(session)) {// 检验session是否有效
			User user = (User)session.getAttribute("user");
			if(user.getShutup() == 1) {
				return ResponseMsg.fail("你已被禁言!");
			}
			StringBuffer path = new StringBuffer("");// 拼接 资源上传到服务器后的完整路径
			StringBuffer address = new StringBuffer();// 拼接 从网络上访问资源的路径
			path.append(pc.getUploadFolder())
						.append("\\data\\")
						.append(user.getId())
						.append("\\")
						.append(file.getOriginalFilename());
			address.append(pc.getNetPath())
				   .append("/data/")
				   .append(user.getId())
				   .append("/")
				   .append(file.getOriginalFilename());
			File realFile = new File(path.toString());//将文件上传后的完整路径映射为file对象
			if(!realFile.getParentFile().exists()) {// 如果有些父文件夹缺省
				realFile.getParentFile().mkdirs();//自动创建缺省的父文件夹
			}
			try {
				file.transferTo(realFile);//将文件存储到服务器磁盘中
				// 在数据中插入一条资料记录
				data.setDate(new Date());//设置日期
				data.setUser(user);//设置用户
				data.setAddress(address.toString());
				data.setName(file.getOriginalFilename());
				data.setDownloadTimes(0);
				data.setStatus(0);
				dataService.upload(data);//向数据库中插入资料信息
				logger.info("上传文件成功");
				return ResponseMsg.success("上传文件成功!");
			} catch (IllegalStateException e) {
				logger.error("上传文件出错");
				return ResponseMsg.fail("文件上传出错, 请重试!");
			} catch (IOException e) {
				logger.error("上传文件出错");
				return ResponseMsg.fail("文件上传出错, 请重试!");
			}
		}
		return ResponseMsg.fail("请先登录!");
	}
	
	/**
	 * 获取首页初始化资料
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getDataInit",method=RequestMethod.GET)
	public ResponseMsg getData(@RequestParam(value="pageNum",required=false,defaultValue="0")Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="6",required=false)Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);//开启分页查询
		List<Data> datas = dataService.getData();// 从数据库中获取资料记录集
		PageInfo<Data> pageInfo = new PageInfo<>(datas);
		if(datas != null) // 集合中有记录
			return ResponseMsg.success("").add("datas", pageInfo);
		return ResponseMsg.fail("加载数据出错,请重新刷新页面!");
	}
	
	/**
	 * 获取所有资料
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getDataAll")
	public ResponseMsg getDataAll(@RequestParam(value="pageNum",required=false,defaultValue="0")Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false)Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Data> data = dataService.getData();
		PageInfo<Data> pageInfo = new PageInfo<>(data);
		if(data != null) 
			return ResponseMsg.success("").add("pageInfo", pageInfo);
		return ResponseMsg.fail("加载数据出错,请重新刷新页面!");
	}
	
	/**
	 * 查看自己发布资料的数量
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getDataCountByUserId",method=RequestMethod.GET)
	public ResponseMsg getDataCountByUserId(@RequestParam(value="id",required=false)Integer id) {
		if(id != null && id instanceof Integer) {
			Integer countByUserId = dataService.getCountByUserId(id);
			return ResponseMsg.success("").add("count", countByUserId);
		}
		return ResponseMsg.fail("");
	}
	
	/**
	 * 根据描述或者文件名进行模糊查询
	 * @param pageSize
	 * @param pageNum
	 * @param content
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="fuzzyQuery",method=RequestMethod.GET)
	public ResponseMsg fuzzyQuery(@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNum",required=false,defaultValue="0")Integer pageNum,
			@RequestParam(value="content",required=false)String content,
			@RequestParam(value="type")Integer type) {
		if(content != null && !content.trim().equals("")) {
			PageHelper.startPage(pageNum, pageSize);
			List<Post> list = dataService.getFuzzy(content);
			PageInfo<Post> pageInfo = new PageInfo<>(list);
			return ResponseMsg.success("").add("pageInfo", pageInfo);
		}else
			return ResponseMsg.fail("输入不能为空!");
	}
	
	/**
	 * 查看自己发布的资料
	 * @param id
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value="getDataByUserId",method=RequestMethod.GET)
	public ResponseMsg getDataByUserId(@RequestParam(value="id",required=false)Integer id,
			@RequestParam(value="pageNum",required=false)Integer pageNum,
			@RequestParam(value="pageSize",required=false)Integer pageSize) {
		if(id != null && id instanceof Integer) {//如果id有效
			if(pageNum != null && pageSize != null) {
				PageHelper.startPage(pageNum, pageSize);
				List<Data> list = dataService.getByUserId(id);
				PageInfo<Data> pageInfo = new PageInfo<>(list);
				return ResponseMsg.success("").add("pageInfo", pageInfo);
			}else {
				List<Data> list = dataService.getByUserId(id);
				return ResponseMsg.success("").add("list", list);
			}
		}
		return ResponseMsg.fail("");
	}
	
	/**
	 * 删除文件
	 */
	@ResponseBody
	@RequestMapping(value="deleteById")
	public ResponseMsg deleteById(@RequestParam(value="id")Integer id) {
		if(id != null && id instanceof Integer) {
			if(dataService.deleteById(id)) {//删除成功
				logger.info("id为:"+id+"的文件被删除");
				return ResponseMsg.success("删除成功!");
			}
		}
		return ResponseMsg.fail("删除失败!");
	}
	
	/*
	 * 用户浏览
	 * */
	@ResponseBody
	@RequestMapping(value="userRead",method=RequestMethod.GET)
	public ResponseMsg userRead(@RequestParam(value="id",required=false)Integer dataId,
			HttpSession session) {
		if(!IntegerUtils.isEmpty(dataId)) {
			Data data = dataService.getById(dataId);
			if(data != null) {
				if(SessionUtils.sessionValidate(session)) {
					User user = (User)session.getAttribute("user");
					return ResponseMsg.success("").add("data", data).add("sessionUser", user);
				}else
					return ResponseMsg.success("").add("data", data).add("sessionUser", "null");
					
			}
		}
		return ResponseMsg.fail("查询文件失败,请检查网络!");
	}
	
	/**
	 * 用户下载
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="download",method=RequestMethod.POST)
	public ResponseMsg download(@RequestParam(value="id")Integer id) {
		Data data = dataService.getById(id);
		data.setDownloadTimes(data.getDownloadTimes()+1);
		if(dataService.updateByIdSelective(data)) {
			logger.info("用户下载了id为:"+id+"的文件");
			return ResponseMsg.success("").add("times", data.getDownloadTimes());
		}
		return ResponseMsg.fail("");
	}
	
	/**
	 * 查看他人发布的资料
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="queryUserData")
	public ResponseMsg queryUserData(@RequestParam(value="id",required=false)Integer userId) {
		List<Data> list = dataService.getByUserId(userId);
		if(list != null) {
			return ResponseMsg.success("").add("datas",list);
		}
		return ResponseMsg.fail("");
	}
	
	/**
	 * 管理帖子
	 * @param pageNum
	 * @param pageSize
	 * @param keywords
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getAllForManage",method=RequestMethod.GET)
	public ResponseMsg getAllForManage(@RequestParam(value="pageNum",required=false,defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="keywords",required=false)String keywords,
			@RequestParam(value="type",required=false)Integer type) {
		if(type == 0) {//没有条件
			PageHelper.startPage(pageNum, pageSize);
			List<Data> list = dataService.getData();
			PageInfo<Data> pageInfo = new PageInfo<>(list);
			return ResponseMsg.success("").add("pageInfo", pageInfo);
		}
		if(StringUtil.isEmpty(keywords)) {//关键词
			return ResponseMsg.fail("关键词输入不能为空!");
		}
		Data data = new Data();
		if(type == 1) {//资料id
			try {
				int id = Integer.parseInt(keywords);
				data.setId(id);
			} catch (NumberFormatException e) {
				return ResponseMsg.fail("非法输入!请输入整型字符!");
			}
		}
		if(type == 2) {//资料名字
			data.setName(keywords);
		}
		if(type == 3) {//资料描述
			data.setDescription(keywords);
		}
		if(type == 4) {//作者名
			User user = new User();
			user.setUsername(keywords);
			data.setUser(user);
		}
		PageHelper.startPage(pageNum, pageSize);
		List<Data> list = dataService.getAllSelectiveFuzzy(data);
		PageInfo<Data> pageInfo = new PageInfo<>(list);
		return ResponseMsg.success("").add("pageInfo", pageInfo);
	}
	
	/**
	 * 查询待审阅的帖子
	 * @param pageNum
	 * @param pageSize
	 * @param keywords
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getAllForReview",method=RequestMethod.GET)
	public ResponseMsg getAllForReview(@RequestParam(value="pageNum",required=false,defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="keywords",required=false)String keywords,
			@RequestParam(value="type",required=false)Integer type) {
		if(type != 0 && StringUtil.isEmpty(keywords)) {//关键词
			return ResponseMsg.fail("关键词输入不能为空!");
		}
		Data data = new Data();
		if(type == 1) {//资料id
			try {
				int id = Integer.parseInt(keywords);
				data.setId(id);
			} catch (NumberFormatException e) {
				return ResponseMsg.fail("非法输入!请输入整型字符!");
			}
		}
		if(type == 2) {//资料名字
			data.setName(keywords);
		}
		if(type == 3) {//资料描述
			data.setDescription(keywords);
		}
		if(type == 4) {//作者名
			User user = new User();
			user.setUsername(keywords);
			data.setUser(user);
		}
		PageHelper.startPage(pageNum, pageSize);
		List<Data> list = dataService.getNotReview(data);
		PageInfo<Data> pageInfo = new PageInfo<>(list);
		return ResponseMsg.success("").add("pageInfo", pageInfo);
	}
	
	@Autowired
	private RolePermissionService rolePermissionService;
	/**
	 * 删除资料
	 */
	@ResponseBody
	@RequestMapping(value="removeData",method=RequestMethod.POST)
	public ResponseMsg removeData(@RequestParam(value="id",required=false)Integer dataId,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.DATA_DELETE, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(dataService.deleteById(dataId)) {
			logger.info("用户"+user.getUsername()+"删除了id为"+dataId+"的文件");
			return ResponseMsg.success("删除成功!");
		}
		return ResponseMsg.fail("删除失败!");
	}
	
	/**
	 * 删除多个资料
	 */
	@ResponseBody
	@RequestMapping(value="removeDataMultiple",method=RequestMethod.POST)
	public ResponseMsg removeDataMultiple(@RequestParam(value="ids",required=false)String dataIds,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.POST_DELETE, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(dataService.deleteByIds(dataIds)) {
			logger.info("用户"+user.getUsername()+"删除了id为"+dataIds+"的所有文件");
			return ResponseMsg.success("");
		}
		return ResponseMsg.fail("删除失败!");
	}
	
	/**
	 * 审阅资料
	 * @param postId
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="reviewData",method=RequestMethod.POST)
	public ResponseMsg reviewPost(@RequestParam(value="id",required=false)Integer dataId,
			HttpSession session) {
		if(!SessionUtils.sessionValidate(session)) {
			return ResponseMsg.fail("请先返回首页登录!");
		}
		User user = (User)session.getAttribute("user");
		if(!rolePermissionService.isHasThePermission(Permission.DATA_REVIEW, user.getRole().getId())) {
			return ResponseMsg.fail("你不具有该权限!");
		}
		if(dataService.review(dataId)) {
			logger.info("审阅并通过了id为["+dataId+"]的资料!");
			return ResponseMsg.success("");
		}
		return ResponseMsg.fail("审阅失败!");
	}
	
	
}
