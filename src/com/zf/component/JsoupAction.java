package com.zf.component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.zf.annotation.Component;
import com.zf.base.Config;
import com.zf.interpreter.ConstantUtil;
import com.zf.util.DefinedException;

@Component
public class JsoupAction extends AbstractComponent{
	
	private String url;
	
	private Connection conn = null;
	
	private boolean isPost = false;
	
	private boolean isGet = true;	
	
	private int timeOut = Config.timeout;
	
	private Response response;
	
	private Map<String, String> cookies;
	
	public void post() {
		this.isPost = true;
		this.isGet = false;
		if(cookies!=null){
			this.setCookies();
		}
		this.submit();
		this.saveCookies();
	}

	public void get() {
		this.isGet = true;
		this.isPost = false;
		this.submit();
	}	
	
	public String response() {
		return response.body();
	}

	private void initial(){
		conn = null;
		isPost = false;
		isGet = true;
		response = null;
		cookies = null;
	}
	
	public void url(String url){
		this.url = url;
		this.connection();
		this.setTimeOut(timeOut);
	}
	
	private void connection(){
		this.checkUrl();
		this.initial();
		conn = Jsoup.connect(url);
	}
	
	private void checkUrl(){
		if(url == null || "".equals(url)){
			throw new DefinedException("请调用setUrl方法!");
		}
	}
	
	private void checkConn(){
		if(conn == null){
			throw new DefinedException("请调用connection方法!");
		}
	}
	
	private void checkResponse(){
		if(response == null){
			throw new DefinedException("请调用submit方法!");
		}
	}
	
	private void checkCookies(){
		if(cookies == null){
			throw new DefinedException("请调用saveCookies方法!");
		}
	}
	
	public void parameter(String key, String value){
		this.checkConn();
		conn.data(key, value);
	}
	
	public void header(String key, String value){
		this.checkConn();
		conn.header(key, value);
	}
	
	private void setTimeOut(int timeOut){
		this.checkConn();
		this.timeOut = timeOut;
		conn.timeout(this.timeOut);
	}
	
	private void setCookies(){
		this.checkCookies();
		conn.cookies(cookies);
	}
	
	private void saveCookies(){
		this.checkResponse();
		if(isPost){
			cookies = response.cookies();
		}else{
			throw new DefinedException("这是Get方法，没有cookies可以保存!");
		}
	}	
	
	private void submit(){
		this.checkConn();
		if(isGet && !isPost){
			conn.method(Method.GET);
		}else if(isPost && !isGet){
			conn.ignoreContentType(true).method(Method.POST);
		}else{
			throw new DefinedException("请调用post或get方法!");
		}
		try {
			response = conn.execute();
			this.setResponseToDocument();
		} catch (IOException e) {			
			throw new DefinedException("请求过程出错！url: "+ url);
		}		
	}
	
	private void setResponseToDocument(){
		DocumentAction da = (DocumentAction) ConstantUtil.ALL_COMPONENT.get(DocumentAction.class.getSimpleName());
		da.setResponse(response);
	}

/*	public String postBody(String url, String rawBody){
		HttpURLConnection conn = null;
		PrintWriter pw = null ;
		BufferedReader rd = null ;
	    StringBuilder sb = new StringBuilder ();
	    String line = null ;
	    String response = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(Config.timeout);
			conn.setConnectTimeout(Config.timeout);
			conn.setUseCaches(false);
			conn.connect();
			pw = new PrintWriter(conn.getOutputStream());
			pw.print(rawBody);
			pw.flush();
			rd  = new BufferedReader( new InputStreamReader(conn.getInputStream(), "UTF-8"));
			while ((line = rd.readLine()) != null ) {
                sb.append(line);
            }
			response = sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{		
			try {
				if(pw != null){
					pw.close();
				}
				if(rd != null){
					rd.close();
				}
				if(conn != null){
					conn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}	*/
	
	public String postBody(String url, String rawBody) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();  
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);
		String response = null;
		try {
			StringEntity s = new StringEntity(rawBody.toString());
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");
			post.setEntity(s);
			HttpResponse res = httpClient.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = res.getEntity();
				Header encodingHeader = entity.getContentEncoding();
				if (encodingHeader != null) {
		            String encodingValue = encodingHeader.getValue();
		            if (encodingValue != null) {
		                if (encodingValue.contains("gzip")) {
		                    return unGzipInputStream(entity.getContent());
		                }
		            }
		        }
				response = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			throw new DefinedException("请求过程出错！url: "+ url+" with: "+rawBody+" error: "+e.getMessage());
		}
		return response;
	}
	
	public String unGzipInputStream(InputStream inputStream) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPInputStream gzipInputStream = null;
		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int c;
			byte[] buff = new byte[256];
			while ((c = inputStream.read(buff)) > 0){
				buf.write(buff, 0, c);
			}
			gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(
					buf.toByteArray()));
			byte[] buffer = new byte[256];
			int count = 0;
			while ((count = gzipInputStream.read(buffer)) >= 0) {
				out.write(buffer, 0, count);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} finally {
			try {
				gzipInputStream.close();
			} catch (Exception e) {
			}
		}

		String outStr = null;
		try {
			outStr = out.toString("UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return outStr;
	}
	
}
