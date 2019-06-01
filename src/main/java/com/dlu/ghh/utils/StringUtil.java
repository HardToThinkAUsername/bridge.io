package com.dlu.ghh.utils;

public class StringUtil {

	/*
	 * null或者空串 返回true
	 */
	public static boolean isEmpty(String str) {
		if(str != null && !str.trim().equals(""))
			return false;
		return true;
	}
	
	/**
	 * 获取字符串中的img标签的src属性
	 * @param string 输入的字符串
	 * @return  img的 src属性
	 */
	public static String getImgSrc(String string) {
		int indexOf1 = string.indexOf("<img src=");//获取第一次出现"<img src="的索引
		if(indexOf1 != -1) {
			string = string.substring(indexOf1);
			int indexOf2 = string.indexOf("style=");//第一次出现"style"的索引
			return new String(string.toCharArray(), 10, indexOf2);
		}
		return "";
	}
	
	public static String getTextFromHtml(String string) {
		StringBuffer text = new StringBuffer("");
		Integer indexOf1 = -1;
		Integer indexOf2 = -1;
		while(true) {
			indexOf1 = string.indexOf(">");
			if(indexOf1 != -1 && indexOf1 != string.length()-1) {
				string = string.substring(indexOf1+1);
				indexOf2 = string.indexOf("<");
				text.append(string.substring(0, indexOf2));
			}else {
				break;
			}
		}
		Integer index1 = -1;
		Integer index2 = -1;
		StringBuffer text2 = new StringBuffer("");
		String newString = text.toString();
		while(true){
			index1 = newString.indexOf("&nbsp;");
			if(index1 == -1){
				text2.append(newString.substring(0));
				break;
			}
			text2.append(newString.substring(0,index1));
			newString = newString.substring(index1+6);
		}
		return text2.toString();
	}
}

