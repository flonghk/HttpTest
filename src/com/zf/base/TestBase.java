package com.zf.base;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

import com.zf.util.Global;
import com.zf.util.Log;
import com.zf.util.ParseXml;
@Listeners({com.zf.assertion.AssertionListener.class})
public class TestBase {
	
	protected Log log = new Log(this.getClass());
	
	public String testCase;
	
	private ParseXml px;
	
	private Map<String, String> commonMap;
	
	@Parameters({ "testCase" })
	@BeforeClass(alwaysRun = true)
	public void getTestCaseName(String testCase) {
		this.testCase = testCase;
	}
	
	private boolean testDataIsExist(){
		File file = new File(System.getProperty("user.dir")+File.separator+"test-data/"+testCase+".xml");
		if(!file.exists()){
			return false;
		}
		return true;
	}
	
	private void initialPx(){
		if(px==null){			
			px = new ParseXml(System.getProperty("user.dir")+File.separator+"test-data/"+testCase+".xml");
		}
	}
	
	private void getCommonMap(){
		if(commonMap==null){			
			Element element = px.getElementObject("/*/common");
			commonMap = px.getChildrenInfoByElement(element);
		}
	}
	
	private Map<String, String> getMergeMapData(Map<String, String> map1, Map<String, String> map2){
		Iterator<String> it = map2.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = map2.get(key);
			if(!map1.containsKey(key)){
				map1.put(key, value);
			}
		}
		return map1;
	}
	
	@DataProvider
    public Object[][] providerMethod(Method method){	
		List<Element> elements = null;
		if(this.testDataIsExist()){
			this.initialPx();
			this.getCommonMap();
			String methodName = method.getName();
			elements = px.getElementObjects("/*/"+methodName);
		}
		int index = 0;
		if(elements != null){
			index = elements.size();
		}
		if(index == 0){
			index++;
		}
		Object[][] object = new Object[index][];
		for (int i =0; i<index; i++) {
			Map<String, String> mergeCommon = new HashMap<String, String>();
			if(this.testDataIsExist() && elements.size()>0){
				mergeCommon = this.getMergeMapData(px.getChildrenInfoByElement(elements.get(i)), commonMap);
			}else if(this.testDataIsExist() && elements.size()==0){
				mergeCommon = this.getMergeMapData(new HashMap<String, String>(), commonMap);
			}
			Map<String, String> mergeGlobal = this.getMergeMapData(mergeCommon, Global.global);
			Object[] temp = new Object[]{mergeGlobal};
			object[i] = temp;
		}
		return object;
	}
	
	
}
