package com.dlu.ghh.test;

import com.dlu.ghh.utils.StringUtil;
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

	@Test
	public void test3(){
		String str = "<p>线路正线全长130.774km，其中建德市境内正线长49.944km，衢州市境内正线长80.83km。<br style='box-sizing: content-box;'><br style='box-sizing: content-box;'>牵&nbsp;头人：中铁十四局集团有限公司，联合体成员：中铁第一勘察设计院集团有限公司<br></p>";
		String s = StringUtil.getTextFromHtml(str);
		System.out.println(s);
	}
}
