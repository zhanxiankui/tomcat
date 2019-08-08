package com.commons.study.webserver.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.action.ActionShowFile;
import com.commons.study.webserver.entity.MyFile;

import ch.qos.logback.core.LogbackException;

public class FileUtil {
	
	static final Logger log = LoggerFactory.getLogger( FileUtil.class);
	
	
	private File file;
	private String path;
	
	public FileUtil(){
		
	}
	
	public FileUtil(File file){
		this.file=file;
	}
	
	
	public FileUtil(String path){
		this.path=path;
	}
	
	
	/**
	 * 获取目录下的所有文件
	 * @return
	 */
	public static ArrayList<MyFile> getDirFils(String path) {
		
		File file=new File(path);
		File[] files=file.listFiles();
		       if(files==null){
		    	   files=new File("D:/succezIDE").listFiles();
		       }
		ArrayList<MyFile> filelist=new ArrayList<>();
		
        for(File f:files){
        	filelist.add(new MyFile(f));
        }		
		return filelist;		
	}
	
	
	public static String fileToString(File file) {
		String str="";	
			try {
				 FileInputStream in=new FileInputStream(file);
				 // size 为字串的长度 ，这里一次性读完
				 int size=in.available();
				 byte[] buffer=new byte[size];
				 in.read(buffer);
				 str= new String(buffer);
				 in.close();

		}
		catch (IOException e) {
			log.error("读取文件出错{}",e);
		}
	
			return str;
	}
	
	
	public static String getDivHtml(String cmd) {
		
 	   ArrayList<MyFile> list=FileUtil.getDirFils(cmd);   
   		StringBuilder  sb=new StringBuilder();
   		String pa="D:/";  //默认路径
   		if(list.size()>0){
   			File f=list.get(0).getFile().getParentFile()==null?list.get(0).getFile():list.get(0).getFile().getParentFile();
   			pa=f.getParent()==null?f.getPath():f.getParent().replace("\\", "/");
   		}
   		
//   		path=path.replace("\\", "/");
   		sb.append("<p> <a  href="+"'" +pa +"'"  + "  onclick= \"getDatas(" +"'" +pa +"'"+")\" >" +"上一级目录"+"  </a></p>");
 		sb.append(" <table id=\"files\" border=\"0\" width=\"80%\"> ").
 	     append( "  <tr>  <th class=\"gvs\">名称 </th>  <th class=\"gvs\">大小</th>  <th class=\"gvs\">修改日期</th>  </tr> ");
 			  
 	   for(MyFile file: list){	   
 		  String html=file.toHtml();   
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
