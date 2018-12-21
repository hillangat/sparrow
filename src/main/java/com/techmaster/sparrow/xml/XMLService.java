package com.techmaster.sparrow.xml;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface XMLService {
	
	XMLService copyXMLService(XMLService xmlService);
	String getElementVal(String xPath);
	Element createNewElemet(String name);
	Attr createAttrNode(String name, String value);
	void addElement(String parentPath, Element e, int position);
	String getTextValue(String xPath);
	void transform(Document xml, Document xsl);
	Node getTheFirstElement() throws XPathExpressionException;
	void transform(String xmlLoc, String xslLoc);
	void addAttribute(Element element, String name, String val);
	void removeAttribute(Element element, String name);
	NodeList getNodeList();
	NodeList getAllElementsUnderTag(String string);
	NamedNodeMap getAttrNodeMap(NodeList nodeList, String nodeName);
	NodeList getDocChildNodes();
	NodeList getElementsByTagName(String tagName);
	String getCData(String xPath);
	NodeList getListOfElementsUnderTag(String tag);
	void insertCDataToRootEle(String elementName, String cdata);
	void insertCDataToElement(Element toAppend, String elementName, String cdata);
	NodeList getNodeListForCompiledXpath(String xPath);
	NodeList getNodeListForXPath(String xPath);
	NodeList getNodeListForPathUsingJavax(String xPath);
	XMLTree getXmlTree();
	Node importNode(Node node, Node appendTo, boolean bolean);
	Node getFirstNodeUsingAjaxByName(String nodeName);
	
	

}
