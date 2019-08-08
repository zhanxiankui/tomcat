package com.commons.study.threedemo;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class FileToByteTest {
	
	@Test
	public void test() throws Exception {
		
		String pathname=this.getClass().getResource("").getFile();  //获取java文件所在的目录。
		File f=new File(pathname+"test.txt");
		
		FileToByte fileToByte=new FileToByte();
		byte[] bs =fileToByte.file2buf(f);

		for (byte b : bs) {
			System.out.println("----" + b);
		}
	}

	
	
	@Test
	public void test1() throws Exception {
		
		String pathname=this.getClass().getResource("").getFile();  //获取java文件所在的目录。
		File f=new File(pathname+"test1.txt");
		
		FileToByte fileToByte=new FileToByte();		
		Assert.assertEquals(null, fileToByte.file2buf(f));
		
	
	}
}
