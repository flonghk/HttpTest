package com.zf.test;

import java.io.IOException;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestJDSearch {
	
	private Document doc;
	
	private Response response;
	
	/**
	 * 在搜索栏中输入mobile,然后输出搜索出来的第一个商品名称
	 */
	public void testGetSearchResult(){
		try {
			doc = Jsoup.connect("http://search.jd.com/Search?keyword=mobile&enc=utf-8&wq=mobile&pvid=6qlklvbi.iez5fx").timeout(5000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void parseResponse(){
//		System.out.println(response.toString());
		String t = doc.select("div#plist li[sku]:nth-of-type(1) div.p-name").text();
		System.out.println(t);
	}
	
	/*
	 * 把430479这个商品放入购入车后，通过"+-"来改变购物车中的数量
	 */
	public void testPost(){
		try {
			response = Jsoup.connect("http://cart.jd.com/async/loadIconsFromExt.action").ignoreContentType(true)
			.data("allSkuIds", "430479")
			.data("rd", "0.20607340258982354")			
			.timeout(5000)
			.method(Method.POST).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void checkPostResponse(){
		System.out.println(response.body().toString());		
	}
	

	
	public static void main(String[] args) {
		TestJDSearch td = new TestJDSearch();
		td.testGetSearchResult();
		td.parseResponse();
		td.testPost();
		td.checkPostResponse();
	}
	
	
}
