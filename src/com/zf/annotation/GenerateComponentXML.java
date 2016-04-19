package com.zf.annotation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.zf.interpreter.ConstantUtil;
import com.zf.util.Log;

public class GenerateComponentXML {
	
	private Log log = new Log(this.getClass());
	
	private Document document;
	
	private ParseComponent pc;
	
	private List<String> abs;
	
	private Map<String, List<String>> mMap;
	
	private String root = "data";
	
	private String common = "common";
	
	private String path = "config/component.xml";
	
	private String workPath;
	
	public void setWorkPath(String workPath) {
		this.workPath = workPath;
	}

	public GenerateComponentXML(){
		pc = new ParseComponent();	
		mMap = new HashMap<String, List<String>>();
	}
	
	public void generateComponentXML(){
		if(workPath!=null){
			System.setProperty("user.dir", workPath);
		}
		pc.loadAllComponent();
		log.info("load all component success!");
		List<String> listComponent = new ArrayList<String>(ConstantUtil.ALL_COMPONENT.keySet());
		for (String key : listComponent) {
			Class<?> clazz = ConstantUtil.ALL_COMPONENT.get(key).getClass();
			Method[] superMethods = clazz.getSuperclass().getDeclaredMethods();
			if(abs==null){
				List<Method> list = new ArrayList<Method>(Arrays.asList(superMethods));
				abs = this.removeDup(list);
			}
			List<Method> methods = new ArrayList<Method>(Arrays.asList(clazz.getMethods()));
			List<Method> declaredMethods = new ArrayList<Method>(Arrays.asList(clazz.getDeclaredMethods()));
			methods.retainAll(declaredMethods);
			mMap.put(clazz.getSimpleName(), this.removeDup(methods));
		}
	}
	
	public void createXML(){
		document = DocumentHelper.createDocument();
		Element root = document.addElement(this.root);
		Element common = root.addElement(this.common);
		for(String methodName : abs){
			common.addElement(methodName);
		}
		List<String> mList = new ArrayList<String>(mMap.keySet());
		for (String key : mList) {
			Element ele = root.addElement(key);
			List<String> kList = mMap.get(key);
			for (String k : kList) {
				ele.addElement(k);
			}
		}
	}
	
	public void saveXML() {
		XMLWriter writer;
		try {				
			OutputFormat format = OutputFormat.createPrettyPrint();
			writer = new XMLWriter(new FileWriter(new File(System.getProperty("user.dir")+File.separator+path)), format);
			writer.write(document);
			writer.close();
			log.info("save "+path+" success!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<String> removeDup(List<Method> list){
		List<String> temp = new ArrayList<String>();
		for (Method method : list) {
			if(!temp.contains(method.getName())){
				temp.add(method.getName());
			}
		}
		return temp;
	}
	
	public static void main(String[] args) {
		GenerateComponentXML gcx = new GenerateComponentXML();
		if(args!=null && args.length!=0){
			gcx.setWorkPath(args[0]);
		}
		gcx.generateComponentXML();
		gcx.createXML();
		gcx.saveXML();
	}
	
}
