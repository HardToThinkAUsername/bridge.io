package com.dlu.ghh.utils;

public class IntegerUtils {

	public static boolean isEmpty(Integer integer) {
		if(integer != null && integer instanceof Integer) 
			return false;
		return true;
	}
	
	public static Integer getPages(Integer integer) {
		if(isEmpty(integer)) {
			return null;
		}
		float f = Float.parseFloat(integer.toString());
		return (int) Math.ceil(f/10f);
	}
	
	public static int[] getNavigates(Integer integer) {
		Integer count = getPages(integer);
		int[] nav = new int[count];
		for(int i=0;i<count;i++) {
			nav[i] = i+1;
		}
		return nav;
	}
	
	
}
