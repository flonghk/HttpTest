package com.zf.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author zhangfei
 * @net id: 再见理想
 * @QQ: 408129370
 * @博客地址: http://www.cnblogs.com/zhangfei/
 */
public class JsonAction {
	
	private JsonList jsonList;
	
	private JsonMap jsonMap;
	
	public JsonAction() {
		jsonList = new JsonList();
		jsonMap = new JsonMap();
	}
	
	public boolean checkJsonString(String json){
		if(json==null){
			return false;
		}
		if(!jsonList.isListString(json) && !jsonMap.isMapString(json)){
			return false;
		}
		return this.checkPartJsonString(json);
	}
	
	private boolean checkPartJsonString(String json){
		if(json==null){
			return false;
		}
		if(json.charAt(0)==JsonUtils.jsonListBegin){
			List<String> listJson = jsonList.listStringAction(json);
			if(listJson == null){
				return false;
			}else{				
				for (String ls : listJson) {
					return this.checkPartJsonString(ls);
				}
			}
		}else if(json.charAt(0)==JsonUtils.jsonMapBegin){
			Map<String, String> mspJson = jsonMap.mapStringAction(json);
			if(mspJson == null){
				return false;
			}else{
				Iterator<String> it = mspJson.values().iterator();
				while(it.hasNext()){
					String value = it.next();
					return this.checkPartJsonString(value);
				}
			}
		}else{
			return JsonUtils.isSmoothString(json);
		}
		return false;
	}
	
	private boolean isVaildPath(String path){
		if(path == null || "".equals(path.trim())){
			return false;
		}
		path = path.trim();
		if(path.charAt(0)!=JsonUtils.pathSeparator){
			return false;
		}
		List<String> listPath = this.getPathKey(path);
		if(listPath == null){
			return false;
		}
		return true;
	}
	
	private List<String> getPathKey(String path){
		List<String> list = new ArrayList<String>();
		path = path.substring(1);
		String[] paths = path.split(String.valueOf(JsonUtils.pathSeparator));
		for (String p : paths) {
			p = p.trim();
			if("".equals(p)){
				return null;
			}else{
				list.add(p);
			}
		}
		return list;
	}
	
	public Object getPathValue(String json, String path){
		if(!this.isVaildPath(path)){
			throw new RuntimeException("the path: "+path+" is not vaild!");
		}
		if(!this.checkJsonString(json)){
			throw new RuntimeException("the json: "+json+" is not vaild!");
		}
		json = json.trim();
		path = path.trim();
		List<String> listPath = this.getPathKey(path);
		String partJson = json;
		for (int i = 0; i < listPath.size(); i++) {
			partJson = this.getPathText(partJson, listPath.get(i));			
		}
		return JsonUtils.outputString(partJson);
	}
	
	public boolean isExistPath(String json, String path){
		if(!this.isVaildPath(path)){
			throw new RuntimeException("the path: "+path+" is not vaild!");
		}
		if(!this.checkJsonString(json)){
			throw new RuntimeException("the json: "+json+" is not vaild!");
		}
		try{			
			json = json.trim();
			path = path.trim();
			List<String> listPath = this.getPathKey(path);
			String partJson = json;
			for (int i = 0; i < listPath.size(); i++) {
				partJson = this.getPathText(partJson, listPath.get(i));			
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	private String getPathText(String json, String p){
		boolean isListString = jsonList.isListString(json);
		boolean listPath = p.matches("\\"+JsonUtils.pathListBegin+"\\"+JsonUtils.pathListIndexBegin+"\\d+"+"\\"+JsonUtils.pathListIndexEnd);
		boolean isMapPath = jsonMap.isMapString(json);
		if(!isListString && listPath){
			throw new RuntimeException("the path: "+p+" is list path, but the path json is "+json);
		}
		int listIndex = 0;
		if(listPath){
			listIndex = Integer.valueOf(p.substring(p.indexOf(JsonUtils.pathListIndexBegin)+1, p.indexOf(JsonUtils.pathListIndexEnd)));
			List<String> jList = jsonList.listStringAction(json);
			if(jList.size() < listIndex+1){
				throw new RuntimeException("the path list json: "+json+" size is "+jList.size()+", but the path: "+p+" index is "+(listIndex+1));
			}
			return jList.get(listIndex);
		}else if(isMapPath){
			Map<String, String> jMap = jsonMap.mapStringAction(json);
			String key = JsonUtils.jsonStringBegin+p+JsonUtils.jsonStringEnd;
			if(!jMap.containsKey(key)){
				throw new RuntimeException("the path map json: "+json+" had not contains the key: "+p);
			}
			return jMap.get(key);
		}else{
			throw new RuntimeException();
		}
	}

	
	
	public static void main(String[] args) {
		String s = "[{ \"firstName\": \"Eric\", \"lastName\": \"Clapton\", \"instrument\": \"guitar\" },{ \"firstName\": \"Sergei\", \"lastName\": \"Rachmaninoff\", \"instrument\": \"piano\" }] ";
		String s1 = "[0,1,2,\"]3\"]";
		String s2 = "[\"a\",\"b\",\"c\",\"d\",\"\"]";
		String s3 = "{\"retCode\":200,\"retMsg\":\"Success.\",\"result\":{\"timestamp\":1433730550000,\"data\":\"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\u003d\u003d\n\"}}";
		JsonAction jsonAction = new JsonAction();		
		System.out.println(jsonAction.isExistPath(s2,"/*[1]"));
		System.out.println(jsonAction.getPathValue(s3,"/result/data"));
		System.out.println(jsonAction.getPathValue(s,"/*[1]"));
		int t = (Integer) jsonAction.getPathValue(s1,"/*[1]");
		System.out.println(t==1);
//		String s4 = "\"retCode\":200";
//		String k = "\"retCode\"";
//		System.out.println(s4.substring(s4.indexOf(k)+k.length()+1));
	}

}
