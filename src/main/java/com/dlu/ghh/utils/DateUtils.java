package com.dlu.ghh.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期帮助类
 * @author 隐王爷
 *
 */
@SuppressWarnings("serial")
public class DateUtils extends SimpleDateFormat{
	
	/**
	 *  返回两个日期的毫秒差值
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long compare(Date date1, Date date2) {
		return date2.getTime()-date1.getTime();//获取两个日期的毫秒差
	}
	
}
