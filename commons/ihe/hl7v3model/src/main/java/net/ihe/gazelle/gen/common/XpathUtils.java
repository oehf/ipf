package net.ihe.gazelle.gen.common;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.XMLConstants;
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
		var clin = getNodeFromString(string, nodeName);
		return evaluateByNode(clin, expression, namespaceContext);
	}

	public static Boolean evaluateByString(String string, String nodeName, String expression) throws Exception {
		var clin = getNodeFromString(string, nodeName);
		return evaluateByNode(clin, expression);
	}
	
	public static Boolean evaluateByNode(Node node, String expression, NamespaceContext namespaceContext) 
			throws Exception {
		Boolean b = null;
		var fabrique = new XPathFactoryImpl();
        var xpath = fabrique.newXPath();
		xpath.setNamespaceContext(namespaceContext);
		b = (Boolean) xpath.evaluate(expression, node, XPathConstants.BOOLEAN);
		return b;
	}

	public static Boolean evaluateByNode(Node node, String expression) throws Exception {
		return evaluateByNode(node, expression, new DatatypesNamespaceContext());
	}

	private static Node getNodeFromString(String string, String nodeName) throws Exception {
        var dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);

		// Countermeasures against XXE and entity-expansion attacks
		dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
		dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		dbf.setXIncludeAware(false);
		dbf.setExpandEntityReferences(false);

        var db = dbf.newDocumentBuilder();
		// getBytes() without a charset used the platform default, corrupting non-ASCII content
        var doc = db.parse(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
        var dd = doc.getElementsByTagName(nodeName);
		return dd.item(0);
	}

}
