package net.ihe.gazelle.gen.common;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class XmlUtil {

	public static Document parse(String document) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(document.getBytes(StandardCharsets.UTF_8));

		// Rebuild the document
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(true);

		DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(bais);
	}

}
