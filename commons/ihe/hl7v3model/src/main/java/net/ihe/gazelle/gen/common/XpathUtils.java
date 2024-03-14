package net.ihe.gazelle.gen.common;

import java.io.ByteArrayInputStream;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.sf.saxon.xpath.XPathFactoryImpl;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XpathUtils {
	
	public static Boolean evaluateByString(String string, String nodeName, String expression,
			NamespaceContext namespaceContext) throws Exception {
		Node clin = getNodeFromString(string, nodeName);
		return evaluateByNode(clin, expression, namespaceContext);
	}

	public static Boolean evaluateByString(String string, String nodeName, String expression) throws Exception {
		Node clin = getNodeFromString(string, nodeName);
		return evaluateByNode(clin, expression);
	}
	
	public static Boolean evaluateByNode(Node node, String expression, NamespaceContext namespaceContext) 
			throws Exception {
		Boolean b = null;
		XPathFactory fabrique = new XPathFactoryImpl();
		XPath xpath = fabrique.newXPath();
		xpath.setNamespaceContext(namespaceContext);
		b = (Boolean) xpath.evaluate(expression, node, XPathConstants.BOOLEAN);
		return b;
	}

	public static Boolean evaluateByNode(Node node, String expression) throws Exception {
		return evaluateByNode(node, expression, new DatatypesNamespaceContext());
	}

	private static Node getNodeFromString(String string, String nodeName) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new ByteArrayInputStream(string.getBytes()));
		NodeList dd = doc.getElementsByTagName(nodeName);
		return dd.item(0);
	}

}
