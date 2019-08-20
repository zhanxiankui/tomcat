package com.commons.study.filesystem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

		String path = String.valueOf(pa);
		int index = String.valueOf(path).indexOf("@"); //这是初始化的标志。
		if (index != -1) {
			path = path.substring(0, index);
			response.getStaticResource(HttpContext.getInstance().getType("html"),
					HttpContext.webdir + "/" + "ts/filesys.html");
			return;
		}

		File fle = new File((String) path);
		ArrayList<String> sb = new ArrayList<>(); //还回一个对应的json文件

		if (fle.isFile()) { //如果是文件,不要取下一级	,还回资源提供查看。		
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
				//				sb.add(FileUtil.getJsonInfo(fle));
				return;
			}

		}

		ObjectMapper mapper = new ObjectMapper();
		response.responseJson(mapper.writeValueAsString(sb));

	}

	//save.do
	public void save(Object path) throws IOException {
		String data = request.getParameter("data");
		String status = "ok";
		try {
			FileUtil.writeFile(FileUtil.getRealFileLocate(path), data);
		}
		catch (IOException e) {
			log.error(e.toString());
			status = "bad";
		}
		Map<String, String> smap = new HashMap<>();
		smap.put("status", status);
		response.responseJson(new ObjectMapper().writeValueAsString(smap));
		; //返回保存的状态
	}

	
	///watchFile.do
	public void watchFile(Object path) throws IOException {
		String url = String.valueOf(path);
		String contentype = HttpContext.getInstance().getType(url.substring(url.indexOf(".") + 1));
		response.getStaticResource(contentype, url);
	}

	
	//download.do?path=
	public void download(Object path) throws Exception {
		log.info("{} 开始下载文件", new Date());
		String type = request.getContentType() == null ? "html" : request.getContentType();
		String contentType = HttpContext.getInstance().getType(type);

		if (FileUtil.getRealFileLocate(path).length() == 0) {
			response.getStaticResource("text/html; Charset=UTF-8", HttpContext.webdir + "/" + "404.html");
		}
		response.responseFile(contentType, FileUtil.getRealFileLocate(path));
	}

	//edit.do?path
	public void edit(Object path) throws IOException {
		log.info("编辑文件开始");
		String url = String.valueOf(path);
		String type = url.substring(url.indexOf(".") + 1);

		if (!Arrays.asList(HttpContext.editTypeFile).contains(type)) {
			return; //不能编辑的不处理。
		}
		response.responseHtml(FileUtil.fileToString(FileUtil.getRealFileLocate(path)));
	}

	//upload.do?path
	public void upload(Object obj) throws IOException {
		log.info("上传文件 {}",obj);
		
		
		BufferedInputStream bfiput=new BufferedInputStream(request.getInputStream());
		String fileName=request.getParameter("fileName");
		byte[] buf=new byte[1024];

		File file=new File(obj+fileName);
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
		try{
		int read=-1;
		while ((read=bfiput.read(buf))!=-1) {
			bos.write(buf, 0, read);	
		}
		
		}finally {
			bos.close();
		}
	
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
