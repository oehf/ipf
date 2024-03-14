package net.ihe.gazelle.gen.common;

import java.io.InputStream;
import java.io.OutputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import net.ihe.gazelle.validation.DetailedResult;

public final class DetailedResultMarshaller {
	
	private DetailedResultMarshaller() {}
	
	public static DetailedResult load(InputStream is) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(DetailedResult.class);
		Unmarshaller u = jc.createUnmarshaller();
		return (DetailedResult) u.unmarshal(is);
	}
	
	public static void save(OutputStream os, DetailedResult txdw) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(DetailedResult.class);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
		m.marshal(txdw, os);
	}

}
