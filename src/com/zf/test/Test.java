package com.zf.test;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		String s = System.getProperty("user.dir");
		String s1 = s + File.separator + "bin";
		System.out.println(s1.substring(s.length() + 1));
		System.out.println(s);
		System.out.println(s1);
	}

	

}
