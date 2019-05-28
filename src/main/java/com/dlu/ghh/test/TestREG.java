package com.dlu.ghh.test;

import java.util.regex.Pattern;

import org.junit.Test;

public class TestREG {
	
	@Test
	public void test() {
		String reg = "^1[3|5|8]{1}[0-9]{9}${11,11}";
		System.out.println(Pattern.matches(reg, "18742590949"));
		
	}
}
