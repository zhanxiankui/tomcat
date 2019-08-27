package com.commons.study.webserver.test;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.net.HttpRequest;
import com.commons.study.webserver.net.HttpResponse;
import com.commons.study.webserver.net.Request;

public class FileServer {

	private static final Logger log = LoggerFactory.getLogger(FileServer.class);

	private Map<String, String> map;

	private HttpRequest request;

	private HttpResponse response;

	public FileServer(HttpRequest req, HttpResponse res) {
		log.info("文件系统服务启动");
		map = new ConcurrentHashMap<>();
		this.request = req;
		this.response = res;
		String url = req.getRequestURL();
		map.put(url, url.substring(1, url.indexOf(".")));
		map.put("req", "setRequest");
		map.put("res", "setResponse");
	}

	public void server() {
		String url = request.getRequestURL();
		String path = request.getParameter("path");
		handle("com.commons.study.filesystem.ActionFileSystem", map.get(url), path);

	}

	private void handle(String className, String methodName, Object parm) {

		Class<?> cla;
		try {
			cla = Class.forName(className);
			//		   cla=ActionFile.class;
			Object obj = cla.newInstance();

			Method[] ms = cla.getDeclaredMethods();
			for (Method m : ms) { //把request,response传递过去。
				if (m.getName().equals("setRequest")) {
					m.invoke(obj, request);
				}
				if (m.getName().equals("setResponse")) {
					m.invoke(obj, response);
				}
			}

			Method method = cla.getMethod(methodName, Object.class);
			method.invoke(obj, parm);
			log.info("{}执行成功了", methodName);
		}
		catch (Exception e) {
			log.error("方法执行异常 {}", e);
		}

	}

}
