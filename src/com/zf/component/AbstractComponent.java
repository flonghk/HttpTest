package com.zf.component;

import com.zf.assertion.Assertion;
import com.zf.json.JsonAction;
import com.zf.util.Log;

public abstract class AbstractComponent {
	
	protected Log log = new Log(this.getClass());
	
	private JsonAction jsonAction;
	
	public AbstractComponent(){
		jsonAction = new JsonAction();
	}
	
	public void log(String msg){
		log.info(msg);
	}
	
	public void verify(Object actual, Object expected){
		Assertion.verifyEquals(actual, expected);
	}
	
	public void verify(Object actual, Object expected, String message){
		Assertion.verifyEquals(actual, expected, message);
	}
	
	public void assertion(Object actual, Object expected){
		Assertion.assertEquals(actual, expected);
	}
	
	public void assertion(Object actual, Object expected, String message){
		Assertion.assertEquals(actual, expected, message);
	}
	
	public void contains(String value, String sub){
		Assertion.contains(value, sub);
	}
	
	public void contains(String value, String sub, String message){
		Assertion.contains(value, sub, message);
	}
	
	public boolean checkJson(String json){
		return jsonAction.checkJsonString(json);
	}
	
	public boolean isExistPath(String json, String path){
		return jsonAction.isExistPath(json, path);
	}
	
	public String jsonValue(String json, String path){
		return jsonAction.getPathValue(json, path).toString();
	}
	
}
