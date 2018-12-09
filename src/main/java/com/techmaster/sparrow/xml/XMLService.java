package com.techmaster.sparrow.xml;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface XMLService {
	
	public XMLService copyXMLService(XMLService xmlService);
	public String getElementVal(String xPath);
	public Element createNewElemet(String name);
	public Attr createAttrNode(String name, String value);
	public void addElement(String parentPath, Element e, int position);
	public String getTextValue(String xPath);
	public void transform(Document xml, Document xsl); 
	public Node getTheFirstElement() throws XPathExpressionException;
	public void transform(String xmlLoc, String xslLoc);
	public void addAttribute(Element element, String name, String val);
	public void removeAttribute(Element element, String name);
	public NodeList getNodeList();
	public NodeList getAllElementsUnderTag(String string);
	public NamedNodeMap getAttrNodeMap(NodeList nodeList, String nodeName);
	public NodeList getDocChildNodes();
	public NodeList getElementsByTagName(String tagName);
	public String getCData(String xPath);
	public NodeList getListOfElementsUnderTag(String tag);
	public void insertCDataToRootEle(String elementName, String cdata);
	public void insertCDataToElement(Element toAppend, String elementName, String cdata);
	public NodeList getNodeListForCompiledXpath(String xPath);
	public NodeList getNodeListForXPath(String xPath);
	public NodeList getNodeListForPathUsingJavax(String xPath);
	public XMLTree getXmlTree();
	public Node importNode(Node node, Node appendTo, boolean bolean);
	public Node getFirstNodeUsingAjaxByName(String nodeName);
	
	

}
