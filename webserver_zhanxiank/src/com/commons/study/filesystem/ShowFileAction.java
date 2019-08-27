package com.commons.study.filesystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.commons.study.springmvc.annotation.Controller;
import com.commons.study.springmvc.annotation.RequestMapping;
import com.commons.study.webserver.entity.HttpContext;
import com.commons.study.webserver.entity.MyFile;
import com.commons.study.webserver.net.HttpRequest;
import com.commons.study.webserver.net.HttpResponse;
import com.commons.study.webserver.net.Request;
import com.commons.study.webserver.net.Response;
import com.commons.study.webserver.util.FileUtil;

/**
 * 文件展示
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月6日
 */

@Controller
public class ShowFileAction {
	static final Logger log = LoggerFactory.getLogger(ShowFileAction.class);
	
	/**
	 * 查询界面
	 * @param req
	 * @param res
	 * @throws IOException 
	 */
	@RequestMapping("files")
	public void show(HttpRequest req, HttpResponse res) throws IOException {
		
		String cmd = req.getParameter("path");
		String mark = req.getParameter("mark");
		res.setContentType(req.getContentType());
		res.setHeader("Content-Type", "text/html; Charset=UTF-8");
		if(cmd==null){
			return;
		}	
		if ( cmd.length() <= 3) {
			File file = new File(HttpContext.webdir + "/" + "file.html");
			String html = FileUtil.fileToString(file);
			html = html.replace("replacesdiv", FileUtil.getDivHtml(cmd, mark));
			res.responseHtml(html);
		}
		else {
			res.responseHtml(FileUtil.getDivHtml(cmd,mark));
			}
		}
}
