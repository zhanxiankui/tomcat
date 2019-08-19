package com.commons.study.webserver.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  请求类，封装了通过scoket解析自http请求的东西。
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月6日
 */

public class HttpRequest implements Request {

	private InputStream inputstream; //输入流

	private String protocol; //协议

	private String contenType; //类型

	private String characterEnconding; //编码

	private String url; //请求url

	private String requestMethod; //请求类型。

	private Map<String, String> header; //请求头部

	private Map<String, List<Object>> parametes; //请求的参数。

	private String requestHeaders; //请求头。

	private String resource; //请求资源。

	static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

	public HttpRequest(InputStream input) throws IOException {
		this.inputstream = input;
		this.parametes = new HashMap<String, List<Object>>();
		this.header = new HashMap<String, String>();

		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		StringBuilder sb = new StringBuilder();
		String line = null;
		
	
		
		try {

			line = br.readLine(); //读取一行
			log.info("第一行信息:{}",line);
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

			sb.append(line).append("\n");
			while ((line = br.readLine()) != null && line.length() > 0) {
				log.info("第n行信息:{}",line);
				String key = line.substring(0, line.indexOf(":")).trim();
				String value = line.substring(line.indexOf(":") + 1).trim();
				this.header.put(key, value);
				sb.append(line).append("\n");
			}

			this.requestHeaders = sb.toString();
			log.info("request请求头部信息:{}", sb.toString());

		}
		catch (Exception e) {
			log.error("输入流解析有问题", e);
		}

	}

	
	public void getFirstHttpInfor(String line) throws Exception {
		
			log.info("第一行信息:{}",line);
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
	
	
	public void pareInformat(BufferedReader br) throws IOException {

		String contentType = header.get("Content-Type");
		String str = null;
		StringBuffer sb = new StringBuffer();
		//读取请求行 
		String requestLine = br.readLine(); //第一行数据
		if (requestLine != null) {
			sb.append(requestLine).append("\n");
			String[] reqs = requestLine.split(" ");
			if (reqs != null && reqs.length > 0) {
				if ("GET".equals(reqs[0])) {
					this.requestMethod = "GET";
				}
				else {
					this.requestMethod = "POST";
				}
			}
		}
		
		//读取请求头 
		while ((str = br.readLine()) != null) {
			if ("".equals(str)) {
				break;
			}
			if (str != null) {
				if (str.indexOf(":") > 0) {
					String[] strs = str.split(":");
					header.put(strs[0].toLowerCase(), strs[1].trim());
				}
			}
			sb.append(str).append("\n");
		}

		                                  //POST请求，Content-type为 multipart/form-data 
		if ("POST".equals(this.getMethod())&&contentType!=null && contentType.startsWith("multipart/form-data")) {
			//文件上传的分割位 这里只处理单个文件的上传 
			String boundary = contentType.substring(contentType.indexOf("boundary") + "boundary=".length());
			while ((str = br.readLine()) != null) {
				//解析结束的标记 
				do {
					//读取boundary中的内容 
					//读取Content-Disposition 
					str = br.readLine();
					//文件上传 的头部
					if (str.indexOf("Content-Disposition:") >= 0 && str.indexOf("filename") > 0) {
						str = str.substring("Content-Disposition:".length());
						String[] strs = str.split(";");
						String fileName = strs[strs.length - 1].replace("\"", "").split("=")[1];
						log.info("文件名称为:{}", fileName);
						//这一行是Content-Type 
						br.readLine();
						//这一行是换行 
						br.readLine();
						//正式去读文件的内容 
						BufferedWriter bw = null;
						try {
							bw = new BufferedWriter(new OutputStreamWriter(
									new FileOutputStream("D:\\" + File.separator + fileName), "UTF-8"));
							while (true) {
								str = br.readLine();
								if (str.startsWith("--" + boundary)) {
									break;
								}
								bw.write(str);
								bw.newLine();
							}
							bw.flush();
						}
						catch (Exception e) {

						}
						finally {
							if (bw != null) {
								bw.close();
							}
						}
					}
					if (str.indexOf("Content-Disposition:") >= 0) {
						str = str.substring("Content-Disposition:".length());
						String[] strs = str.split(";");
						String name = strs[strs.length - 1].replace("\"", "").split("=")[1];
						br.readLine();
						StringBuilder stringBuilder = new StringBuilder();
						while (true) {
							str = br.readLine();
							if (str.startsWith("--" + boundary)) {
								break;
							}
							stringBuilder.append(str);
						}
//						parametes.put(name, stringBuilder.toString());
					}
				}
				while (("--" + boundary).equals(str));
				//解析结束 
				if (str.equals("--" + boundary + "--")) {
					break;
				}
			}
		}

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
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		this.characterEnconding = env;

	}

	@Override
	public String getCharacterEncoding() {
		return this.characterEnconding;
	}

	@Override
	public Map<String, String> getHeader(String name) {
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
	public List<Object> getParameterValues(String name) {
		// TODO Auto-generated method stub
		return parametes.get(name);
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

}
