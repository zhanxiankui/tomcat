package com.commons.study.filesystem;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.commons.study.springmvc.annotation.Controller;
import com.commons.study.springmvc.annotation.RequestMapping;
import com.commons.study.webserver.entity.HttpContext;
import com.commons.study.webserver.entity.MyFile;
import com.commons.study.webserver.net.HttpRequest;
import com.commons.study.webserver.net.HttpResponse;
import com.commons.study.webserver.util.FileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ftp文件系统的controller
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author zhanxiank
 * @createdate 2019年8月27日
 */

@Controller
public class FileSystemAction {
	private static final Logger log = LoggerFactory.getLogger(FileSystemAction.class);

	//show.do?path=c/
	@RequestMapping("show.do")
	public void show(HttpRequest request, HttpResponse response) throws IOException {
		String path = request.getParameter("path");
		ObjectMapper mapper = new ObjectMapper();
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
			else { //下一级目录为空,需要还回一个信息给前台。						
				sb.add("nofile");
			}
		}
		response.responseJson(mapper.writeValueAsString(sb));
	}

	//save.do
	@RequestMapping("save.do")
	public void save(HttpRequest request, HttpResponse response) throws JsonProcessingException, IOException {
		String status = "ok";
		String path = request.getParameter("path");
		try {
			String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
			FileUtil.writeFile(FileUtil.getRealFileLocate(path), data);
		}
		catch (IOException e) {
			log.error(e.toString());
			status = "bad";
		}
		Map<String, String> smap = new HashMap<>();
		smap.put("status", status);
		response.responseJson(new ObjectMapper().writeValueAsString(smap)); //返回保存的状态

	}

	///watchFile.do
	@RequestMapping("watchFile.do")
	public void watchFile(HttpRequest request, HttpResponse response) throws IOException {
		String url = request.getParameter("path");
		String contentype = HttpContext.getInstance().getType(url.substring(url.indexOf(".") + 1));
		response.getStaticResource(contentype, url);
	}

	//download.do?path=
	@RequestMapping("download.do")
	public void download(HttpRequest request, HttpResponse response) throws Exception {
		log.info("{} 开始下载文件", new Date());
		String path = request.getParameter("path");
		String type = request.getContentType() == null ? "html" : request.getContentType();
		String contentType = HttpContext.getInstance().getType(type);
		if (FileUtil.getRealFileLocate(path).length() == 0) {
			response.getStaticResource("text/html; Charset=UTF-8", HttpContext.webdir + "/" + "404.html");
		}
		response.responseFile(contentType, FileUtil.getRealFileLocate(path));
	}

	//edit.do?path
	@RequestMapping("edit.do")
	public void edit(HttpRequest request, HttpResponse response) throws Exception {
		log.info("编辑文件开始");
		String url = request.getParameter("path");
		String type = url.substring(url.indexOf(".") + 1);
		if (!Arrays.asList(HttpContext.editTypeFile).contains(type)) {
			return; //不能编辑的不处理。
		}
		response.responseHtml(FileUtil.fileToUTFString(FileUtil.getRealFileLocate(url)));
	}

	@RequestMapping("upload.do")
	public void upload(HttpRequest request, HttpResponse response) throws IOException {
		log.info("上传文件 开始");
		String obj = request.getParameter("path");
		Map<String, String> formmap = new HashMap<>(); //放表单的参数。
		String body = request.getRequestbody(); //body数据。
		String contentType = request.getHeader().get("content-type");
		boolean cond = contentType != null && contentType.startsWith("multipart/form-data");

		if (body != null && cond) { //POST请求，Content-type为 multipart/form-data 
			String boundary = contentType.substring(contentType.indexOf("boundary") + "boundary=".length());
			String[] str = body.split("--" + boundary + "\r\n");
			String temp = null; //文件上传 的头部
			for (int i = 0; i < str.length; i++) { //读取内容。
				if (str[i].indexOf("--" + boundary + "--") > -1) { //防止下标越界。
					break;
				}

				if (str[i] != null && str[i].indexOf("Content-Disposition:") >= 0 && str[i].indexOf("filename") > 0) {
					String[] input = str[i].split("\r\n\r\n"); //在分一次，去掉了最后的空格
					temp = input[0].split("\r\n")[0].substring("Content-Disposition:".length());
					String[] strs = temp.split(";");
					DataOutputStream fio = null; //这一行是换行 ,正式去读文件的内容 
					try {
						String fn = strs[strs.length - 1].replace("\"", "").split("=")[1];
						String fileName = URLDecoder.decode(new String(fn.getBytes(HttpContext.Encoder)),
								HttpContext.UTF8);
						fio = new DataOutputStream(
								new BufferedOutputStream(new FileOutputStream(new File("D:\\" + fileName))));
						temp = input[1];
						fio.write(temp.getBytes("ISO-8859-1"));//
						fio.flush();
						log.debug("上传文件成功---- {}", fileName);
					}
					catch (Exception e) {
						log.error("发生异常 {}", e);
					}
					finally {
						fio.close();
					}
				}
				else if (str[i] != null && str[i].indexOf("Content-Disposition:") >= 0) { //普通表单
					String input = str[i].split("\r\n\r\n")[0]; //在分一次，去掉了最后的空格
					String[] lines = input.split("\r\n");
					for (String strs : lines) {
						String[] st = strs.split(";");
						String name = st[st.length - 1].replace("\"", "").split("=")[0];
						String value = st[st.length - 1].replace("\"", "").split("=")[1];
						formmap.put(name, value);
					}
				}
			}

		}

	}

}
