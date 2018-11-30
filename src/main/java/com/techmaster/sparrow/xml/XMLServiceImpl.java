package com.techmaster.sparrow.xml;

import com.techmaster.sparrow.exception.SparrowRunTimeException;
import com.techmaster.sparrow.util.SparrowUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringWriter;


public class XMLServiceImpl implements XMLService{
	
	private static final Logger logger = LoggerFactory.getLogger(XMLServiceImpl.class);
	
	public XMLServiceImpl(XMLTree xmlTree) throws SparrowRunTimeException {
		super();
		if(xmlTree.getDoc() == null){
			throw new SparrowRunTimeException("XML Document not set for the xml tree");
		}
		this.xmlTree = xmlTree;
	}
	
	private XMLTree xmlTree = null;
	private XPath xpath = XPathFactory.newInstance().newXPath();
	
	@Override
	public XMLTree getXmlTree() {
		return xmlTree;
	}
	public void setXmlTree(XMLTree xmlTree) throws Exception {
		if(xmlTree.getDoc() == null){
			throw new Exception("XML Document not set for the xml tree");
		}
		this.xmlTree = xmlTree;
	}

	@Override
	public XMLService copyXMLService(XMLService xmlService) {
		XMLService xmlService2 = null;
		try {
			XMLTree tree = new XMLTree(xmlService.toString(), true);
			xmlService2 = new XMLServiceImpl(tree);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return xmlService2;
	}
	@Override
	public String getElementVal(String xPath) {
		String val = null;
		try {
			val = xpath.compile(xPath).evaluate(this.xmlTree.getDoc());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return val;
	}

	@Override
	public void transform(Document xml, Document xsl) {
		
	}

	@Override
	public void transform(String xmlLoc, String xslLoc) {
		
	}

	@Override
	public void addAttribute(Element element, String name, String value) {
		element.setAttribute(name, value); 
	}
	
	@Override
	public String getTextValue(String xPath) {
		try {
			return xpath.compile(xPath).evaluate(this.xmlTree.getDoc());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	@Override
	public Node getTheFirstElement() throws XPathExpressionException {
		return getNodeList().item(0);
	}
	@Override
	public NodeList getNodeList() {
		Document xmlTreeDoc = this.xmlTree.getDoc();
		Element root = xmlTreeDoc.getDocumentElement();
		NodeList nodeList =  root.getChildNodes();
		if(nodeList.getLength() < 1)
			logger.info(">>>>>>>>>>>> node list is empty"); 
		return nodeList;
	}
	
	/**
	 * @param tag the tag name for which child elements are sought for.
	 * @return Returns an empty nodelist if elements under search are not found.
	 * @throws NullPointerException if tag is null.
	 */
	
	@Override
	public NodeList getAllElementsUnderTag(String tag) {
		
		// return empty node list if the tag is null
		if(tag == null)
			return getEmptyNodeList();
		 
		Document xmlTreeDoc = this.xmlTree.getDoc();
		NodeList nodeList = xmlTreeDoc.getElementsByTagName(tag);
		
		if(SparrowUtility.isNodeListNotEmptpy(nodeList)){
			Node node = nodeList.item(0);
			if(node != null){
				NodeList subNodeList = node.getChildNodes();
				return subNodeList;
			}
		}
		
		// an empty nodeList
		return getEmptyNodeList();
	}
	
	public static NodeList getEmptyNodeList(){
		return new NodeList() {
			@Override
			public Node item(int i) {
				return null;
			}
			@Override
			public int getLength() {
				return 0;
			}
		};
	}
	
	@Override
	public NodeList getListOfElementsUnderTag(String tag) {
		if(tag == null)
			throw new NullPointerException();
		Document xmlTreeDoc = this.xmlTree.getDoc();
		NodeList nodeList = xmlTreeDoc.getElementsByTagName(tag);
		return nodeList;
	}
	
	
	@Override
	public NamedNodeMap getAttrNodeMap(NodeList nodeList, String nodeName) {
		NamedNodeMap map = null;
		for(int i=0; i<nodeList.getLength(); i++){
			if(nodeList.item(i).getNodeName().equals(nodeName)){
				map = nodeList.item(i).getAttributes();
				return map;
			}
		}
		return map;
	}
	@Override
	public NodeList getDocChildNodes() {
		NodeList nodeList = this.getXmlTree().getDoc().getDocumentElement().getChildNodes();
		return nodeList;
	}
	
	@Override
	public NodeList getElementsByTagName(String tagName) {
		NodeList nodeList = this.getXmlTree().getDoc().getElementsByTagName(tagName);
		return nodeList;
	}
	
	@Override
	public String getCData(String xPath) {
		if(!SparrowUtility.notNullNotEmpty(xPath))
			throw new IllegalArgumentException();
		String end = "text()";
		String path = xPath.toLowerCase();
		if(!path.endsWith(end)){
			xPath += '/' + end;
		}
		return getElementVal(xPath);
	}
	
	@Override
	public void insertCDataToRootEle(String elementName, String cdata) {
		Document doc = this.xmlTree.getDoc();
		CDATASection cData = doc.createCDATASection(elementName);
		Element root = doc.getDocumentElement();
		root.appendChild(cData);
	}
	
	@Override
	public void insertCDataToElement(Element toAppend, String elementName, String cdata) {
		Document doc = this.xmlTree.getDoc();
		CDATASection cData = doc.createCDATASection(elementName);
		toAppend.appendChild(cData);
	}
	
	@Override
	public void addElement(String parentPath, Element e, int position) {
		logger.debug("Adding element to path : " + parentPath);
		NodeList nodes = this.xmlTree.getDoc().getElementsByTagName(parentPath);
		if(nodes == null || nodes.getLength() == 0){
			String message = "No node found for the path : " + parentPath;
			logger.debug(message); 
			throw new IllegalArgumentException(message);
		}
		if(nodes.getLength() <= position + 1){
			logger.debug("Position given( "+ position +" ) is larger than the position of the last element("+ (nodes.getLength() - 1) +")"); 
			position = nodes.getLength() - 1;
			logger.debug("Position set to : " + position); 
		}
		nodes.item(0).appendChild(e);
	}
	
	@Override
	public Element createNewElemet(String name) {
		Element ele = this.xmlTree.getDoc().createElement(name);
		return ele;
	}
	
	@Override
	public Attr createAttrNode(String name, String value) {
		Attr attr = this.xmlTree.getDoc().createAttribute(name);
		attr.setNodeValue(value); 
		return attr;
	}
	
	@Override
	public void removeAttribute(Element element, String name) {
		element.removeAttribute(name);
	}
	
	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
		try { 
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		}
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        try {
			transformer.transform(new DOMSource(this.xmlTree.getDoc()), new StreamResult(sw));
		} catch (TransformerException e) {
			e.printStackTrace();
		}
        return sw.toString();
		
	}
	@Override
	public NodeList getNodeListForCompiledXpath(String xPath) {
		return null;
	}
	@Override
	public NodeList getNodeListForXPath(String xPath) {
		NodeList nodes = null;
		try {
			nodes = (NodeList)this.xpath.evaluate(xPath, this.xmlTree.getDoc().getDocumentElement(), XPathConstants.NODESET);
		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
		}
		
		return nodes;
	}
	@Override
	public NodeList getNodeListForPathUsingJavax(String xPathStr) {
		try {
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			javax.xml.xpath.XPathExpression expr = xpath.compile(xPathStr);
			NodeList nl = (NodeList) expr.evaluate(this.getXmlTree().getDoc(), XPathConstants.NODESET);
			return nl;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public Node importNode(Node node, Node appendTo, boolean bolean) {
		Node newNode = getXmlTree().getDoc().importNode(node, bolean);
		appendTo.appendChild(newNode);
		return newNode;
	}
	@Override
	public Node getFirstNodeUsingAjaxByName(String nodeName) {
		NodeList nodeList = getNodeListForPathUsingJavax("//" + nodeName);
		for(int i=0; i<nodeList.getLength();i++){
			Node node = nodeList.item(i);
			if( node.getNodeName().equalsIgnoreCase(nodeName) ){
				return node;
			}
		}
		return null;
	}

	
	
	
	


}
