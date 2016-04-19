package com.zf.test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
 
public class TestDataDemo {
     
	@DataProvider
    public Object[][] dataProvider(){
		TestEnum[] vs = TestEnum.values();
		Object[][] obj = new Object[vs.length][];
		for (int i = 0; i < vs.length; i++) {
			obj[i] = new Object[]{vs[i]};
		}
        return obj;
    }
     
    @Test(dataProvider="dataProvider")
    public void testDemo(TestEnum te){
        System.out.println(te.getRetCode());
        System.out.println(te.getMsg());
    }  
   
}
