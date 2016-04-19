package com.zf.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {

	public boolean match(String reg, String str) {
		return Pattern.matches(reg, str);
	}

	public List<String> find(String reg, String str) {
		Matcher matcher = Pattern.compile(reg).matcher(str);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}
	
	public String find(String reg, String str, int index) {
		Matcher matcher = Pattern.compile(reg).matcher(str);
		if (matcher.find()) {
			return matcher.group(index);
		}
		return null;
	}

	public String findString(String reg, String str) {
		String returnStr = null;
		List<String> list = this.find(reg, str);
		if (list.size() != 0)
			returnStr = list.get(0);
		return returnStr;
	}

	

	public static void main(String[] args) {
		RegExp re = new RegExp();
		System.out.println(re.find("(?<=TOTAL.{0,1})\\d+", "TOTAL1234-213", 0));
		String s = "TOTAL1234-213";
		System.out.println(s.replaceFirst("TOTAL.*?(\\d+).*", "$1"));
	}
}
