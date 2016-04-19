package com.zf.testcase;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.testng.annotations.Test;

import com.zf.base.TestBase;
import com.zf.interpreter.ParseCase;

public class TestTemp extends TestBase{
	
	@Test(dataProvider="providerMethod")
	public void test(Map<String, String> param) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		ParseCase pc = new ParseCase(this.getClass().getSimpleName(), param);
		pc.parseCase();
	}

	
}
