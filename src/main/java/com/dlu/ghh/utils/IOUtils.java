package com.dlu.ghh.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class IOUtils {
	
	public static String readFile(File file){
		StringBuffer stringBuffer = new StringBuffer("");
		FileReader fileReader = null;
		char[] c = new char[1024];
		int len;
		try {
			fileReader = new FileReader(file);
			while ((len = fileReader.read(c)) != -1) {
				stringBuffer.append(new String(c, 0, len));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stringBuffer.toString();
	}
}
