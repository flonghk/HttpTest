package com.zf.assertion;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import com.zf.util.Log;

public class Assertion {
	
	private static Log log = new Log(Assertion.class);
	
	public static boolean flag = true;
	
	public static List<Error> errors = new ArrayList<Error>();
	
	public static void begin(){
		flag = true;
	}
	
	public static void verifyEquals(Object actual, Object expected){
		try{
			Assert.assertEquals(actual, expected);
			log.info("verify success : "+actual);
		}catch(Error e){
			errors.add(e);
			flag = false;
		}
	}
	
	public static void verifyEquals(Object actual, Object expected, String message){
		try{
			Assert.assertEquals(actual, expected, message);
			log.info("verify success : "+message+" "+actual);
		}catch(Error e){
			errors.add(e);
			flag = false;
		}
	}
	
	public static void assertEquals(Object actual, Object expected){
		Assert.assertEquals(actual, expected);
		log.info("verify success : "+actual);
	}
	
	public static void assertEquals(Object actual, Object expected, String message){
		Assert.assertEquals(actual, expected, message);
		log.info("verify success : "+message+" "+actual);
	}
	
	public static void contains(String value, String sub){
		try{
			if(value.contains(sub)){
				log.info("verify success : ["+value+"] contains ["+sub+"].");
			}else{
				Assert.fail("verify failed : ["+value+"] not contains ["+sub+"].");
			}
		}catch(Error e){
			errors.add(e);
			flag = false;
		}
	}
	
	public static void contains(String value, String sub, String message){
		try{
			if(value.contains(sub)){
				log.info("verify success : "+message+" ["+value+"] contains ["+sub+"].");
			}else{
				Assert.fail("verify failed : "+message+" ["+value+"] not contains ["+sub+"].");
			}
		}catch(Error e){
			errors.add(e);
			flag = false;
		}
	}

}
