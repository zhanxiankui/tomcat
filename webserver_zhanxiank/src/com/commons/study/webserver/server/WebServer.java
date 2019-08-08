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


/**
 * web 服务器
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月6日
 */
public class WebServer {

	private static final int port = 8080; //端口

	private boolean isShutDown = false; //是否关闭

	static final Logger log = LoggerFactory.getLogger(WebServer.class);

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		WebServer webServer = new WebServer();
		webServer.startServer();

	}
	
	

	private void startServer() throws InterruptedException, ExecutionException {
		ServerSocket serverSocket = null;
		ExecutorService threadPool = null;
		try {
			log.info("服务器启动成功");
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
			threadPool = Executors.newFixedThreadPool(12);
			while (!isShutDown) {
				log.info("等待客户端连接 :");
				Socket socket  = serverSocket.accept(); //会阻塞在这里		
				Future<String> future = threadPool.submit(new HttpServer(socket));
				log.debug("线程池是否关闭{}", threadPool.isShutdown());
			}

		}
		catch (Exception e) {
			log.error("服务器异常：", e);
		}

	}

}
