package com.zf.base;

import java.io.File;

import com.zf.util.Log;
import com.zf.util.ParseXml;

public class Config {
	
	private static Log log = new Log(Config.class);
	
	public static int timeout;
	
	static{
		ParseXml px = new ParseXml(System.getProperty("user.dir")+File.separator+"config/config.xml");
		timeout = Integer.valueOf(px.getElementText("/config/timeout"))*1000;
		log.info("the timeout is: "+ timeout);
	}
	
}
