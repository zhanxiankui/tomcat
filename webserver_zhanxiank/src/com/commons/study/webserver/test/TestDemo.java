package com.commons.study.webserver.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.entity.HttpContext;
import com.commons.study.webserver.entity.MyFile;
import com.commons.study.webserver.entity.TreeNode;
import com.commons.study.webserver.net.HttpRequest;
import com.commons.study.webserver.net.HttpResponse;
import com.commons.study.webserver.server.HttpServer;
import com.commons.study.webserver.util.FileUtil;

//测试
public class TestDemo {

	static final Logger log = LoggerFactory.getLogger(HttpContext.class);

	@Test
	public void test() throws ParseException, UnknownHostException, IOException {

		ServerSocket serverSocket = null;

		serverSocket = new ServerSocket(8089, 1, InetAddress.getByName("127.0.0.1"));

		while (true) {

			Socket socket = serverSocket.accept(); //会阻塞在这里		

			try {
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				HttpRequest req = new HttpRequest(inputStream);

				if (req.getRequestURL() != null) {
					HttpResponse response = new HttpResponse(outputStream);
					String contentType = HttpContext.getInstance().getType(req.getContentType());
					if (contentType == null || contentType.equals("")) {
						contentType = "text/plain";
					}

					response.setContentType(req.getContentType());
					response.setHeader("Content-Type", contentType);
					response.getStaticResource(contentType, req.getResource());
				}

			}
			catch (Exception e) {

				log.error("出现问题", e);

			}

		}

	}
	
	
	
	@Test
	public void test1() {
		
	   ArrayList<MyFile>  list= FileUtil.getSameDirFils("D:/succezIDE/gitpose");
	   
	   for(MyFile f:list){
		   System.out.println(f.getFileName()+"  "+f.getPath()+"  "+f.getParentdrenDir());
	   }
		
	}
	
	
	@Test
	public void test3() {
		
//	Tree tree=new Tree("D:/", firstChild, nextSibling);
		
		
	}

}
