package com.dlu.ghh.test;

import org.junit.Test;

import com.dlu.ghh.utils.IntegerUtils;

public class TestUtils {

	@Test
	public void test() {
		int[] navigates = IntegerUtils.getNavigates(89);
		for(Integer i:navigates) {
			System.out.println(i);
		}
	}
	
	@Test
	public void test2() {
		System.out.println(IntegerUtils.getPages(3));
	}
}
