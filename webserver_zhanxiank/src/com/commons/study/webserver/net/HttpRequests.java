package com.commons.study.webserver.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.commons.study.webserver.entity.HttpContext;

/**
 *  请求类，封装了通过scoket解析自http请求的东西。
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月6日
 */

public class HttpRequests implements Request {

	private InputStream inputstream; //输入流

	private String protocol; //协议

	private String contenType; //类型

	private String characterEnconding; //编码

	private String url; //请求url

	private String requestMethod; //请求类型。

	private Map<String, String> header; //请求头部

	private Map<String, List<Object>> parametes; //请求的参数。

	private String resource; //请求资源。

	private String requestDatas; //http请求所有的信息。

	private String requestbody; //请求体

	static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

	public HttpRequests(InputStream input) throws IOException {
		this.inputstream = input;
		this.parametes = new HashMap<String, List<Object>>();
		this.header = new HashMap<String, String>();

		try {
			pareInformat(input);
		}
		catch (Exception e) {
			log.error("输入流解析有问题", e);
		}
	}

	public void getFirstHttpInfor(String line) throws Exception {

		log.info("第一行信息:{}", line);
		if (line != null && line.length() > 0) {
			String[] temp = line.split(" ");
			this.requestMethod = temp[0];
			this.url = URLDecoder.decode(temp[1], "UTF-8"); //前端进行了utf-8编码，这里解码防止乱码。
			this.protocol = temp[2];
			int index = this.url.indexOf("?");
			if (index != -1) {
				String str = this.url.substring(index + 1);
				setParameters(str);
				this.url = this.url.substring(0, index);
			}
			if ("/".equals(this.url)) { //没有url
				setResource("hello.html");
			}
			else {
				setResource(this.url.substring(this.url.indexOf("/") + 1));
			}

			if (this.resource.indexOf(".") > 0) {
				this.contenType = this.resource.substring(this.resource.lastIndexOf(".") + 1);
			}
			log.info("请求的方式 {} 请求的url{}  请求的资源{}", requestMethod, url, resource);
		}
	}

	public void pareInformat(InputStream stream) throws Exception {

		StringBuffer sb = new StringBuffer();
		int len = 0;  
	    while (len == 0) {  
	       len = stream.available();  
	    }  
	    
	    byte[] b = new byte[len];  
	    stream.read(b);  
		sb.append(new String(b,0,b.length, HttpContext.Encoder));
		
		if(sb.length()==0)
		{
			return;
		}		
	    this.requestDatas = sb.toString();
		String[] rqline = null;
		if (this.requestDatas.length() > 0) {
			rqline = requestDatas.split("\r\n");
			this.getFirstHttpInfor(rqline[0]); //第一行数据解析。
		}
		int count = 1;
		StringBuffer body =new StringBuffer();
		if (rqline != null) {
			for (int j = 1; j < rqline.length; j++) { //头部解析数据
				log.info("头部信息 {}", rqline[j]);
				if ("".equals(rqline[j])) {
					break;
				}
				count++;
				if (rqline[j] != null) {
					if (rqline[j].indexOf(":") > 0) {
						String[] strs = rqline[j].split(":");
						header.put(strs[0].toLowerCase(), strs[1].trim());
					}
				}
			}

			for (int k = count; k < rqline.length; k++) {   //body的信息解析
				body.append(rqline[k]);
			}

		}
		requestbody = body.toString();   //post请求有相关的东西。
	}

	public void setResource(String name) {

		this.resource = name;
	}

	public String getResource() {
		return this.resource;
	}

	/**
	 * 设置查询的参数
	 * @param str
	 */
	private void setParameters(String str) {

		if (str != null) {
			String[] temp = str.split("&");
			for (String ts : temp) {
				List<Object> list = new ArrayList<>();
				String[] t = ts.split("=");
				if (t.length == 2) {
					list.add(t[1]);

					if (parametes.containsKey(t[1])) { //有相同的key,value增加。
						list = parametes.get(t[0]);
						list.add(t[2]);
					}
					else {
						parametes.put(t[0], list);
					}
				}
				else {
					parametes.put(t[0], null);
				}
			}

		}
	}

	@Override
	public String getProtocol() {
		// TODO Auto-generated method stub
		return this.protocol;
	}

	@Override
	public String getContentType() {

		return this.contenType;
	}

	@Override
	public void setCharacterEncoding(String env) {
		this.characterEnconding = env;

	}

	@Override
	public String getCharacterEncoding() {
		return this.characterEnconding;
	}

	@Override
	public Map<String, String> getHeader() {
		// TODO Auto-generated method stub
		return this.header;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return this.requestMethod;
	}

	@Override
	public String getRequestURL() {

		return this.url;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return this.inputstream;
	}

	@Override
	public String getParameter(String name) {
		// TODO Auto-generated method stub
		if (parametes.get(name) == null)
			return null;

		return (String) parametes.get(name).get(0);
	}



	@Override
	public void setAttribute(String name, Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContentType(String value) {

		this.contenType = value;

	}
	
	
	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestDatas() {
		return requestDatas;
	}

	public String getRequestbody() {
		return requestbody;
	}


}
