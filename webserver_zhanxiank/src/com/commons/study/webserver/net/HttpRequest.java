package com.commons.study.webserver.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.entity.HttpContext;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.KebabCaseStrategy;

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

	private Map<String, String> parametes; //请求的参数。

	private String resource; //请求资源。

	private String requestDatas; //http请求所有的信息。

	private String requestbody; //请求体

	static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

	public HttpRequest(InputStream input) throws Exception {
		this.inputstream = input;
		this.parametes = new HashMap<String, String>();
		this.header = new HashMap<String, String>();
		parseRequest(input);
		parsePostBody();
	}

	public void parseUrl(String line) throws Exception {
		if (line != null && line.length() > 0) {
			String[] temp = line.split(" ");
			this.requestMethod = temp[0];
			this.url = URLDecoder.decode(temp[1], "UTF-8"); //前端进行了utf-8编码，这里解码防止乱码。
			this.protocol = temp[2];
			int index = this.url.indexOf("?");
			if (index != -1) {
				String str = this.url.substring(index + 1);
				parseParameters(str);
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

	public void readAllData(InputStream in) throws IOException {
		int count = 1024;
		byte[] b = new byte[count];
		int readCount = 0; // 已经成功读取的字节的个数
		while (readCount < count) {
			readCount += in.read(b, readCount, count - readCount);
			if (readCount == -1) {
				break;
			}
		}
	}

	public void parseRequest(InputStream stream) throws Exception {
		StringBuffer sb = new StringBuffer();
		int len = 0;
		while (len == 0) {
			len = stream.available(); //能够读到的字节数，最大为int的最大值。至少读一次，数据可能缺少。
		}
		byte[] b = new byte[len];
		stream.read(b);
		sb.append(new String(b, 0, b.length, HttpContext.Encoder));
		if (sb.length() == 0) {
			return;
		}
		this.requestDatas = sb.toString();
		String[] rqline = null;
		if (this.requestDatas.length() > 0) {
			rqline = requestDatas.split("\r\n"); //会拆掉空字符，导致换行吃掉需要注意。
			this.parseUrl(rqline[0]); //第一行数据解析。
		}

		StringBuffer body = new StringBuffer();
		int headBlen = rqline[0].length() + 2; //一个英文占用一个字节
		int j = 1;
		if (rqline != null) {
			for (j = 1; j < rqline.length; j++) { //头部解析数据
				//				log.info("头部信息 {}", rqline[j]);
				if ("".equals(rqline[j]) || rqline[j].startsWith("--")) { //post请求的换行被拆分成空字符。
					headBlen += 2;
					break;
				}

				if (rqline[j] != null) {
					headBlen += (rqline[j].length() + 2);
					if (rqline[j].indexOf(":") > 0) {
						String[] strs = rqline[j].split(":");
						header.put(strs[0].toLowerCase(), strs[1].trim());
					}
				}
			}

			for (int k = j + 1; k < rqline.length; k++) { //body的信息解析	
				body.append(rqline[k]);
				if (k < rqline.length - 1) //最后一个不是换行不能追加。
				{
					body.append("\r\n");
				}
			}
			if (rqline[rqline.length - 1].equals("")) { //补充上空格
				body.append("\r\n");
			}
		}

		if (getMethod().equals("GET")) { //POST请求才继续。
			return;
		}

		log.info("请求的头部长度为:{} ,已经读取的body长度:{}  总的读取长度:{} 计算的长度为:{}", headBlen, body.toString().length(), len,
				len - headBlen);
		int bodyLen = body.toString().length();
		int min = Math.min(len - headBlen, bodyLen); //相等就计算准确，否则取小的哪一个。	
		String contentLength = header.getOrDefault("content-length", "0"); //body体的长度,排除没有这个属性。
		Long conlen = Long.parseLong(contentLength); //118,960,870
		boolean mark = conlen > min;
		int num = (bodyLen == 0) ? 0 : min; //body没有内容直接指定为0
		long count = 0;
		while (bodyLen == 0 || mark) { //在读
			len = stream.available();
			byte[] tb = new byte[len];
			stream.read(tb);
			body.append(new String(tb, 0, len, HttpContext.Encoder));
			num += len;
			mark = conlen > num;
			log.debug(count + " 在读取body体里面的内容： 读取进度为-----------------: {}", num * 1.00 / conlen);
			bodyLen = num;
			if (len == 0) {
				count++; //20次读取后没有反应就终止程序,休眠一下等待数据, 测试拿数据没有，通过调整这个次数给予反应
				Thread.sleep(1000);
				if (count == 30)
					break;
				if (conlen > 118960870 * 3) {
					inputstream.close(); //大文件300m医生，可能阻塞，关闭流。
				}
			}
		}
		requestbody = body.toString(); //post请求有相关的东西。
		log.info("请求的contentLength是 {}, 已经读取的body大小为: {}", contentLength, requestbody.length());
	}

	/**
	 * 解析post请求，这里解析的是普通post请求。
	 */
	public void parsePostBody() {
		if (this.requestbody == null || requestbody.length() == 0) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		String contentType = header.get("content-type");
		//POST请求，Content-type为 multipart/form-data发送文件数据。application/x-www-form-urlencoded为普通表单提交 
		if ("POST".equals(this.getMethod()) && contentType != null
				&& contentType.startsWith("application/x-www-form-urlencoded")) {
			String[] rqline = requestbody.split("\r\n");
			for (int i = 0; i < rqline.length; i++) {
				if (!rqline[i].equals("")) {
					sb.append(rqline[i]);
				}
			}
			parseParameters(sb.toString());
		}
	}

	public void setResource(String name) {
		this.resource = name;
	}

	public String getResource() {
		return this.resource;
	}

	/**
	 * 解析请求的参数
	 * @param str
	 */
	private void parseParameters(String str) {
		if (str != null) {
			String[] temp = str.split("&");
			for (String ts : temp) {
				String[] t = ts.split("=");
				if (t.length == 2) {
					parametes.put(t[0], t[1]);
				}
				else if (t.length == 1) {
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
	public void setContentType(String value) {

		this.contenType = value;
	}

	public String getRequestDatas() {
		return requestDatas;
	}

	public String getRequestbody() {
		return requestbody;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

}
