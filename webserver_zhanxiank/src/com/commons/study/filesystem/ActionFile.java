package com.commons.study.filesystem;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.entity.HttpContext;
import com.commons.study.webserver.net.HttpRequest;
import com.commons.study.webserver.net.HttpResponse;

public class ActionFile {
	
   private	HttpRequest request;
   private HttpResponse response;
   
   private static final Logger log=LoggerFactory.getLogger(ActionFile.class);
	
	public void download(Object path) throws Exception {
		log.info("{} 开始下载文件",new Date());
		File file = new File(HttpContext.webdir + "/" + path);
		String type = request.getContentType() == null ? "html" : request.getContentType();
		String contentType = HttpContext.getInstance().getType(type);
		
		if(file.length()==0){
			response.getStaticResource("text/html; Charset=UTF-8", "404.html");
		}	
		response.responseFile(contentType, file);
	}
	

	public HttpRequest getRequest() {
		return request;
	}

	public void setRequest(Object request) {
		this.request = (HttpRequest) request;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = (HttpResponse) response;
	}


}
