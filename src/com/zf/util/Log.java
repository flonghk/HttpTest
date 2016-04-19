package com.zf.util;

import java.util.Date;

import org.apache.log4j.Logger;
import org.testng.Reporter;

public class Log {
	private Logger logger;
	
	private DateFormat df;
	
	public Log(Class<?> clazz){
		logger = Logger.getLogger(clazz);
		df = new DateFormat();
	}
	
	public Log(String s){
		logger = Logger.getLogger(s);
		df = new DateFormat();
	}
	
	public Log(){
		logger = Logger.getLogger("");
		df = new DateFormat();
	}

    public void info(Object message) {       
        logger.info(message);
        this.testngLogOutput(message);
    }
    
    public void info1(Object message) {       
        logger.info(message);
        Reporter.log(message.toString());
    }

    public void error(Object message) {        
        logger.error(message);
        this.testngLogOutput(message);
    }

    public void warn(Object message) {        
        logger.warn(message);
        this.testngLogOutput(message);
    }
    
    public void debug(Object message) {        
        logger.debug(message);
        this.testngLogOutput(message);
    }
    
    private void testngLogOutput(Object message){
    	Reporter.log(df.formatDate(new Date())+" : "+message);
    }
}
