package com.commons.study.webserver.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  xml操作类
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author zhanxiank
 * @createdate 2019年8月26日
 */
public class XmlUtil {
	
	private static final Logger log=LoggerFactory.getLogger(XmlUtil.class);
	
	public Document getDocument(String fileName){
		Document document=null;
		SAXReader reader=new SAXReader();
		try {
		document=reader.read(new File(fileName));
		}
		catch (DocumentException e) {
			log.error("解析xml发送异常: {}",e);
		}	
		return document;
	}
	

	
	public Map<String, String> parseXmlToMap(String fileName) {
		Map<String, String> map=new HashMap<String, String>();
		Document docum=getDocument(fileName);
		Element root=docum.getRootElement();
		List<Element> elemslist=root.elements();
		for(Element elem:elemslist){
			map.put(elem.getName(),elem.getText());	
		}	
		return map;
	}
	
	
	
	public void parseSpringmvcXml(String fileName){
		Document doc=getDocument(fileName);
		
		
	}
	
}
