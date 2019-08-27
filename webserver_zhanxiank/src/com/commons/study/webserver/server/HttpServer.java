package com.commons.study.webserver.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.commons.study.webserver.net.HttpRequest;
import com.commons.study.webserver.net.HttpResponse;
import com.commons.study.webserver.net.HttpServlet;

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
    private  List<Object> list; //初始化的bean
	
	public HttpServer(Socket socket,ArrayList<Object> list) {
		this.socket = socket;	 
		this.list=list;
	}

	@Override
	public String call() throws Exception {
		InputStream inputStream=null;
		OutputStream outputStream=null ;	
		try {		
			inputStream = socket.getInputStream();
			 outputStream = socket.getOutputStream();
			HttpRequest req = new HttpRequest(inputStream);
			HttpResponse response = new HttpResponse(outputStream);
			for(Object obj:list){		
				((HttpServlet) obj).doGet(req, response);
			}	
			socket.close();
			return "success";
		}
		catch (Exception e) {
			log.error("出现问题", e);
			return "bad";
		}
		finally {
			log.info("关闭连接");
			if(outputStream!=null){
				outputStream.close();
			}
			if (!socket.isClosed())
				socket.close();
		}	
	}

}
