package com.commons.study.springmvc.servlet;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.commons.study.springmvc.annotation.Controller;
import com.commons.study.springmvc.annotation.RequestMapping;
import com.commons.study.webserver.entity.HttpContext;
import com.commons.study.webserver.net.HttpRequest;
import com.commons.study.webserver.net.HttpResponse;
import com.commons.study.webserver.net.HttpServlet;
import com.commons.study.webserver.util.FileUtil;

/**
 * 核心的servlet,处理路径映射路径，分发等。继承自 HttpServlet
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author zhanxiank
 * @createdate 2019年8月26日
 */
public class DispatcherServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

	private static final Map<String, List<String>> sprConf = HttpContext.sprConf; //springmvc的配置文件

	private static List<String> className = new LinkedList<String>();

	private static ConcurrentHashMap<String, Method> requestMap = new ConcurrentHashMap<>(); //url和method的map

	private static Map<String, String> urlclass = new HashMap<String, String>(); // url和class的map。

	static {
		init();
	}

	/**
	 * 初始化url和method，class对应的map。
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */

	public static void init() {
		List<String> scanPages = sprConf.get("component-scan");
		for (String pages : scanPages) {
			if(pages==null){
				continue;
			}
			className.addAll(FileUtil.getRealClassPath(pages));
		}
		for (String strcla : className) {
			Class<?> cla = null;
			try {
				cla = Class.forName(strcla);
			}
			catch (ClassNotFoundException e) {
				log.error("反射出现问题", e);
			}
			if (cla.isAnnotationPresent(Controller.class)) { //有注解的类
				RequestMapping rootmap = cla.getAnnotation(RequestMapping.class);
				String rooturl = "";
				if (rootmap != null) {
					rooturl = rootmap.value(); //类上面的
					if (!rooturl.startsWith("/")) {
						rooturl = "/" + rooturl;
					}
				}
				Method[] methods = cla.getDeclaredMethods();
				for (Method m : methods) {
					if (m.isAnnotationPresent(RequestMapping.class)) { //有注解的方法。
						RequestMapping mapping = m.getAnnotation(RequestMapping.class);
						if (mapping != null) {
							String methodUrl = mapping.value(); //方法上面的。
							if (!methodUrl.startsWith("/")) {
								methodUrl = "/" + methodUrl;
							}
							requestMap.put(rooturl + methodUrl, m); //把路径和方法存放进去
							urlclass.put(methodUrl, strcla); //url 和类对应。
						}
					}
				}

			}
		}
	}

	@Override
	public void doGet(HttpRequest req, HttpResponse resp) throws Exception {
		super.doGet(req, resp); //调用父类的方法。
		doDispatch(req, resp);

	}

	@Override
	public void doPost(HttpRequest req, HttpResponse resp) throws Exception {
		this.doPost(req, resp);
	}

	private void doDispatch(HttpRequest request, HttpResponse response) {
		String url = request.getRequestURL();
		if (url != null&&!isfilterUrl(url)) {
			Class<?> cla;
			try {
				log.info("请求的类是:{} 请求的方法：{}",urlclass.get(url),requestMap.get(url));
				cla = Class.forName(urlclass.get(url));
				Object obj = cla.newInstance();
				Method m = requestMap.get(url);
				Class[] parms = m.getParameterTypes(); //获取方法的参数。
				//方法反射调用需要的参数 Object[]  里面放从请求过来的参数
				Object[] methodParameter = new Object[parms.length];
				for (int i = 0; i < parms.length; i++) {
					String parmName = parms[i].getSimpleName();
					if (parmName.equals("HttpRequest")||parmName.equals("Request")) {
						methodParameter[i] = request;
					}
					if (parmName.equals("HttpResponse")||parmName.equals("Response")) {
						methodParameter[i] = response;
					}
				}
				m.invoke(obj, methodParameter);
				log.info("{}执行成功了", requestMap.get(url));
			}
			catch (Exception e) {
				log.error("方法执行异常 {}", e);
			}

		}
	}
	
	/**
	 *  拦截路径，可以定义一个正则表达式
	 * @param url
	 */
	private boolean isfilterUrl(String url) {
		for(String st: sprConf.get("url-pattern")){
			if(url.equals("/")){  //直接通过
				return false;   
			}
			if(st.indexOf(".")>-1&&url.endsWith(st.substring(st.indexOf(".")+1))){  //允许的后缀，通过。
				return false;
			}
			
			if(url.equals("/files")){
				return  false;
			}
		}
		return true;	
	}	
}
