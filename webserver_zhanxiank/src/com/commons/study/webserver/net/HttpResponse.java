package com.commons.study.webserver.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.threedemo.FileToByte;
import com.commons.study.webserver.entity.HttpContext;

/**
 * 相应类，返回信息给浏览器。
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月6日
 */
public class HttpResponse implements Response {
	static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

	private OutputStream outputStream;

	private Map<String, String> header;

	private HttpContext httpContext;
	
	private String status; //状态码
	
	private String contentype; //文件类型

	public HttpResponse(OutputStream outputStream) {
		this.outputStream = outputStream;
		this.header = new HashMap<String, String>();
		this.httpContext = HttpContext.getInstance();
	}
	
	
	/**
	 * 返回json字符串
	 * @throws IOException 
	 */
	public void responseJson(List<String> list) throws IOException{
		log.info("返回json形式的字符串");
		
		if(list.size()>0){
			status = 200 + " " + httpContext.getStats("200");
			writeHeader(HttpContext.getInstance().getType("json"), status);	
		}
		
		for(String file:list){
			outputStream.write(file.getBytes());	
		}				
		
	}
	
	

	/**
	 * 返回资源
	 * @throws IOException 
	 */
	public void getStaticResource(String contentType, String name) throws IOException {

		log.info("请求的资源为: {}", name);
		String pathname = HttpContext.webdir;
		File file = new File(pathname + "/" + name);
		String status = "200";
		setStatus("200");
		setContentType(contentType);
		if (file.exists() && file.isFile()) {
			status = status + " " + httpContext.getStats(status);
			writeHeader(contentType, status);
			writeFile(file);
		}
		else {
			status = "404" + " " + httpContext.getStats("404");
			writeHeader(contentType, status);
			writeFile(new File(pathname + "/404.html"));
		}
		  
		outputStream.close();
	}

	public void writeFile(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			FileToByte fby = new FileToByte();
			outputStream.write(fby.file2buf(file));

			
			log.info("文件输出完毕");

		}
		catch (Exception e) {
			log.error("写文件出错{}", e);
		}
		finally {
			if (fis != null)
				try {
					fis.close();
				}
				catch (IOException e) {
					log.error("文件流的关闭出现问题: {}", e);
				}
		}
	}

	public void writeHeader(String contentType, String status) {

		String first = "HTTP/1.1 " + status + "\r\n";
		String responseHeader = "Content-Type:" + contentType + "\r\n";

		log.info("返回头部信息{} ", responseHeader);
		try {
			outputStream.write(first.getBytes());
			outputStream.write("accept-ranges: bytes \r\n".getBytes());
			outputStream.write(responseHeader.getBytes());
			outputStream.write("\r\n".getBytes());
		}
		catch (Exception e) {
			log.error("头部信息输出出错: {}", e);
		}

	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return outputStream;
	}


	@Override
	public void setCharacterEncoding(String charset) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return contentype;
	}

	@Override
	public void setContentType(String type) {
		this.contentype=type;
	}

	@Override
	public void setHeader(String name, String value) {
		// TODO Auto-generated method stub
       header.put(name, value);
	}

	@Override
	public void addHeader(String name, String value) {
		header.put(name, value);

	}
	
	

	@Override
	public Map<String, String> getHeader(String name) {
		// TODO Auto-generated method stub
		return this.header;
	}

	@Override
	public void setStatus( String status) {
       this.status=status;
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return this.status;
	}

}
