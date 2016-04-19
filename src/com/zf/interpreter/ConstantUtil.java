package com.zf.interpreter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConstantUtil {
	
	public static Map<String, Object> ALL_COMPONENT = new HashMap<String, Object>();
	
	public static Map<String, String> EXT_PARAMS = new HashMap<String, String>();
	
	public static Map<String, String> INT_PARAMS = new HashMap<String, String>();
	
	public static final String CASE_PATH = System.getProperty("user.dir")+File.separator+"test-case";
	
	public static final String CASE_NAME_SUFFIX = ".xml";
	
	public static final String CASE_STEP_XPATH = "/*/*";
	
	public static final String COMPONENT_PATH = "com.zf.component";
	
	public static final String COMPILE_PATH = "bin";
	
	public static final String RETURN_ATTRIBUTE = "return";
	
	public static void main(String[] args) {
		String jarFilePath = ConstantUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();  
		System.out.println(jarFilePath);
		System.out.println(ConstantUtil.class.getResource("").getPath());
		File file = new File(jarFilePath);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].getPath());
			System.out.println(files[i].getAbsolutePath());
		}
	}
	
}
