package net.ihe.gazelle.gen.common;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

public class DatatypesNamespaceContext implements NamespaceContext{
	
	public String getNamespaceURI(String prefix) {
		if ("cda".equals(prefix)) {
			return "urn:hl7-org:v3";
		} 
		else if ("hl7".equals(prefix)) {
			return "urn:hl7-org:v3";
		}
		else if ("xsi".equals(prefix)) {
			return "http://www.w3.org/2001/XMLSchema-instance";
		}
		return null;
	}

	public String getPrefix(String namespaceURI) {
		if ("urn:hl7-org:v3".equals(namespaceURI)) {
			return "cda";
		} else if ("http://www.w3.org/2001/XMLSchema-instance".equals(namespaceURI)) {
			return "xsi";
		}
		return null;
	}

	public Iterator<String> getPrefixes(String namespaceURI) {
		return null;
	}
}

