package com.commons.study.webserver.net;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.filesystem.FileServer;
import com.commons.study.webserver.action.ActionShowFile;
import com.commons.study.webserver.entity.HttpContext;
import com.commons.study.webserver.util.FileUtil;

public class HttpServlet {
	
	private static final Logger log = LoggerFactory.getLogger(HttpServlet.class);

	public void doGet(HttpRequest req, HttpResponse response) throws Exception {   //需要做成   https://www.cnblogs.com/xifengxiaoma/p/9451044.html

		String url = req.getRequestURL();	
		if (url != null) {           
			String type = req.getContentType() == null ? "html" : req.getContentType();
			String contentType = HttpContext.getInstance().getType(type);
			if (contentType == null || contentType.equals("")) {
				contentType = "text/plain";
			}
			if (!"/files".equals(url) && !"do".equals(type) && !"/project".equals(url)) //加载静态资源
			{
				response.setContentType(req.getContentType());
				response.setHeader("Content-Type", contentType);
				response.getStaticResource(contentType, HttpContext.webdir + "/" + req.getResource());
			}	
		}		
	}



	public void doPost(HttpRequest req, HttpResponse resp) throws Exception {	
		doGet(req, resp);
	 }
}
