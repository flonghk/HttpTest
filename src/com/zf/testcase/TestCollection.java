package com.zf.testcase;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.testng.annotations.Test;

import com.zf.base.TestBase;
import com.zf.interpreter.ParseCase;

public class TestCollection extends TestBase {

	@Test(dataProvider = "providerMethod")
	public void httpTest(Map<String, String> param) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		ParseCase pc = new ParseCase(testCase==null?this.getClass().getSimpleName():testCase, param);
		pc.parseCase();
	}
}