package com.commons.study.webserver.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.action.ActionShowFile;
import com.commons.study.webserver.entity.MyFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.LogbackException;

public class FileUtil {

	static final Logger log = LoggerFactory.getLogger(FileUtil.class);

	private File file;

	private String path;

	public FileUtil() {

	}

	public FileUtil(File file) {
		this.file = file;
	}

	public FileUtil(String path) {
		this.path = path;
	}

	/**
	 * 获取目录下的所有文件
	 * @return
	 */
	public static ArrayList<MyFile> getDirFils(String path) {

		File file = new File(path);
		ArrayList<MyFile> filelist = new ArrayList<>();
		if (file.isFile()) {
			return filelist; //是文件没有下一级目录直接还回
		}
		
		File[] files = file.listFiles(); 
		if (files == null) {                  //有些隐藏文件属性很诡异，忽略
			files = new File("D:/succezIDE").listFiles();
		}

		for (File f : files) {
			filelist.add(new MyFile(f));
		}
		return filelist;
	}

	
	public static String fileToString(File file) {
		String str = "";
		try {
			FileInputStream in = new FileInputStream(file);
			// size 为字串的长度 ，这里一次性读完
			int size = in.available();
			byte[] buffer = new byte[size];
			in.read(buffer);
			str = new String(buffer);
			in.close();
		}
		catch (IOException e) {
			log.error("读取文件出错{}", e);
		}

		return str;
	}

	public static boolean isContainsChinese(String str) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find()) {
			flg = true;
		}
		return flg;
	}
	
	
	
	public static String getJsonInfo(File f) throws JsonProcessingException {
		
		if(f==null){
			return null;
		}
		String[] t=f.getPath().split("/");
		int num=t.length;
	    
	    ObjectMapper mapper=new ObjectMapper();
	    Map<String, String> fMap=new HashMap<>();
	    fMap.put("name", f.getName());
	    fMap.put("isdir", f.isDirectory()+"");
	    fMap.put("leave", String.valueOf(num));
	    fMap.put("path", f.getPath());
		
		return mapper.writeValueAsString(fMap)	;
	}
	
	

	public static String getDivHtml(String cmd,String parent) {

		File fle = new File(cmd);
		if (fle.isFile()) { //如果是文件,没有还回
			return null;
		}

		ArrayList<MyFile> list = FileUtil.getDirFils(cmd);  //获取下一级目录	
		StringBuilder sb = new StringBuilder();
		String pa =cmd; //默认路径

		if (list.size() >0) { //下一级目录有文件
			File f = list.get(0).getFile().getParentFile() == null ? list.get(0).getFile()
					: list.get(0).getFile().getParentFile();
			pa = f.getParent() == null ? f.getPath() : f.getParent().replace("\\", "/");
		}else{   //下一级目录为空
			
			pa=fle.getParent();		
			if(parent!=null)  //这是还回的操作。
			list=FileUtil.getDirFils(pa);	
			
		}
		
		pa=pa+"&mark=parent";

		//   		path=path.replace("\\", "/");
		sb.append("<p> <a  href=" + "'" + pa + "'" + "  onclick= \"getDatas(" + "'" + pa + "'" + ")\" >" + "上一级目录"
				+ "  </a></p>");
		sb.append(" <table id=\"files\" border=\"0\" width=\"80%\"> ").append(
				"  <tr>  <th class=\"gvs\">名称 </th>  <th class=\"gvs\">大小</th>  <th class=\"gvs\">修改日期</th>  </tr> ");

		for (MyFile file : list) {
			String html = file.toHtml();
			sb.append(html);
		}
		sb.append("</table>");
		return sb.toString();

	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
