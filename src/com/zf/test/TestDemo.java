package com.zf.test;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
 
public class TestDemo {
     
    public void testJsop(){
        try {
        	Connection conn = Jsoup.connect("https://passport.jd.com/new/login.aspx");
            conn.data("loginname","test1");
            conn.data("loginpwd","test1");
            conn.timeout(30000);           
            conn.method(Method.POST);
            Response response = conn.execute();
            Map<String, String> cookies = response.cookies();
            Document doc = Jsoup.connect("http://order.jd.com/center/list.action")
                .cookies(cookies)
                .timeout(30000)
                .get();
            System.out.println(doc);
        } catch (IOException e) {          
            e.printStackTrace();
        }
    }
     
    public static void main(String[] args) {
    	TestDemo t = new TestDemo();
        t.testJsop();
    }
     
}

