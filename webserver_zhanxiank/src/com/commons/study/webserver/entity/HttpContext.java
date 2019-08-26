package com.commons.study.webserver.entity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.test.TestDemo;
import com.commons.study.webserver.util.XmlUtil;

/**
 * 单列模型，常量类。
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月6日
 */

public class HttpContext {

	static final Logger log = LoggerFactory.getLogger(HttpContext.class);

	private static HttpContext hContext = new HttpContext();

	private Properties p = new Properties(); //这是content-type的配置文件。

	private Properties p1 = new Properties(); //这是状态的配置文件。

	//资源存放的路径
	public static String webdir = HttpContext.class.getClassLoader().getResource("resource").getPath();

	public static final int ThreadNums = 6; //线程池的线程数量。
	
	public  static final String Encoder="ISO_8859_1";
	
	public static final  String  UTF8="UTF-8";

	public static final String[] editTypeFile = { "html", "txt", "js", "css", "xml" }; //可以编辑的文件类型。
	
	public static  Map<String, String> fileConfMap;  //存放文件系统配置文件。
	
	static{
		init();
	}
	

	private HttpContext() {
		loadPropeyty();
	}

	public static HttpContext getInstance() {
		if (hContext == null) {
			return new HttpContext();
		}
		return hContext;
	}

	public String getType(String key) {
		return p.getProperty(key);
	}

	public String getStats(String key) {
		return p1.getProperty(key).trim();
	}

	private void loadPropeyty() {
		InputStream in = null;
		InputStream ins = null;
		try {
			in = HttpContext.class.getClassLoader().getResourceAsStream("conf/contenttype.properties");
			p.load(in);
			ins = HttpContext.class.getClassLoader().getResourceAsStream("conf/statas.properties");
			p1.load(ins);
			log.debug("加载配置文件{},{}成功", p, p1);
		}
		catch (FileNotFoundException e) {
			log.error("读取配置文件 出现异常:{}", e);
		}
		catch (IOException e) {
			log.error("读取配置文件 出现异常:{}", e);
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (IOException e) {
					log.warn("关闭配置文件 出现异常:{}", e);
				}
			}
			if (ins != null) {
				try {
					ins.close();
				}
				catch (IOException e) {
					log.warn("关闭配置文件 出现异常:{}", e);
				}
			}
		}

	}

	
	
	private  static void init() {
		XmlUtil xml=new XmlUtil();
		String path=HttpContext.class.getClassLoader().getResource("conf/filesysconf.xml").getPath();
		fileConfMap=xml.parseXmlToMap(path);	
	}
	
}
