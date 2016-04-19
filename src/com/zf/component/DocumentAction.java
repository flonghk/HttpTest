package com.zf.component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zf.annotation.Component;
import com.zf.util.DefinedException;
@Component
public class DocumentAction extends AbstractComponent{
	
	private Document doc;
	
	private Response response;		
	
	public void setResponse(Response response) {
		this.response = response;
		this.load();
	}

	private void load(){
		try {
			doc = response.parse();		
		} catch (IOException e) {
			throw new DefinedException("解析response出错: "+ response);
		}		
	}
	
	private void checkDoc(){
		if(doc == null){
			throw new DefinedException("请调用load方法!");
		}
	}
	
	private Elements findElements(String cssSelector){
		this.checkDoc();
		return doc.select(cssSelector);
	}
	
	private Element findElement(String cssSelector){
		this.checkDoc();
		Element element = null;
		Elements elements = this.findElements(cssSelector);
		if(elements.size()!=0){
			element = elements.get(0);
		}
		return element;
	}
	
	public boolean isExist(String cssSelector){
		if(this.findElements(cssSelector).size()==0){
			return false;
		}
		return true;
	}
	
	public String getElementText(String cssSelector){
		Element element = this.findElement(cssSelector);
		if(element == null){
			return null;
		}
		return element.text();
	}
	
	public String getElementName(String cssSelector){
		Element element = this.findElement(cssSelector);
		if(element == null){
			return null;
		}
		return element.nodeName();
	}
	
	private Map<String, String> getElementAttribute(String cssSelector){
		Map<String, String> map = new HashMap<String, String>(); 
		Element element = this.findElement(cssSelector);
		if(element == null){
			return null;
		}
		Iterator<Attribute> it = element.attributes().iterator();
		while(it.hasNext()){
			Attribute attr = it.next();
			map.put(attr.getKey(), attr.getValue());
		}
		return map;
	}
	
	public String getElementAttribute(String cssSelector, String attr){
		return this.getElementAttribute(cssSelector).get(attr);
	}
}
