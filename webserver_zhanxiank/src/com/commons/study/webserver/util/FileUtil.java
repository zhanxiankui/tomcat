package com.commons.study.webserver.util;

import java.io.File;
import java.util.ArrayList;

import com.commons.study.webserver.entity.MyFile;

public class FileUtil {
	
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
