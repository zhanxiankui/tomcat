package com.commons.study.webserver.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.action.ActionShowFile;
import com.commons.study.webserver.entity.HttpContext;
import com.commons.study.webserver.net.HttpRequest;
import com.commons.study.webserver.net.HttpResponse;



/**
 *  服务器任务处理类，是多线程类型。
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月6日
 */
public class HttpServer implements Callable<String> {

	static final Logger log = LoggerFactory.getLogger(HttpServer.class);

	private Socket socket;

	public HttpServer(Socket socket) {
		this.socket = socket;
	}

	@Override
	public String call() {

		try {
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			HttpRequest req = new HttpRequest(inputStream);
            String url=req.getRequestURL();
            
			if ( url!= null) {
				HttpResponse response = new HttpResponse(outputStream);
				
				String type=req.getContentType()==null ? "html":req.getContentType();
				String contentType = HttpContext.getInstance().getType(type);
				if (contentType == null||contentType.equals("")) {
					contentType = "text/plain";
				}
	
				if(!"/files".equals(url))
				{
					response.setContentType(req.getContentType());
					response.setHeader("Content-Type", contentType);
					response.getStaticResource(contentType, req.getResource());
				}else{                //文件系统的展示
				
					if(req.getParameter("path").length()<=3){
					contentType="text/html; Charset=UTF-8";
					response.setContentType(req.getContentType());
					response.setHeader("Content-Type", contentType);
					response.getStaticResource(contentType, "file.html");
					}  else{
						
						ActionShowFile showfile=new ActionShowFile();
					    showfile.show(req, response);
					}
				
				     
				}
		
				
				
			}
			
			
			

		}
		catch (Exception e) {
			log.error("出现问题", e);
			return "bad";
		}

		return "success";
	}

}
