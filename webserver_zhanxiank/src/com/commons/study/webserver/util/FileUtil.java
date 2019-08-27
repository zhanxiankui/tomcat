package com.commons.study.webserver.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.commons.study.webserver.entity.HttpContext;
import com.commons.study.webserver.entity.MyFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 文件工具类，定义了对文件操作的函数
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author zhanxiank
 * @createdate 2019年8月19日
 */
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
		if (files == null) { //有些隐藏文件属性很诡异，为空也直接还回
			return filelist;
		}
		ArrayList<MyFile> list1 = new ArrayList<>();
		ArrayList<MyFile> list2 = new ArrayList<>();
		for (File f : files) { //文件夹在前面，文件在后面
			//			filelist.add(new MyFile(f));
			if (f.isDirectory()) {
				list1.add(new MyFile(f)); //文件夹
			}
			else {
				list2.add(new MyFile(f));
			}
		}
		list1.addAll(list2);
		return list1;
	}

	public static String fileToString(File file) throws UnsupportedEncodingException {
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

		return str; //转成utf-8编码
	}

	public static String fileToUTFString(File file) {
		BufferedReader buf = null;
		StringBuilder sb = new StringBuilder();
		String temp = "";
		try {
			String fileCode = gueseCode(file);
			buf = new BufferedReader(new InputStreamReader(new FileInputStream(file), fileCode));
			while ((temp = buf.readLine()) != null) {
				sb.append(temp + System.lineSeparator());
			}
			temp = new String(sb.toString().getBytes(), "utf-8");
		}
		catch (Exception e) {
			log.error(e.toString());
		}
		finally {
			try {
				buf.close();
			}
			catch (IOException e) {
				log.error(e.toString());
			}
		}
		return temp;
	}

	
	public static String getJsonInfo(File f) throws JsonProcessingException {
		if (f == null) {
			return null;
		}
		String[] t = f.getPath().split("\\\\");
		int num = t.length;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> fMap = new HashMap<>();
		fMap.put("name", f.getName());
		fMap.put("isdir", f.isDirectory() + "");
		fMap.put("leave", String.valueOf(num));
		fMap.put("path", f.getPath());
		return mapper.writeValueAsString(fMap);
	}

	public static String getDivHtml(String cmd, String parent) {

		File fle = new File(cmd);
		if (fle.isFile()) { //如果是文件,没有还回
			return null;
		}
		ArrayList<MyFile> list = FileUtil.getDirFils(cmd); //获取下一级目录	
		StringBuilder sb = new StringBuilder();
		String pa = cmd; //默认路径
		if (list.size() > 0) { //下一级目录有文件
			File f = list.get(0).getFile().getParentFile() == null ? list.get(0).getFile()
					: list.get(0).getFile().getParentFile();
			pa = f.getParent() == null ? f.getPath() : f.getParent().replace("\\", "/");
		}
		else { //下一级目录为空
			pa = fle.getParent();
			if (parent != null) //这是还回的操作。
				list = FileUtil.getDirFils(pa);
		}
		pa = pa + "&mark=parent";
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

	public static String writeFile(File f, String data) throws IOException {
		OutputStream out = null;
		try {
			out = new FileOutputStream(f);
			out.write(data.getBytes());
			return "ok";
		}
		finally {
			out.close();
		}
	}

	public static File getRealFileLocate(Object path) {

		String url = String.valueOf(path);
		File file = null;
		if (url.indexOf("\\") > -1) { //完整路径
			file = new File(url);
		}
		else { //这是web容器下的路径。
			file = new File(HttpContext.webdir + "/" + path);
		}
		return file;
	}
	
	
	
	public static List<String> getRealClassPath(String packageName) {
		List<String> list=new LinkedList<>();
	
		URL url= FileUtil.class.getClassLoader().getResource(packageName.replace(".", "/"));
		if(url!=null){
			   String path=url.getPath();
			   String[] temp=new File(path).list();
				for(String st:temp){
					list.add(packageName+"."+st.split("\\.")[0].trim());	
				}
		}
//		String path=FileUtil.class.getClassLoader().getResource(packageName.replace(".", "/")).getPath();
//		String[] temp=new File(path).list();
//		for(String st:temp){
//			list.add(packageName+"."+st.split("\\.")[0].trim());	
//		}
		return list;
	}
	
	public static String gueseCode(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				bis.close();
				return charset; // 文件编码为 ANSI
			}
			else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE"; // 文件编码为 Unicode
				checked = true;
			}
			else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE"; // 文件编码为 Unicode big endian
				checked = true;
			}
			else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8"; // 文件编码为 UTF-8
				checked = true;
			}
			bis.reset();
			if (!checked) {
				while ((read = bis.read()) != -1) {
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
							// (0x80 - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					}
					else if (0xE0 <= read && read <= 0xEF) { // 也有可能出错，但是几率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							}
							else
								break;
						}
						else
							break;
					}
				}
			}
			bis.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("--文件- 采用的字符集为: [" + charset + "]");
		return charset;
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
