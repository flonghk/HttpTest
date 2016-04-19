package com.zf.interpreter;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.zf.annotation.ParseComponent;
import com.zf.component.JsoupAction;
import com.zf.util.DefinedException;
import com.zf.util.Log;
import com.zf.util.ParseXml;

public class ParseCase {
	
	private ParseXml px;
	
	private String caseName;
	
	private Object currentObject;
	
	private Parameter param;
	
	private Map<String, String> paramMap;
	
	private Log log = new Log(this.getClass());
	
	public ParseCase(String caseName, Map<String, String> paramMap) {
		this.caseName = caseName;
		this.paramMap = paramMap;
		this.initial();
	}
	
	private void initial(){
		ConstantUtil.ALL_COMPONENT.clear();
		ConstantUtil.EXT_PARAMS.clear();
		ConstantUtil.INT_PARAMS.clear();
		ParseComponent pc = new ParseComponent();
    	pc.loadAllComponent();
    	ConstantUtil.EXT_PARAMS = paramMap;
		px = new ParseXml(ConstantUtil.CASE_PATH+File.separator+this.caseName+ConstantUtil.CASE_NAME_SUFFIX);
		param = new Parameter();
		currentObject = ConstantUtil.ALL_COMPONENT.get(JsoupAction.class.getSimpleName());
	}
	
	public void parseCase() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		log.info("Start to run testcase: "+ caseName);
		List<Element> steps = px.getElementObjects(ConstantUtil.CASE_STEP_XPATH);
		if(steps==null || steps.size()==0){
			throw new DefinedException("The case: "+caseName+" is empty!");
		}
		for (Element element : steps) {
			String p = px.getElementText(element);
			param.setParam(p);
			List<String> paramList = param.getParameter();
			String returnName = px.getElementAttribute(element).get(ConstantUtil.RETURN_ATTRIBUTE);
			Method method = this.getMethod(element.getName(), paramList, returnName);
			if(method==null){
				this.setObject(element.getName());
				if(currentObject == null){
					if(returnName==null){
						throw new DefinedException("The step: "+element.getName()+" with "+paramList.toString()+" has undefined!");
					}else{
						throw new DefinedException("The step: "+element.getName()+" with "+paramList.toString()+" and return value ["+returnName+"] has undefined!");
					}
				}
			}else{
				this.executor(method, paramList, returnName);
			}
		}
	}
	
	private void setObject(String key){
		currentObject = ConstantUtil.ALL_COMPONENT.get(key);
	}
	
	private Method getMethod(String methodName, List<String> paramList, String returnName) {
		try {
			Method[] methods = currentObject.getClass().getMethods();
			for (Method m : methods) {
				if(m.getName().equals(methodName) && m.getParameterTypes().length==paramList.size()){
					if(returnName==null && this.isVoid(m.getReturnType())){
						return m;
					}else if(returnName!=null && !this.isVoid(m.getReturnType())){
						return m;
					}
				}
			}
		} catch (SecurityException e) {
			return null;
		}
		return null;
	}
	
	private boolean isVoid(Class<?> t) {
        return Void.class.isAssignableFrom(t) || void.class.equals(t);
    }
	
	private boolean isValidJson(String json){
		JsoupAction ja = (JsoupAction) ConstantUtil.ALL_COMPONENT.get(JsoupAction.class.getSimpleName());
		return ja.checkJson(json);
	}
	
	private boolean hasJson(List<String> paramList){
		for (int i = 0; i < paramList.size(); i++) {
			if(this.isValidJson(paramList.get(i))){
				return true;
			}
		}
		return false;
	}
	
	private void outputExecutorJson(Method method, List<String> paramList, Exception e, String returnName){
		boolean isJson = this.hasJson(paramList);
		if(isJson){
			log.info("setp: "+method.getName()+" with "+paramList.size()+" parameter: ");
			for (String p : paramList) {
				log.info1(p);
			}
			String err = e.getMessage();
			if(err==null){
				err = "error!";
			}
			if(returnName!=null){
				log.info1("and return ["+returnName+"] executor: "+err);
			}else{
				log.info1("executor: "+err);
			}
		}else{
			String msg = e.getMessage();
			if(msg==null){
				msg = "error!";
			}else if(e instanceof InvocationTargetException){
				msg = ((InvocationTargetException) e).getTargetException().getMessage();
			}
			log.info("setp: "+method.getName()+" with "+paramList.toString()+ " executor: "+msg);
		}
	}
	
	private void outputExecutorJson1(Method method, List<String> paramList, String returnValue){
		boolean isJson = this.hasJson(paramList);
		if(isJson){
			log.info(method.getName()+" with "+paramList.size()+" parameter: ");
			for (String p : paramList) {
				log.info1(p);
			}
			if(returnValue!=null){
				if(!this.isValidJson(returnValue)){
					log.info1("and the return value is: "+returnValue);
				}else{
					log.info1("and the return value is: ");
					log.info1(returnValue);
				}
			}
		}else{
			if(returnValue!=null){
				if(!this.isValidJson(returnValue)){
					log.info(method.getName()+" with "+paramList.toString()+ " and the reture vaule is: "+returnValue);
				}else{
					log.info(method.getName()+" with "+paramList.toString()+ " and the reture vaule is: ");
					log.info1(returnValue);
				}
			}else{
				log.info(method.getName()+" with "+paramList.toString());
			}
		}
	}
	
	private void executor(Method method, List<String> paramList, String returnName) throws IllegalAccessException, InvocationTargetException {
		Object[] params = new Object[paramList.size()];
		paramList.toArray(params);
		if(returnName != null){
			String returnValue = null;
				try {
					returnValue = method.invoke(currentObject, params).toString();
				} catch (IllegalArgumentException e) {
					this.outputExecutorJson(method, paramList, e, null);
					throw e;
				} catch (IllegalAccessException e) {
					this.outputExecutorJson(method, paramList, e, null);
					throw e;
				} catch (InvocationTargetException e) {
					this.outputExecutorJson(method, paramList, e, null);
					throw e;
				} catch(Exception e){
					this.outputExecutorJson(method, paramList, e, returnName);
					throw new DefinedException();
				}
			returnValue = this.convert(returnValue);
			ConstantUtil.INT_PARAMS.put(returnName, returnValue);
			this.outputExecutorJson1(method, paramList, returnValue);
		}else{
			try {
				method.invoke(currentObject, params);
			} catch (IllegalArgumentException e) {
				this.outputExecutorJson(method, paramList, e, null);
				throw e;
			} catch (IllegalAccessException e) {
				this.outputExecutorJson(method, paramList, e, null);
				throw e;
			} catch (InvocationTargetException e) {
				this.outputExecutorJson(method, paramList, e, null);
				throw e;
			} catch(Exception e){
				this.outputExecutorJson(method, paramList, e, null);
				throw new DefinedException();
			}
			this.outputExecutorJson1(method, paramList, null);
		}
	}
	
	private String convert(String utfString){  
	    StringBuilder sb = new StringBuilder();  
	    int i = -1;  
	    int pos = 0;  
	    while((i=utfString.indexOf("\\u", pos)) != -1){  
	        sb.append(utfString.substring(pos, i));  
	        if(i+5 < utfString.length()){  
	            pos = i+6;  
	            sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));  
	        }  
	    }  
	    sb.append(utfString.substring(pos));
	    return sb.toString();  
	}
	
}
