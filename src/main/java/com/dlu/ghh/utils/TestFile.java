package com.dlu.ghh.utils;

import java.io.File;

import org.junit.Test;

public class TestFile {
	
	@Test
	public void test() {
		File file = new File("D://nihao//wohao//dajiahao//hello.txt");
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
	}
}
