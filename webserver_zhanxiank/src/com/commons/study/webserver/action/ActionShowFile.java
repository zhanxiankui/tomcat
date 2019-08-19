package com.commons.study.webserver.action;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ActionShowFile {

	static final Logger log = LoggerFactory.getLogger(ActionShowFile.class);
	
	/**
	 * 查询界面
	 * @param req
	 * @param res
	 */
	public void show(Request req, HttpResponse res) {
		if (req == null || res == null) {
			return;
		}

		String cmd = req.getParameter("path");
		String parent=req.getParameter("mark");

		if (cmd != null) {
			try {
				res.responseHtml(FileUtil.getDivHtml(cmd,parent));
			}
			catch (IOException e) {

			}

		}

	}

}
