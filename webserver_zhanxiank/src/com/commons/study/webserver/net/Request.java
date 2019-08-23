package com.commons.study.webserver.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;


/**
 *  请求协议接口，规划化，制定标准。
 * @return
 */

public interface Request {
	

    public String getProtocol(); 
    
    public String getContentType();
    
    public void setContentType(String value);
    
    public void setCharacterEncoding(String env);

    public String getCharacterEncoding();
    
    public Map<String, String> getHeader();
    
    public String getMethod();
    
    public String getRequestURL();
        
    public InputStream getInputStream() throws IOException;
    
    public String getParameter(String name);
  
    public void setAttribute(String name, Object o);
    
    public Object getAttribute(String name);
    
    public Enumeration<String> getAttributeNames();
   

}
