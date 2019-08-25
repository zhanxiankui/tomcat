package com.commons.study.webserver.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.entity.HttpContext;
import com.commons.study.webserver.entity.MyFile;
import com.commons.study.webserver.entity.TreeNode;
import com.commons.study.webserver.net.HttpRequest;
import com.commons.study.webserver.net.HttpResponse;
import com.commons.study.webserver.server.HttpServer;
import com.commons.study.webserver.util.FileUtil;

//测试
public class TestDemo {

	static final Logger log = LoggerFactory.getLogger(HttpContext.class);

	@Test
	public void test() throws ParseException, UnknownHostException, IOException {

		String src = "今天的天气真的不好";

        byte[] buff = new byte[8];

        InputStream is = new ByteArrayInputStream(src.getBytes("utf-8"));

        int len = -1;

        while(-1 != (len = is.read(buff))) {

            String str1 = new String(buff, 0, len);//new String(buff, 0, len, "UTF-8")这是两个方法的合体

            String str2 = new String(buff, "UTF-8");

            System.out.println(str1);

            System.out.println(str2);

        }

	}
	
	
	
	@Test
	public void test1() throws IOException {
		String str="\r\n";
	    BufferedInputStream bf=new BufferedInputStream(new FileInputStream("D:/test.txt"));
        int  n=bf.read();
		while (n!=-1) {
			n=bf.read();
			System.out.println(((char) n)+"--------"+n);
			
		}
		bf.close();
	}
	
	
	@Test
	public void test3() throws UnsupportedEncodingException {
		String str="dflx\r\nfghh\r\ngh\r\n";
		StringBuilder sb=new StringBuilder();
		String[] st=str.split("\r\n",-1);
		
		for(int i=0; i<st.length; i++){
		   sb.append(st[i]);
		   sb.append("\r\n");
		       
		}
		
		  if(st[st.length-1].equals(""))
			   sb.delete(sb.toString().length()-2, sb.toString().length());
		
		
		 byte[] len =sb.toString().getBytes("utf-8");
		    System.out.println(sb+"------rrrr--------"+len.length+"--"+sb.toString().equals(str));
		
		
	}

}
