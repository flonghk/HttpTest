package com.zf.interpreter;

import java.util.ArrayList;
import java.util.List;

import com.zf.util.DefinedException;

public class Parameter {
	
	private String param;
	
	private RegExp re;

	public Parameter() {
		re = new RegExp();
	}
	
	public void setParam(String param) {
		this.param = param;
	}

	private boolean hadSubParameter(String p){
		return p.matches(".*(?<!\\\\)\\$\\{.*");
	}
	
	private boolean isValidSubParameter(String p){
		return p.matches(".*((?<!\\\\)\\$\\{(.*?)(?<!\\\\)\\}[^(\\$\\{)]*)+");
	}
	
	private String getSubParameter(String p){
		String reg = "(?<!\\\\)\\$\\{(.*?)(?<!\\\\)\\}";
		while(this.hadSubParameter(p)){
			p = p.replaceFirst(reg, this.getRealValue(re.find(reg, p, 1)));
		}
		return p;
	}
	
	private String getRealValue(String key){		
		if(ConstantUtil.INT_PARAMS.containsKey(key)){
			return ConstantUtil.INT_PARAMS.get(key);
		}
		if(ConstantUtil.EXT_PARAMS.containsKey(key)){
			return ConstantUtil.EXT_PARAMS.get(key);
		}
		throw new DefinedException("The parameter: "+key+" had not been defined.");
	}
	
	
	
	public List<String> getParameter(){
		List<String> list = new ArrayList<String>();
		if(param.trim().equals("")){
			return list;
		}
		String[] ps = param.split("(?<!\\\\),");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <= ps.length; i++) {
			if(sb.length()!=0){
				if(this.hadSubParameter(sb.toString()) && this.isValidSubParameter(sb.toString())){
					list.add(this.getSubParameter(sb.toString()));
					sb = new StringBuffer();
				}else if(!this.hadSubParameter(sb.toString())){
					list.add(sb.toString());
					sb = new StringBuffer();
				}
			}
			if(i!=ps.length){
				sb.append(ps[i]);
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		String s = "123,${dd},a${ssf} ";
		Parameter p = new Parameter();
		p.setParam(s);
		ConstantUtil.EXT_PARAMS.put("dd", "s1");
		ConstantUtil.EXT_PARAMS.put("ssf", "s2");
		System.out.println(p.getParameter());
	}
	
}
