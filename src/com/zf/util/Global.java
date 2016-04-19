package com.zf.util;

import java.io.File;
import java.util.Map;


public class Global {
	
	public static Map<String, String> global;
	
	static{
		ParseXml px = new ParseXml(System.getProperty("user.dir")+File.separator+"test-data/global.xml");
		global = px.getChildrenInfoByElement(px.getElementObject("/*"));
	}
	
}
