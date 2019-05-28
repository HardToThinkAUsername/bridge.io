package com.dlu.ghh.utils;

import javax.servlet.http.HttpSession;

import com.dlu.ghh.bean.User;

public class SessionUtils {

	/**
	 * 检测session  判断是否已经登录
	 * @param session
	 * @return
	 */
	public static boolean sessionValidate(HttpSession session) {
		if(session != null) {
			Object object = session.getAttribute("user");//从session中获取user属性
			if(object != null && object instanceof User)//如果user不是空并且是User
				return true;
		}
		return false;
	}
}
