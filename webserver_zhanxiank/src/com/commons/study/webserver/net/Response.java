package com.commons.study.webserver.net;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;


/**
 *   相应的接口
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author w
 * @createdate 2019年8月6日
 */
public interface Response {
	
	 public String getContentType();
	 
	 public OutputStream getOutputStream() throws IOException;
	 	 
	 public void setCharacterEncoding(String charset);
	 
	 public String getCharacterEncoding();
	 
	 public void setContentType(String type);
	 
	 public void setHeader(String name, String value);
	 
	 public void addHeader(String name, String value);
	  
	 public  Map<String, String> getHeader(String name);
	 	 	 
	 public void setStatus(String msg);

	 public String getStatus();

}
