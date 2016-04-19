package com.zf.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author zhangfei
 * @net id: 再见理想
 * @QQ: 408129370
 * @博客地址: http://www.cnblogs.com/zhangfei/
 */
public class JsonMap {
	
	public Map<String, String> mapStringAction(String map){
		Map<String, String> mapString = new HashMap<String, String>();
		List<String> mapList = this.getMapString(map);
		if(mapList==null){
			return null;
		}
		for (String s : mapList) {
			Map<String, String> m = this.getKeyValue(s.trim());
			if(m!=null){
				mapString.put(m.get("key"), m.get("value"));
			}
		}
		return mapString;
	}
	
	private List<String> getMapString(String map){
		List<String> jsonMap = new ArrayList<String>();		
		String mapString = map.trim().substring(1, map.trim().length()-1);
		String[] arrString = mapString.split(",");
		StringBuffer sb = new StringBuffer();
		for (String arrs : arrString) {
			if(sb.length()>0){
				if(this.isSmoothMapString(sb.toString())){
					jsonMap.add(sb.toString());
					sb.delete(0, sb.length());					
				}else{
					sb.append(",");
				}
			}
			sb.append(arrs);			
		}
		if(this.isSmoothMapString(sb.toString())){
			jsonMap.add(sb.toString());
			sb.delete(0, sb.length());					
		}else{
			jsonMap = null;
		}
		return jsonMap;
	}
	
	private Map<String, String> getKeyValue(String s){
		Map<String, String> map = new HashMap<String, String>();
		String[] kv = s.split(String.valueOf(JsonUtils.jsonMapConnector));
		if(kv.length < 2){
			return null;
		}
		StringBuffer temp = new StringBuffer();
		String key = null;
		String value = null;
		for (String kvs : kv) {
			temp.append(kvs);
			if(JsonUtils.isValidString(temp.toString(), JsonUtils.jsonStringBegin, JsonUtils.jsonStringEnd)){
				break;
			}else{
				temp.append(String.valueOf(JsonUtils.jsonMapConnector));
			}
		}
		key = temp.toString().trim();
		value = s.substring(s.indexOf(key)+key.length()+1).trim();
		if(!JsonUtils.isValidString(key, JsonUtils.jsonStringBegin, JsonUtils.jsonStringEnd)){
			return null;
		}
		if(!JsonUtils.isSmoothString(value)){
			return null;
		}
		map.put("key", key);
		map.put("value", value);
		return map;
	}
	
	private boolean isSmoothMapString(String s){
		boolean flag = false;
		if(s.indexOf(JsonUtils.jsonMapConnector)==-1){
			return flag;
		}
		s = s.trim();
		if(this.getKeyValue(s)!=null){
			flag = true;
		}
		return flag;
	}
	
	public boolean isMapString(String json){
		if(json.trim().charAt(0)==JsonUtils.jsonMapBegin){
			return true;
		}
		return false;
	}
	
}
