package com.commons.study.webserver.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.filesystem.ActionFileSystem;
import com.commons.study.filesystem.FileSystemAction;
import com.commons.study.springmvc.annotation.Controller;
import com.commons.study.springmvc.annotation.RequestMapping;
import com.commons.study.springmvc.servlet.DispatcherServlet;
import com.commons.study.webserver.entity.HttpContext;
import com.commons.study.webserver.util.FileUtil;
import com.commons.study.webserver.util.XmlUtil;

//测试
public class TestDemo {

	static final Logger log = LoggerFactory.getLogger(HttpContext.class);

	@Test
	public void test() throws ParseException, UnknownHostException, IOException {

		String src = "今天的天气真的不好";

		byte[] buff = new byte[8];

		InputStream is = new ByteArrayInputStream(src.getBytes("utf-8"));

		int len = -1;

		while (-1 != (len = is.read(buff))) {

			String str1 = new String(buff, 0, len);//new String(buff, 0, len, "UTF-8")这是两个方法的合体

			String str2 = new String(buff, "UTF-8");

			System.out.println(str1);

			System.out.println(str2);

		}

	}

	@Test
	public void test1() throws IOException {
		String str = "\r\n";
		BufferedInputStream bf = new BufferedInputStream(new FileInputStream("D:/test.txt"));
		int n = bf.read();
		while (n != -1) {
			n = bf.read();
			System.out.println(((char) n) + "--------" + n);

		}
		bf.close();
	}

	@Test
	public void test3() throws UnsupportedEncodingException {
		String str = "dflx\r\nfghh\r\ngh\r\n";
		StringBuilder sb = new StringBuilder();
		String[] st = str.split("\r\n", -1);

		for (int i = 0; i < st.length; i++) {
			sb.append(st[i]);
			sb.append("\r\n");

		}

		if (st[st.length - 1].equals(""))
			sb.delete(sb.toString().length() - 2, sb.toString().length());

		byte[] len = sb.toString().getBytes("utf-8");
		System.out.println(sb + "------rrrr--------" + len.length + "--" + sb.toString().equals(str));

	}

	@Test
	public void test5() {
		XmlUtil xml = new XmlUtil();
		String path = TestDemo.class.getClassLoader().getResource("conf/filesysconf.xml").getPath();
		Map<String, String> map = xml.parseXmlToMap(path);

	}

	@Test
	public void test6() {

		XmlUtil xml = new XmlUtil();
		String path = TestDemo.class.getClassLoader().getResource("conf/springmvc.xml").getPath();
		Map<String, List<String>> map = xml.parseSpringmvcXml(path);
	}

	@Test
	public void annotation() {

		Class cla = FileSystemAction.class;
		Class cla2 = ActionFileSystem.class; //首先可以通过 Class 对象的 isAnnotationPresent() 方法判断它是否应用了某个注解
		System.out.println(
				cla.isAnnotationPresent(Controller.class) + "----" + cla2.isAnnotationPresent(Controller.class));

		Method[] methods = cla.getDeclaredMethods();
		for (Method m : methods) {

			if (m.isAnnotationPresent(RequestMapping.class)) { //有注解的方法。			
				Annotation[] anns = m.getAnnotations();
				for (Annotation ann : anns) {

					String url = ((RequestMapping) ann).value();
					System.out.println(url + "----" + ann.annotationType().getSimpleName() + "  ---- " + m.getName());
				}

				m.setAccessible(true); //访问可见性质
			}

		}

	}
	
	
	@Test
	public void testfile() {
		
		new FileUtil().getRealClassPath("com.commons.study.filesystem");
	}
	
	@Test
	public void mvc() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		 DispatcherServlet d=new DispatcherServlet();
		 d.init();
	}

	
	@Test
	public void  testmvc() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		String clas="com.commons.study.filesystem.FileSystemAction";
		
		Class<?> cla;
		
			cla = Class.forName(clas);
			//		   cla=ActionFile.class;
			Object obj = cla.newInstance();

			Method[] ms = cla.getDeclaredMethods();
			for (Method m : ms) { //把request,response传递过去。
				if (m.getName().equals("show")) {
					Class[] parameterTypes = m.getParameterTypes();
					
					 for (int i=0;i<parameterTypes.length;i++){
				            //获取方法名
				            String parameterType =  parameterTypes[i].getSimpleName();

				            System.out.println(parameterType+"------"+parameterTypes[i].getName());
					 }
				}	
			}
	}
	
	
	
}
