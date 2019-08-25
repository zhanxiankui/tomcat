package com.commons.study.webserver.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.entity.HttpContext;


/**
 * web 服务器
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月6日
 */
public class WebServer {

	private static final int port = 8089; //端口

	private boolean isShutDown = false; //是否关闭

	static final Logger log = LoggerFactory.getLogger(WebServer.class);

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {

		WebServer webServer = new WebServer();
		webServer.startServer();

	}
	
	

	private void startServer() throws InterruptedException, ExecutionException, IOException {
		ServerSocket serverSocket = null;
		ExecutorService threadPool = null;
		int count=0;
		try {
			log.info("服务器启动成功");
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
			threadPool = Executors.newFixedThreadPool(HttpContext.threadNums);
			while (!isShutDown) {
				log.info("开启{}次连接:",count++);
				Socket socket  = serverSocket.accept(); //会阻塞在这里		
				Future<String> future = threadPool.submit(new HttpServer(socket));
				socket.setSoTimeout(6000);
			}
		}
		catch (Exception e) {
			log.error("服务器异常：", e);
			serverSocket.close();
		}

	}

}
