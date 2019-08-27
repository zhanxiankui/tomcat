package com.commons.study.webserver.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commons.study.webserver.entity.HttpContext;


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

	public List<String> getNodeData(String fileName, String xpath,boolean needAttr) {
		Document doc = getDocument(fileName);
		List<String> temp = new ArrayList<String>();
		for (Node node : doc.selectNodes(xpath)) {
			String value = node.getText().trim().replace("\r\n", "");
			if(needAttr){
				  Element e=(Element) node;
				  value=e.attributeValue("base-package").trim().replace("\r\n", "");
			}
			if (value.indexOf(",") > -1) { //处理空格分隔的问题。
				String[] t = value.split(",");
				for (String st : t) {
					temp.add(st);
				}
			}
			else {
				temp.add(value);
			}
		}
		return temp;
	}
	
	/**
	 * 采用dom4j的 xpath解析需要的数据
	 * @param fileName
	 */
	
	public Map<String,List<String>> parseSpringmvcXml(String fileName){
		
	    Map<String,List<String>> springxml=new HashMap<>();
		springxml.put("servlet-class", getNodeData(fileName, "//servlet-class",false));
		springxml.put("url-pattern", getNodeData(fileName, "//url-pattern",false));	
		springxml.put("component-scan", getNodeData(fileName, "//component-scan",true));	
	    return springxml;
	}
	

}
