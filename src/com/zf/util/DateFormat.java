package com.zf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
	public String formatDateToString(long date) {
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	    return formatter.format(date);
	}
	 
	public String formatDate(Date date){       
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    return formatter.format(date);
	}
	
	//时间戳的格式化，当前时间往前推7天的时间戳：new Date().getTime()-7*24*3600*1000;
	public String formatDate(long date){       
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    return formatter.format(date);
	}
	
	public Date formatToDate(String date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    Date d = null;
	    try {
			d = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}	 
	    return d;
	}
	 
	public long formatDate(String date){       
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    Date d = null;
	    try {
			d = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}	 
	    return d.getTime();
	}
	
	public static void main(String[] args) {
		DateFormat df = new DateFormat();
		System.out.println(df.formatDateToString(System.currentTimeMillis()));
		System.out.println(df.formatDate(new Date()));
		System.out.println(df.formatDate(System.currentTimeMillis()));
		System.out.println(df.formatDate("2015-07-16"));
		System.out.println(df.formatToDate("2015-07-16"));
	}
}
