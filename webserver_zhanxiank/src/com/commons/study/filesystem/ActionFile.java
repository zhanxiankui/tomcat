package com.commons.study.filesystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.entity.HttpContext;
import com.commons.study.webserver.entity.MyFile;
import com.commons.study.webserver.net.HttpRequest;
import com.commons.study.webserver.net.HttpResponse;
import com.commons.study.webserver.util.FileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ActionFile {

	private HttpRequest request;

	private HttpResponse response;

	private static final Logger log = LoggerFactory.getLogger(ActionFile.class);
	

	//show.do?path=c/
	public void show(Object pa) throws IOException {
       
		String path=String.valueOf(pa);
		int index=String.valueOf(path).indexOf("@");   //这是初始化的标志。
		if(index!=-1){
			path=path.substring(0, index);
			response.getStaticResource(HttpContext.getInstance().getType("html"),  "ts/filesys.html");
			return;
		}
		 
		File fle = new File((String) path);
		ArrayList<String> sb = new ArrayList<>(); //还回一个对应的json文件
		
	    

		if (fle.isFile()) { //如果是文件,不要取下一级		
			sb.add(FileUtil.getJsonInfo(fle));
		}
		else {
			ArrayList<MyFile> list = FileUtil.getDirFils((String) path); //获取下一级目录	

			if (list.size() > 0) { //下一级目录有文件			
				for (MyFile my : list) {
					sb.add(FileUtil.getJsonInfo(my.getFile()));
				}

			}
			else { //下一级目录为空				
				sb.add(FileUtil.getJsonInfo(fle));
			}

		}
		
		 ObjectMapper mapper=new ObjectMapper();
		response.responseJson(mapper.writeValueAsString(sb));

	}

	//download.do?path=
	public void download(Object path) throws Exception {
		log.info("{} 开始下载文件", new Date());
		File file = new File(HttpContext.webdir + "/" + path);
		String type = request.getContentType() == null ? "html" : request.getContentType();
		String contentType = HttpContext.getInstance().getType(type);

		if (file.length() == 0) {
			response.getStaticResource("text/html; Charset=UTF-8", "404.html");
		}
		response.responseFile(contentType, file);
	}

	//edit.do?path
	public void edit(Object path) throws IOException {
		log.info("编辑文件开始");
		File file = new File(HttpContext.webdir + "/" + path);
		String name = (String) path;
		String type = name.substring(name.indexOf(".") + 1);

		if (!Arrays.asList(HttpContext.editTypeFile).contains(type)) {
			return; //不能编辑的不处理。
		}

		String contentType = HttpContext.getInstance().getType(type);
		response.getStaticResource(contentType, (String) path);

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
