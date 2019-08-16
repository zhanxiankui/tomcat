package com.commons.study.threedemo.test;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.commons.study.threedemo.FileToByte;


/**
 *
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author zhanxiank
 * @createdate 2019年8月9日
 */

public class FileToByteTest {
	
	@Test
	public void test() throws Exception {  //正确的文件
		
		byte[] bs = new FileToByte().file2buf(getFiles("test.txt"));
		
		Assert.assertEquals(14, bs.length); //正常测试
		
		Assert.assertNull(new FileToByte().file2buf(new File("D:/document"))); //测试目录。

		Assert.assertEquals(null, new FileToByte().file2buf(null));  //测试为空。
		
		Assert.assertEquals(null, new FileToByte().file2buf(getFiles("a.txt")));
	}

	
	public File getFiles(String fileName) {
		
		String pathname=this.getClass().getResource("").getFile();  //获取java文件所在的目录。
		return new File(pathname+fileName);
	}
	
	
	
	
}
