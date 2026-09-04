package net.ihe.gazelle.gen.common;

import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class XmlUtil {

	public static Document parse(String document) throws Exception {
		var bais = new ByteArrayInputStream(document.getBytes(StandardCharsets.UTF_8));

		// Rebuild the document
		var builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(true);

		// Countermeasures against XXE and entity-expansion attacks
		builderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		builderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		builderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		builderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		builderFactory.setXIncludeAware(false);
		builderFactory.setExpandEntityReferences(false);

		var builder = builderFactory.newDocumentBuilder();
        return builder.parse(bais);
	}

}
