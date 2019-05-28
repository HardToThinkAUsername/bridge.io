package com.dlu.ghh.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.dlu.ghh.bean.ParamConfiguration;
import com.dlu.ghh.bean.User;

@Controller
@RequestMapping("/img")
public class ImageController {
	
	private Logger logger = Logger.getLogger(ImageController.class);
	
	@Autowired
	private ParamConfiguration pc;
	
	@ResponseBody
	@RequestMapping(value="uploadImage",method=RequestMethod.POST)
	public String uploadImage(@RequestParam(value="myImage") MultipartFile myImage,HttpSession session) throws Exception {
		JSONObject jsonObject = new JSONObject();
		StringBuffer netPath = new StringBuffer("");
		if(!myImage.isEmpty()) {
			// 获取session中的user
			User user = null;
			Object object = session.getAttribute("user");
			if(object != null && object instanceof User) {
				user = (User)object;
			}
			String filePath = pc.getUploadFolder()+"//post//"+user.getId();//获取文件在磁盘中存储的路径
			String fileName = myImage.getOriginalFilename();//获取文件名
			File fileFile = new File(filePath,fileName);//获取到文件对象
			if(!fileFile.getParentFile().exists()) {// 如果文件的父文件夹不存在
				fileFile.getParentFile().mkdirs();//自动创建缺少的父文件夹
			}
			try {
				myImage.transferTo(fileFile);//将参数中的文件存储到磁盘中
				netPath.append(pc.getNetPath())
					   .append("/post/")
					   .append(user.getId())
					   .append("/")
					   .append(myImage.getOriginalFilename());
				logger.info("用户"+user.getUsername()+"修改了头像");
				return jsonObject.put("result", "success").put("url", netPath.toString()).toString();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return jsonObject.put("result", "fail").toString();
	}
	
	
}
