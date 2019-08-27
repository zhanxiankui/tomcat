package com.commons.study.webserver.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

	private static final String port = HttpContext.fileConfMap.get("port"); //端口

	private static final String threadNum = HttpContext.fileConfMap.get("threadnum");

	private boolean isShutDown = false; //是否关闭

	private static final Map<String, List<String>> sprConf = HttpContext.sprConf; //springmvc的配置文件
	
	private  ArrayList<Object> list=new ArrayList<Object>();

	static final Logger log = LoggerFactory.getLogger(WebServer.class);

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {

		WebServer webServer = new WebServer();
		webServer.init();
		webServer.startServer();

	}

	private void startServer() throws InterruptedException, ExecutionException, IOException {
		ServerSocket serverSocket = null;
		Socket socket = null;
		ExecutorService threadPool = null;
		int count = 0;
		try {
			log.info("服务器启动成功");
			serverSocket = new ServerSocket(Integer.parseInt(port), 1, InetAddress.getByName("127.0.0.1"));
			threadPool = Executors.newFixedThreadPool(Integer.parseInt(threadNum));
			while (!isShutDown) {
				log.info("开启{}次连接:", count++);
				socket = serverSocket.accept(); //会阻塞在这里		
				socket.setSoTimeout(6000);
				Future<String> future = threadPool.submit(new HttpServer(socket, list));
			}
		}
		catch (Exception e) {
			log.error("服务器异常：", e);
			serverSocket.close();
		}
		finally {
			if (!socket.isClosed()) {
				socket.close();
			}
		}
	}

	/**
	 *  初始化web服务器的一些类，配置在xml文件中。
	 */

	private void init() {
		for (String className : sprConf.get("servlet-class")) {
			Class<?> cla;
			try {
				cla = Class.forName(className);
				Object obj = cla.newInstance();
				list.add(obj);
			}
			catch (Exception e) {
				log.error("初始化类发送错误 ： {}", e);
			}
		}
	}
}
