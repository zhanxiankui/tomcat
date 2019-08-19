package com.commons.study.webserver.entity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 文件对象，记录文件的一些信息。 每一次相应查询一次，设计一个缓存上一级目录。
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月2日
 */
public class MyFile {

	private boolean  isDirect; //是否是目录。
	
	private String fileName; //文件名称

	private long size; //文件大小

	private Date mdtime; //修改日期

	private String path; //文件的路径。
		
	private File file; //文件本身
	
	
	public MyFile (String path) {
		this.path=path;
		this.file=new File(path);
		this.isDirect=file.isDirectory();
		this.size=file.length();	
		this.mdtime=new Date(file.lastModified());
		this.fileName=file.getName();
	}
	

	public MyFile(File file){
         this(file.getPath());
	}
	

	
	public File getFile() {
		return file;
	}




	public void setFile(File file) {
		this.file = file;
	}



	
	public boolean isDirect() {
		return isDirect;
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getSize() {
		return size;
	}



	public Date getMdtime() {
		return mdtime;
	}

	public void setMdtime(Date mdtime) {
		this.mdtime = mdtime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}



	public void setSize(long size) {
		this.size = size;
	}


	@Override
	public String toString() {
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String time=sfd.format(mdtime);
		
		return "isDirect:" + isDirect + ", fileName:" + fileName + ", size:" + size + ", mdtime:" + time
				+ ", path:" + path ;
	}

	
	public String toHtml(){

		SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String time=sfd.format(mdtime);	
		StringBuilder  sb=new StringBuilder();
		path=path.replace("\\", "/");
		
       sb.append("<tr> <td> <a  href="+"'" +path +"'"  + "  onclick= \"getDatas(" +"'" +path +"'"+")\" >" +path+"  </a> </td> ")
       .append(" <td>" +size+"b</td>  <td> "+  time+  "</td>    </tr>");
       
	   return sb.toString();
			
	}
	
}
